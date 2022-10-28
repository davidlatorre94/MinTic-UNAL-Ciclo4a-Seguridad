package tutorial.misionTIC.seguridad.controladores;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tutorial.misionTIC.seguridad.modelos.Usuario;
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

    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
