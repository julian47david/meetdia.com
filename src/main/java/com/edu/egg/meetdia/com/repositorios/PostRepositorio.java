package com.edu.egg.meetdia.com.repositorios;

import com.edu.egg.meetdia.com.entidades.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepositorio extends JpaRepository<Post, String>{
    @Query("SELECT p FROM Post p WHERE p.fecha_publicacion = (SELECT MAX(m.fecha_publicacion) FROM Post m)")
    public Post buscarUltimoPost();
    @Query("SELECT p FROM Post p ORDER BY p.fecha_publicacion DESC")
    public List<Post> mostrarUltimosPost();
}
