package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface BahnCallService {
    ResponseEntity<String> getRequestAnswer(String url, HttpEntity<?> requestEntity);
}
