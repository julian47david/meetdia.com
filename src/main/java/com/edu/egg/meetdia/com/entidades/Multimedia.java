package com.edu.egg.meetdia.com.entidades;

import javax.persistence.Basic;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

import com.edu.egg.meetdia.com.enumeraciones.*;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Multimedia {
	// ESTA CLASE ES PARA LOS ARCHIVOS MULTIMEDIA QUE VAN EN LOS POSTS

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String nombre;
	private String mime;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] contenidoMultimedia;
	@Enumerated(EnumType.STRING)
	private Tipo tipo;
        @Lob
	@Basic(fetch = FetchType.LAZY)
        private String encoded64;

    public String getEncoded64() {
        return encoded64;
    }

    public void setEncoded64(String encoded64) {
        this.encoded64 = encoded64;
    }

	public byte[] getContenidoMultimedia() {
		return contenidoMultimedia;
	}

	public void setContenidoMultimedia(byte[] contenidoMultimedia) {
		this.contenidoMultimedia = contenidoMultimedia;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}
}
