package possacode.gestionvehiculeapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehicule {

    @UuidGenerator
    @Id
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    private String marque;
    private String modele;
    private String couleur;
    private String photo;


}
