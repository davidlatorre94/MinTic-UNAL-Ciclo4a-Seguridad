package tutorial.misionTIC.seguridad.controladores;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tutorial.misionTIC.seguridad.modelos.Permiso;
import tutorial.misionTIC.seguridad.modelos.PermisosRol;
import tutorial.misionTIC.seguridad.modelos.Rol;
import tutorial.misionTIC.seguridad.repositorios.RepositorioPermiso;
import tutorial.misionTIC.seguridad.repositorios.RepositorioPermisosRol;
import tutorial.misionTIC.seguridad.repositorios.RepositorioRol;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
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


    @GetMapping("validar-permiso/rol/{idRol}")
    public PermisosRol validarPermisosDelRol(@PathVariable String idRol, @RequestBody Permiso infoPermiso, HttpServletResponse response) throws IOException {

        //Buscar en base de datos el rol y permiso
        Rol rolActual = miRepositorioRol.findById(idRol).orElse(null);
        Permiso permisoActual = miRepositorioPermiso.findByUrlAndMethod(infoPermiso.getUrl(), infoPermiso.getMetodo());

        //Validar si existe el rol y el permiso en base de datos
        if (rolActual != null && permisoActual != null) {

            String idRolActual = rolActual.get_id();
            String idPermisoActual = permisoActual.get_id();
            log.info("idRolActual: {}, idPermisoActual: {}", idRolActual, idPermisoActual);

            //Buscar en la tabla PermisosRol si el rol tiene asociado el permiso.
            PermisosRol permisosRolActual = miRepositorioPermisosRol.findByRolAndPermissions(idRolActual, idPermisoActual);
            log.info("El permisosRol que encontró en BD fue: {}", permisosRolActual);

            if (permisosRolActual != null) {
                return permisosRolActual;
            } else {
                log.error("NO se encuentra el PermisosRol en base de datos");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        } else {
            log.error("NO se encuentra el rol o el permiso en base de datos");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

    }


}
