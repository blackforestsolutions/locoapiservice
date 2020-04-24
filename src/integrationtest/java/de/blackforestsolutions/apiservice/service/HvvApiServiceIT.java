package de.blackforestsolutions.apiservice.service;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.HvvCallService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvJourneyHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvStationListHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvTravelPointHttpCallBuilderService;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@SpringBootTest
@AutoConfigureMockMvc
class HvvApiServiceIT {

    @Autowired
    HvvCallService hvvCallService;

    @Autowired
    HvvStationListHttpCallBuilderService stationListCallBuilder;

    @Autowired
    HvvTravelPointHttpCallBuilderService travelPointCallBuilder;

    @Autowired
    HvvJourneyHttpCallBuilderService journeyHttpCallBuilder;

    @Test
    void test_getStationList() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvStationListTokenAndUrl();

        ResponseEntity<String> result = hvvCallService.postStationList(buildUrlWith(testData).toString(), stationListCallBuilder.buildStationListHttpEntityForHvv(testData));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }

    @Test
    void test_getTravelPoint() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvTravelpointTokenAndUrl();

        ResponseEntity<String> result = hvvCallService.postTravelPoint(buildUrlWith(testData).toString(), travelPointCallBuilder.buildTravelPointHttpEntityForHvv(testData, testData.getDeparture()));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }

    @Test
    void test_getJourney() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getHvvJourneyTokenAndUrl();
        HvvStation start = TravelPointObjectMother.getRosenhofHvvStation();
        HvvStation destination = TravelPointObjectMother.getStadthausbrueckeHvvStation();

        ResponseEntity<String> result = hvvCallService.postJourney(buildUrlWith(testData).toString(), journeyHttpCallBuilder.buildJourneyHttpEntityForHvv(testData, start, destination));

        Assertions.assertThat(HttpStatus.OK).isEqualTo(result.getStatusCode());
    }


}
