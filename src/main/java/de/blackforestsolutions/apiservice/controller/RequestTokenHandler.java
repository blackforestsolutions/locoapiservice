package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import reactor.core.publisher.Mono;

public final class RequestTokenHandler {

    public static ApiTokenAndUrlInformation getRequestApiTokenWith(ApiTokenAndUrlInformation request, ApiTokenAndUrlInformation configuredRequestData) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builderCopy = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(configuredRequestData);
        builderCopy.setArrival(request.getArrival());
        builderCopy.setDeparture(request.getDeparture());
        builderCopy.setArrivalDate(request.getArrivalDate());
        builderCopy.setDepartureDate(request.getDepartureDate());
        builderCopy.setDepartureCoordinates(request.getDepartureCoordinates());
        builderCopy.setArrivalCoordinates(request.getArrivalCoordinates());
        return builderCopy.build();
    }

    public static Mono<ApiTokenAndUrlInformation> mapStringToRequestApiToken(String request) {
        LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
        try {
            return Mono.just(locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
