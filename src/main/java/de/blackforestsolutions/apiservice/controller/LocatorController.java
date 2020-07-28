package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.OSMApiService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("locate")
public class LocatorController {

    private final OSMApiService osmApiService;
    private final ExceptionHandlerService exceptionHandlerService;

    @Resource(name = "osmApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation osmApiTokenAndUrlInformation;

    @Autowired
    public LocatorController(OSMApiService osmApiService, ExceptionHandlerService exceptionHandlerService) {
        this.osmApiService = osmApiService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @RequestMapping("/get")
    public Coordinates retrieveLocatorJourneys(@RequestParam String address) {
        return exceptionHandlerService.handleExceptions(osmApiService.getCoordinatesFromTravelPointWith(osmApiTokenAndUrlInformation, address));
    }

    @VisibleForTesting
    void setOsmApiTokenAndUrlInformation(ApiTokenAndUrlInformation osmApiTokenAndUrlInformation) {
        this.osmApiTokenAndUrlInformation = osmApiTokenAndUrlInformation;
    }
}
