package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.entidades.ConfirmationToken;
import com.edu.egg.meetdia.com.entidades.Persona;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.ConfirmationTokenRepositorio;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class PersonaServicio implements UserDetailsService {

    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Autowired
    private MultimediaServicio ms;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ConfirmationTokenRepositorio confirmationTokenRepositorio;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String nickname, String profesion, String ciudad, String cp, String email, String clave1, String clave2) throws ErrorServicio {
        validar(nombre, nickname, profesion, ciudad, email, clave1, clave2);

        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setNickname(nickname);
        persona.setEmail(email);
        persona.setProfesion(profesion);
        persona.setCodpostal(cp);
        persona.setCiudad(ciudad);
        persona.setMultimedia(ms.guardar(archivo));
        persona.setIs_enabled(false);

        String encriptada = new BCryptPasswordEncoder().encode(clave1);
        persona.setClave(encriptada);

        personaRepositorio.save(persona);

        ConfirmationToken confirmationToken = new ConfirmationToken(persona);

        confirmationTokenRepositorio.save(confirmationToken);

        emailSenderService.sendEmail(persona.getEmail(), "Completa tu Registro a meetdia.com!", "meetdia", "Para confirmar tu cuenta haz click aquí: "
                + ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+"/confirm-account?token=" + confirmationToken.getConfirmationToken());
    }

    @Transactional
    public void eliminarPersona(String idPersona) throws ErrorServicio {

        Persona respuesta = personaRepositorio.buscarPersona(idPersona);

        if (respuesta != null) {
            personaRepositorio.delete(respuesta);
        } else {
            throw new ErrorServicio("No se encontro la persona solicitada");
        }
    }
    
    @Transactional
    public void modificarContraseña(Persona persona, String clave1, String clave2) throws ErrorServicio {
         if (clave1 == null || clave1.isEmpty() || clave1.length() < 6) {
            throw new ErrorServicio("La clave de usuario no puede ser nula, y debe ser mayor a 6 caracteres");
        }
        if (!clave1.equals(clave2)) {
            throw new ErrorServicio("Las claves deben coincidir");
        }
        
        String encriptada = new BCryptPasswordEncoder().encode(clave1);
        persona.setClave(encriptada);
    }

    @Transactional
    public void modificarPersona(String idPersona, String nombre, String profesion, String domicilio, String codpostal, String ciudad, String email, String alias, String clave, String mime, byte[] contenido) throws ErrorServicio {

        validar(nombre, alias, profesion, ciudad, email, clave, clave);
        nombre = nombre.toUpperCase();
        profesion = profesion.toUpperCase();
        domicilio = domicilio.toUpperCase();
        ciudad = ciudad.toUpperCase();

        Optional<Persona> respuesta = personaRepositorio.findById(idPersona);

        if (respuesta.isPresent()) {
            Persona persona = respuesta.get();
            persona.setNombre(nombre);
            persona.setProfesion(profesion);
            persona.setDomicilio(domicilio);
            persona.setCodpostal(codpostal);
            persona.setCiudad(ciudad);
            persona.setEmail(email);
            persona.setNickname(alias);
            persona.setClave(clave);

//			String idMultimedia = null;                                          Hacer cuando este la multimedia
//             if(persona.getcontenidoMultimedia() != null){
//                 idMultimedia = persona.getcontenidoMultimedia().getId();
//             }
//             
//             Multimedia multimedia = MultimediaServicio.actualizar(idMultimedia, archivo);
//             persona.setcontenidoMultimedia(contenidoMultimedia);
            personaRepositorio.save(persona);

        } else {
            throw new ErrorServicio("No se encontro la persona solicitada");
        }
    }

    private void validar(String nombre, String nickname, String profesion, String ciudad, String email, String clave, String clave2) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (profesion == null || profesion.isEmpty()) {
            throw new ErrorServicio("La profesión no puede ser nula");
        }
        if (ciudad == null || ciudad.isEmpty()) {
            throw new ErrorServicio("La ciudad no puede ser nula");
        }
        Persona personapormail = personaRepositorio.buscarPersonaporEmail(email);
        if (personapormail != null) {
            throw new ErrorServicio("El E-Mail ya esta siendo utilizado");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorServicio("El E-Mail no puede ser nulo");
        }
        if (clave == null || clave.isEmpty() || clave.length() < 6) {
            throw new ErrorServicio("La clave de usuario no puede ser nula, y debe ser mayor a 6 caracteres");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben coincidir");
        }
        Persona personaporalias = personaRepositorio.buscarNickname(nickname);
        if (personaporalias != null) {
            throw new ErrorServicio("El nickname ya esta siendo utilizado");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Persona persona = personaRepositorio.buscarPersonaporEmail(mail);
        if (persona != null) {
            if (persona.isIs_enabled()) {
                List<GrantedAuthority> permisos = new ArrayList<>();
                GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
                permisos.add(p1);

                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpSession session = attr.getRequest().getSession(true);
                session.setAttribute(("Usuariosession"), persona);

                User user = new User(persona.getEmail(), persona.getClave(), permisos);
                return user;
            }
        }
        return null;
    }
}
