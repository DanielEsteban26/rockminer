package com.rockminer;

import com.rockminer.entity.Usuario;
import com.rockminer.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RockminerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RockminerApplication.class, args);
    }

    //@Bean
    //public CommandLineRunner crearUsuarioInicial(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
      //  return args -> {
        //    if (usuarioRepository.findByUsuario("admin").isEmpty()) {
          //      Usuario usuario = new Usuario();
            //    usuario.setNombre("Administrador");
              //  usuario.setUsuario("admin");
               // usuario.setClave(passwordEncoder.encode("admin123")); // clave encriptada
               // usuario.setRol("ADMIN");
               // usuario.setEstado(true);
               // usuarioRepository.save(usuario);
               // System.out.println("✅ Usuario 'admin' creado con clave encriptada: admin123");
           // }
       // };
    //}
}
