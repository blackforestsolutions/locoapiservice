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
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("direct-connections")
public class DirectConnectionController {

    private final LocoJsonMapper locoJsonMapper = new LocoJsonMapper();
    private final DBApiService dbApiService;
    @Resource(name = "dbApiTokenAndUrlInformation")
    private ApiTokenAndUrlInformation dbApiTokenAndUrlInformation;
    private final ExceptionHandlerService exceptionHandlerService;

    @Autowired
    public DirectConnectionController(DBApiService dbApiService, ExceptionHandlerService exceptionHandlerService) {
        this.dbApiService = dbApiService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @RequestMapping("/get")
    public Map<UUID, Journey> retrieveTrainJourneys(@RequestBody String request) throws IOException {
        ApiTokenAndUrlInformation requestInformation = locoJsonMapper.mapJsonToApiTokenAndUrlInformation(request);
        return this.exceptionHandlerService.handleExceptions(Arrays.asList(
                dbApiService.getJourneysForRouteWith(getDbApiTokenAndUrlInformation(requestInformation))
        ));
    }

    private ApiTokenAndUrlInformation getDbApiTokenAndUrlInformation(ApiTokenAndUrlInformation request) {
        return RequestTokenHandler.getRequestApiTokenWith(request, dbApiTokenAndUrlInformation);
    }

    @VisibleForTesting
    void setDbApiTokenAndUrlInformation(ApiTokenAndUrlInformation dbApiTokenAndUrlInformation) {
        this.dbApiTokenAndUrlInformation = dbApiTokenAndUrlInformation;
    }
}
