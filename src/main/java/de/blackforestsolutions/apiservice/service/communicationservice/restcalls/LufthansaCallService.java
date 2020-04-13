package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface LufthansaCallService {
    ResponseEntity<String> getFlights(String url, HttpEntity<?> requestEntity);
}
