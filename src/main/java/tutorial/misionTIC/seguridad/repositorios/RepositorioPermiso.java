package tutorial.misionTIC.seguridad.repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tutorial.misionTIC.seguridad.modelos.Permiso;

public interface RepositorioPermiso extends MongoRepository<Permiso, String> {

    @Query("{'url': ?0, 'metodo': ?1}")
    public Permiso findByUrlAndMethod(String url, String metodo);

}
