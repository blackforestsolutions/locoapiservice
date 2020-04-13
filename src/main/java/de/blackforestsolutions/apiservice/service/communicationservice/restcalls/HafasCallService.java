package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface HafasCallService {
    ResponseEntity<String> getStationId(String url, HttpEntity<?> requestEntity);

    ResponseEntity<String> getJourney(String url, HttpEntity<?> requestEntity);
}