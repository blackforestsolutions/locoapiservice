package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface AirportsFinderCallService {
    ResponseEntity<String> getNearestAirports(String url, HttpEntity<?> requestEntity);
}
