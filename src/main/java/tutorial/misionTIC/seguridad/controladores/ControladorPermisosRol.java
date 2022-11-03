package tutorial.misionTIC.seguridad.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tutorial.misionTIC.seguridad.modelos.Permiso;
import tutorial.misionTIC.seguridad.modelos.PermisosRol;
import tutorial.misionTIC.seguridad.modelos.Rol;
import tutorial.misionTIC.seguridad.repositorios.RepositorioPermiso;
import tutorial.misionTIC.seguridad.repositorios.RepositorioPermisosRol;
import tutorial.misionTIC.seguridad.repositorios.RepositorioRol;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permisos-rol")
public class ControladorPermisosRol {

    @Autowired
    private RepositorioPermisosRol miRepositorioPermisosRol;

    @Autowired
    private RepositorioRol miRepositorioRol;

    @Autowired
    private RepositorioPermiso miRepositorioPermiso;

    @PostMapping("/rol/{idRol}/permiso/{idPermiso}")
    public PermisosRol crearPermisosRol(@PathVariable String idRol, @PathVariable String idPermiso) {
        Rol rol = miRepositorioRol
                .findById(idRol)
                .orElseThrow(RuntimeException::new);

        Permiso permiso = miRepositorioPermiso
                .findById(idPermiso)
                .orElseThrow(RuntimeException::new);

        PermisosRol permisosRol = new PermisosRol(rol, permiso);

        return miRepositorioPermisosRol.save(permisosRol);
    }

    @GetMapping
    public List<PermisosRol> buscarTodosLosPermisosRol() {
        return miRepositorioPermisosRol.findAll();
    }

}
