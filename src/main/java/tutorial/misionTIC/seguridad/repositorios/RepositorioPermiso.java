package tutorial.misionTIC.seguridad.repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;
import tutorial.misionTIC.seguridad.modelos.Permiso;

public interface RepositorioPermiso extends MongoRepository<Permiso, String> {

}
