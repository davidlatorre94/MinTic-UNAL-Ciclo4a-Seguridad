package tutorial.misionTIC.seguridad.repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tutorial.misionTIC.seguridad.modelos.Usuario;

public interface RepositorioUsuario extends MongoRepository<Usuario, String> {

    @Query("{'correo': ?0}")
    public Usuario findByEmail(String correo);

}
