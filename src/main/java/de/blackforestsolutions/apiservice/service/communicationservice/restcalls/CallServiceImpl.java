package de.blackforestsolutions.apiservice.service.communicationservice.restcalls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CallServiceImpl implements CallService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Autowired
    public CallServiceImpl(RestTemplateBuilder restTemplateBuilder, WebClient webClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.webClient = webClient;
    }

    @Override
    public ResponseEntity<String> getOld(String url, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
    }

    @Override
    public ResponseEntity<String> post(String url, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

    @Override
    public Mono<ResponseEntity<String>> get(String url, HttpEntity<?> request) {
        return webClient
                .get()
                .uri(url)
                .headers(httpHeaders -> request.getHeaders().forEach(httpHeaders::addAll))
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(String.class));
    }

    @Override
    public Mono<ResponseEntity<String>> post(String url, HttpEntity<?> request, MediaType typeOfRequestBody) {
        return Optional.ofNullable(request.getBody())
                .map(body -> webClient
                        .post()
                        .uri(url)
                        .headers(httpHeaders -> request.getHeaders().forEach(httpHeaders::addAll))
                        .contentType(typeOfRequestBody)
                        .body(BodyInserters.fromValue(body))
                        .exchange()
                        .flatMap(clientResponse -> clientResponse.toEntity(String.class))
                )
                .orElseGet(() -> Mono.error(new Exception("No Body available for post request!")));

    }
}
