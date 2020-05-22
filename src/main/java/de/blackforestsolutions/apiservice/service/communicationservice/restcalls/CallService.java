package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface CallService {
    ResponseEntity<String> get(String url, HttpEntity<?> requestEntity);

    ResponseEntity<String> post(String url, HttpEntity<?> requestEntity);
}
