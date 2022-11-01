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
import tutorial.misionTIC.seguridad.modelos.Permiso;
import tutorial.misionTIC.seguridad.repositorios.RepositorioPermiso;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/permisos")
public class ControladorPermiso {

    @Autowired
    RepositorioPermiso miRepositorioPermiso;

    @GetMapping
    public List<Permiso> buscarTodosLosPermisos() {
        log.info("Buscando todos los permisos....");
        return miRepositorioPermiso.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permiso crearPermiso(@RequestBody Permiso infoPermiso) {
        log.info("Creando el permiso.....");
        return miRepositorioPermiso.save(infoPermiso);
    }

}
