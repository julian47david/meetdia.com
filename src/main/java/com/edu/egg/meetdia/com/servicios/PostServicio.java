package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.entidades.Post;
import com.edu.egg.meetdia.com.enumeraciones.Categoria;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import com.edu.egg.meetdia.com.repositorios.PostRepositorio;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostServicio {
    
    @Autowired
    PostRepositorio postRepositorio;
    
    @Autowired
    PersonaRepositorio personaRepositorio;
    
    @Autowired
    MultimediaServicio ms;
    
    @Transactional
    public void nuevoPost(String titulo, String descripcion, String idPersona, MultipartFile archivo, String categoria, boolean busco) throws ErrorServicio{
        validar(titulo, descripcion, categoria);
        Post post = new Post();
        post.setTitulo(titulo);
        post.setDescripcion(descripcion);
        post.setPersona(personaRepositorio.buscarPersona(idPersona));
        post.setFecha_publicacion(new Date());
        post.setMultimedia(ms.guardar(archivo));
        post.setCategoria(Categoria.valueOf(categoria));
        post.setBusco(busco);
        postRepositorio.save(post);
    }
    private void validar(String titulo, String descripcion,String categoria) throws ErrorServicio{
        if (titulo==null || titulo.isEmpty()){
            throw new ErrorServicio("Debe ingresar un titulo");
        }
        if (descripcion == null || descripcion.isEmpty()){
            throw new ErrorServicio("Debe ingresar una descripcion");
        }
        if (categoria == null || categoria.isEmpty() ){
            throw new ErrorServicio("La categoria debe ser seleccionada");
        }
    }
}
