package de.blackforestsolutions.apiservice.service.supportservice;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidServiceImpl implements UuidService {

    public UUID createUUID() {
        return UUID.randomUUID();
    }
}
