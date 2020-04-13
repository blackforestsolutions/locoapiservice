package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface BritishAirwaysCallService {
    ResponseEntity<String> getFlights(String url, HttpEntity<?> requestEntity);
}
