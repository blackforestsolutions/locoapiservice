package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface FlinksterCallService {
    ResponseEntity<String> getRides(String url, HttpEntity<?> requestEntity);
}
