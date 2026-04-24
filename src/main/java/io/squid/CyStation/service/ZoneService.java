package io.squid.CyStation.service;

import io.squid.CyStation.model.Zone;
import io.squid.CyStation.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService( ZoneRepository zoneRepository){
        this.zoneRepository = zoneRepository;
    }



    public void save( Zone zone){

        zoneRepository.save(zone);
    }
}
