package possacode.gestionvehiculeapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import possacode.gestionvehiculeapi.entity.Vehicule;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, String> {
}
