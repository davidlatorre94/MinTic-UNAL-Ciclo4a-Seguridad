package tutorial.misionTIC.seguridad.modelos;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document()
public class Usuario {

    @Id
    private String _id;

    private String seudonimo;
    private String correo;
    private String contrasena;

    @DBRef
    private Rol rol;

    public Usuario(String seudonimo, String correo, String contrasena) {
        this.seudonimo = seudonimo;
        this.correo = correo;
        this.contrasena = contrasena;
    }

}
