package com.edu.egg.meetdia.com.repositorios;

import com.edu.egg.meetdia.com.entidades.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MultimediaRepositorio extends JpaRepository<Multimedia, String> {

	@Query("SELECT a FROM Multimedia a WHERE a.id= :id")
	public Multimedia buscarPersona(@Param("id") String id);

	@Query("SELECT a FROM Multimedia a WHERE a.nombre= :nombre")
	public Multimedia buscarNombre(@Param("nombre") String nombre);

	@Query("SELECT a FROM Multimedia a WHERE a.mime= :mime")
	public Multimedia buscarMime(@Param("mime") String mime);

//	@Query("SELECT a FROM Multimedia a WHERE a.contenidoMultimedia= :contenidoMultimedia")
//	public Multimedia buscarcontenidoMultimedia(@Param("ContenidoMultimedia") Byte contenidoMultimedia);

}