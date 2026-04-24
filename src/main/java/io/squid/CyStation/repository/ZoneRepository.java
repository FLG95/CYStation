package io.squid.CyStation.repository;

import io.squid.CyStation.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    public Zone findZoneByName(String name);
}