package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.entidades.Mensaje;
import com.edu.egg.meetdia.com.entidades.Persona;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.MensajeRepositorio;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MensajeServicio {
    
	@Autowired
	private MensajeRepositorio mensajeRepositorio;

	@Transactional
	public void crearMensaje(String id, String contenido, Persona emisor, Persona receptor) throws ErrorServicio {
		if (contenido != null) {
			throw new ErrorServicio("No se puede crear un mensaje vacio");
		}
                Mensaje mensaje = new Mensaje();

		mensaje.setContenido(contenido);
		mensaje.setFecha(new Date());
                Persona emite = emisor;
		mensaje.setEmisor(emite);
                Persona recibe = receptor;
		mensaje.setReceptor(recibe);

		mensajeRepositorio.save(mensaje);
	}
	

	public List<Mensaje> buscarMensajes(String contenido, Persona emisor, Persona receptor, Date fecha)
			throws ErrorServicio {

		List<Mensaje> Chat = mensajeRepositorio.buscarMensajeEmisorReceptor(emisor, receptor);
		if (Chat != null) {
			throw new ErrorServicio("No se puede crear un mensaje vacio");

		} else {

			return Chat;
		}

	}
}