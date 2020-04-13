package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface RMVCallService {
    ResponseEntity<String> getStationId(String url, HttpEntity<?> requestEntity);

    ResponseEntity<String> getTrip(String url, HttpEntity<?> requestEntity);
}
