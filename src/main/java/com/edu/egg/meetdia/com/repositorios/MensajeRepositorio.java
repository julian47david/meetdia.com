package com.edu.egg.meetdia.com.repositorios;

import com.edu.egg.meetdia.com.entidades.Mensaje;
import com.edu.egg.meetdia.com.entidades.Persona;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepositorio extends JpaRepository<Mensaje,String> {

	@Query("SELECT a FROM Mensaje a WHERE a.id= :id")
	public Mensaje buscarMensaje(@Param("id") String id);

	@Query("SELECT a FROM Mensaje a WHERE a.fecha= :fecha")
	public Mensaje buscarFecha(@Param("fecha") Date fecha);

	@Query("SELECT a FROM Mensaje a WHERE a.contenido= :contenido")
	public Mensaje buscarContenido(@Param("contenido") String contenido);

	@Query("SELECT a FROM Mensaje a WHERE a.emisor =: emisor AND a.receptor =: receptor ORDER BY a.fecha")
	public List<Mensaje> buscarMensajeEmisorReceptor(@Param("emisor") Persona emisor,
			@Param("receptor") Persona receptor);

}