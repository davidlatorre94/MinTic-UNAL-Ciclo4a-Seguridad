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
import tutorial.misionTIC.seguridad.modelos.Rol;
import tutorial.misionTIC.seguridad.repositorios.RepositorioRol;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/roles")
public class ControladorRol {

    @Autowired
    RepositorioRol miRepositorioRol;

    @GetMapping
    public List<Rol> buscarTodosLosRoles() {
        log.info("Buscando todos los roles....");
        return miRepositorioRol.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Rol crearRol(@RequestBody Rol infoRol) {
        log.info("Creando un rol.....");
        return miRepositorioRol.save(infoRol);
    }


    @GetMapping("{idRol}")
    public Rol buscarRol(@PathVariable String idRol) {
        return miRepositorioRol
                .findById(idRol)
                .orElse(null);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("{idRol}")
    public void eliminarRol(@PathVariable String idRol) {
        miRepositorioRol.deleteById(idRol);
    }

    @PutMapping("{idRol}")
    public Rol modificarRol(@PathVariable String idRol, @RequestBody Rol infoRol) {
        log.info("Modificando el rol: {}", idRol);
        Rol rolActual = miRepositorioRol.findById(idRol).orElse(null);
        log.info("Rol encontrado en base de datos: {}", rolActual);

        if (rolActual != null) {
            rolActual.setNombre(infoRol.getNombre());
            rolActual.setDescripcion(infoRol.getDescripcion());
            return miRepositorioRol.save(rolActual);
        } else {
            return null;
        }
    }

}
