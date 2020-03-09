package com.edu.egg.meetdia.com.controladores;

import com.edu.egg.meetdia.com.entidades.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.edu.egg.meetdia.com.enumeraciones.Categoria;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.PostRepositorio;
import com.edu.egg.meetdia.com.servicios.PostServicio;
import java.util.Base64;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/muro")
public class MuroControlador {

    @Autowired
    private PostServicio postServicio;
    
    @Autowired
    private PostRepositorio postRepositorio;
    
    @RequestMapping("")
    @PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @GetMapping
    public String muro(ModelMap modelo) {
        List<Post> listaPost = postRepositorio.mostrarUltimosPost();
        modelo.addAttribute("listapost", listaPost);
        return "muro.html";
    }
    
    @PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @GetMapping("/nuevo-post")
    public String editapost(ModelMap modelo){
        return "nuevo_post.html";
    }
    
    @PostMapping("/publicar")
    public String nuevoPost(ModelMap modelo, @RequestParam String titulo, @RequestParam String descripcion, @RequestParam String idPersona, MultipartFile archivo, String categoria, String busco_check) {
        boolean busco;
        if (busco_check  == null){
            busco = false;
        }else{
            busco = true;
        }
        try {
            postServicio.nuevoPost(titulo, descripcion, idPersona, archivo, categoria, busco);
            return "forward:/muro/";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("catOpt",categoria);
            modelo.put("titulo",titulo);
            modelo.put("descripcion",descripcion);
            modelo.put("check_value", busco);
            return "nuevo_post.html";
        }
    }
}
