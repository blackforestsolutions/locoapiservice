package de.blackforestsolutions.apiservice.controller;

import com.google.common.annotations.VisibleForTesting;
import de.blackforestsolutions.apiservice.service.communicationservice.DBApiService;
import de.blackforestsolutions.apiservice.service.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.util.LocoJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("train-rides")
public class NationalTrainRidesController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final DBApiService dbApiService;
    private final ExceptionHandlerService exceptionHandlerService;

    @Resource(name = "dbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation dbApiTokenAndUrlInformation;

    @Autowired
    public NationalTrainRidesController(DBApiService dbApiService, ExceptionHandlerService exceptionHandlerService) {
        this.exceptionHandlerService = exceptionHandlerService;
        this.dbApiService = dbApiService;
    }

    @RequestMapping("/get")
    public Map<UUID, Journey> retrieveTrainJourneys(@RequestBody String request) throws IOException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return this.exceptionHandlerService.handleExceptions(Collections.singletonList(
                dbApiService.getJourneysForRouteWith(getDbApiTokenAndUrlInformation(requestInformation))
        ));
    }

    @VisibleForTesting
    void setDbApiTokenAndUrlInformation(ApiTokenAndUrlInformation dbApiTokenAndUrlInformation) {
        this.dbApiTokenAndUrlInformation = dbApiTokenAndUrlInformation;
    }

    private ApiTokenAndUrlInformation getDbApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, dbApiTokenAndUrlInformation);
    }
}
