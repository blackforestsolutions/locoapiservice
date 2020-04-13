package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface SearchChCallService {
    ResponseEntity<String> getRequestAnswer(String url, HttpEntity<?> requestEntity);
}
