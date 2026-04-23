package io.squid.CyStation.repository;

import io.squid.CyStation.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< Updated upstream

public interface ZoneRepository extends JpaRepository<Zone, Long> {

}
=======
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
>>>>>>> Stashed changes
