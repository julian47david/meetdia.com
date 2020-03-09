package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.entidades.Multimedia;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.edu.egg.meetdia.com.enumeraciones.Tipo;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.MultimediaRepositorio;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class MultimediaServicio {

    @Autowired
    private MultimediaRepositorio multimediaRepositorio;

    @Transactional
    public Multimedia guardar(MultipartFile archivo) throws ErrorServicio {

        if (archivo != null) {
            try {
                System.out.println("Guardando archivo...");
                Multimedia multimedia = new Multimedia();
                multimedia.setMime(archivo.getContentType());
                multimedia.setNombre(archivo.getName());
                multimedia.setContenidoMultimedia(archivo.getBytes());
                multimedia.setTipo(tipoDeArchivo(archivo));
                multimedia.setEncoded64(Base64.getEncoder().encodeToString(archivo.getBytes()));
                multimediaRepositorio.save(multimedia);
                return multimedia;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
            System.out.println("No hubo multimedia");
            return null;
    }
	
	

    private Tipo tipoDeArchivo(MultipartFile archivo) {

        String[] extensionfrag = archivo.getOriginalFilename().split("\\.");
        String extension = extensionfrag[extensionfrag.length - 1];

        if (extension.equals("mp4") || extension.equals("avi") || extension.equals("flv") || extension.equals("mkv") || extension.equals("mov")) {

            Tipo tipo = Tipo.VIDEO;
            return tipo;

        } else if (extension.equals("mp3") || extension.equals("wav") || extension.equals("wma") || extension.equals("vorbis") || extension.equals("flac")) {

            Tipo tipo = Tipo.AUDIO;
            return tipo;

        } else if (extension.equals("jpg") || extension.equals("gif") || extension.equals("png") || extension.equals("png") || extension.equals("jpeg")) {

            Tipo tipo = Tipo.IMAGEN;
            return tipo;
        } else {

            return null;
        }

    }
}
