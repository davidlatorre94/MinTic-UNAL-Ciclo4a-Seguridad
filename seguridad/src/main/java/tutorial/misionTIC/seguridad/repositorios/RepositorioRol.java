package tutorial.misionTIC.seguridad.repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;
import tutorial.misionTIC.seguridad.modelos.Rol;

public interface RepositorioRol extends MongoRepository<Rol, String> {

}
