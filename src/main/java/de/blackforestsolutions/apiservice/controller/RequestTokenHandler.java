package de.blackforestsolutions.apiservice.controller;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;

public final class RequestTokenHandler {

    public static ApiTokenAndUrlInformation getRequestApiTokenWith(ApiTokenAndUrlInformation request, ApiTokenAndUrlInformation configuredRequestData) {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builderCopy = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(configuredRequestData);
        builderCopy.setArrival(request.getArrival());
        builderCopy.setDeparture(request.getDeparture());
        builderCopy.setArrivalDate(request.getArrivalDate());
        builderCopy.setDepartureDate(request.getDepartureDate());
        return builderCopy.build();
    }
}
