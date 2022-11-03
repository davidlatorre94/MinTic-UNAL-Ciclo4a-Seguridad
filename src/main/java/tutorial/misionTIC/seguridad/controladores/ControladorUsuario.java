package tutorial.misionTIC.seguridad.controladores;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tutorial.misionTIC.seguridad.modelos.MessageResponse;
import tutorial.misionTIC.seguridad.modelos.Rol;
import tutorial.misionTIC.seguridad.modelos.Usuario;
import tutorial.misionTIC.seguridad.repositorios.RepositorioRol;
import tutorial.misionTIC.seguridad.repositorios.RepositorioUsuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class ControladorUsuario {

    @Autowired
    RepositorioUsuario miRepositorioUsuario;

    @Autowired
    RepositorioRol miRepositorioRol;

    @GetMapping
    public List<Usuario> buscarTodosLosUsuarios() {
        log.info("Buscando todos los usuarios....");
        return miRepositorioUsuario.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario infoUsuario) {
        log.info("Creando un usuario.....");

        String contrasenaCifrada = convertirSHA256(infoUsuario.getContrasena());
        infoUsuario.setContrasena(contrasenaCifrada);
        return miRepositorioUsuario.save(infoUsuario);
    }

    @GetMapping("{idUsuario}")
    public Usuario buscarUsuario(@PathVariable String idUsuario) {
        return miRepositorioUsuario
                .findById(idUsuario)
                .orElse(new Usuario("", "", ""));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("{idUsuario}")
    public MessageResponse eliminarUsuario(@PathVariable String idUsuario) {
        miRepositorioUsuario.deleteById(idUsuario);

        MessageResponse respuesta = new MessageResponse();
        respuesta.setMessage("Usuario Eliminado");

        return respuesta;
    }

    @PutMapping("{idUsuario}")
    public Usuario modificarUsuario(@PathVariable String idUsuario, @RequestBody Usuario infoUsuario) {
        log.info("Modificando el usuario: {}", idUsuario);

        // 1. Buscar el usuario en BD
        Usuario usuarioActual = miRepositorioUsuario
                .findById(idUsuario)
                .orElse(null);

        log.info("Usuario encontrado en base de datos: {}", usuarioActual);

        // 2. Validar si existe el usuario en BD.
        if (usuarioActual != null) {
            // 3. Modificar los atributos del usuario.
            usuarioActual.setSeudonimo(infoUsuario.getSeudonimo());
            usuarioActual.setCorreo(infoUsuario.getCorreo());
            usuarioActual.setContrasena(convertirSHA256(infoUsuario.getContrasena()));

            //4. Actualizar el usuario en base de datos y retornarlo.
            return miRepositorioUsuario.save(usuarioActual);
        } else {
            // 3. No se encuentra el usuario en BD, se retorna null.
            return null;
        }
    }

    @PutMapping("{idUsuario}/rol/{idRol}")
    public Usuario asignarRolAlUsuario(@PathVariable String idUsuario, @PathVariable String idRol) {
        Usuario usuario = miRepositorioUsuario
                .findById(idUsuario)
                .orElse(null);

        Rol rol = miRepositorioRol
                .findById(idRol)
                .orElse(null);

        if (usuario != null && rol != null) {
            usuario.setRol(rol);
            return miRepositorioUsuario.save(usuario);
        } else {
            return null;
        }
    }

    @PostMapping("validar-usuario")
    public Usuario validarUsuario(@RequestBody Usuario infoUsuario) {
        log.info("Validando el usuario, request body: {}", infoUsuario);

        //Busco el usuario en base de datos dado el email
        Usuario usuarioActual = miRepositorioUsuario.findByEmail(infoUsuario.getCorreo());

        //Comparar las contraseñas que llegan desde postman y la que está en BD
        String contrasenaUsuario = convertirSHA256(infoUsuario.getContrasena());
        String contrasenaBaseDatos = usuarioActual.getContrasena();

        if (contrasenaUsuario.equals(contrasenaBaseDatos)) {
            return usuarioActual;
        } else {
            return null;
        }
    }


    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
