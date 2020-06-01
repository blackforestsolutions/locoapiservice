package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;

import java.text.ParseException;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getRMVTokenAndUrl;
import static org.assertj.core.api.Assertions.assertThat;

class RMVHttpCallBuilderServiceTest {

    private final RMVHttpCallBuilderService classUnderTest = new RMVHttpCallBuilderServiceImpl();

    @Test
    void test_buildLocationPathWith_locationPath_locationString_authorization_language_returns_correct_path() throws ParseException {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("", "");

        String result = classUnderTest.buildLocationStringPathWith(testData, "Lorch-Lorchhausen Bahnhof");

        assertThat(result).isEqualTo("/hapi/location.name?input=Lorch-Lorchhausen Bahnhof&accessId=1a4fbca8-ce2b-40fc-a1ed-333bcf5aed6e&lang=de");
    }

    @Test
    void test_buildLocationCoordinatesPathWith_coordinatesPath_authorization_language_radius_outputFormat_and_coordinates_return_correct_path() throws ParseException {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("", "");

        String result = classUnderTest.buildLocationCoordinatesPathWith(testData, testData.getDepartureCoordinates());

        assertThat(result).isEqualTo("/hapi/location.nearbystops?accessId=1a4fbca8-ce2b-40fc-a1ed-333bcf5aed6e&originCoordLat=50.052278&originCoordLong=8.571331&r=1000&type=SP&lang=de");
    }

    @Test
    void test_buildTripPathWith_journeyDetailsPath_authorization_departure_arrival_timeIsDeparture_resultLengthBeforeAndAfterDate_allowIntermediateStops_return_correct_path() throws ParseException {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("003011037", "003000010");

        String result = classUnderTest.buildTripPathWith(testData);

        assertThat(result).isEqualTo("/hapi/trip?accessId=1a4fbca8-ce2b-40fc-a1ed-333bcf5aed6e&originId=003011037&destId=003000010&date=2020-05-04&time=08:00&searchForArrival=0&numB=2&numF=4&passlist=1");
    }

    @Test
    void test_buildHttpEntityForRMV_with_authorization_returns_correct_header_and_entity() throws ParseException {
        ApiTokenAndUrlInformation testData = getRMVTokenAndUrl("", "");

        HttpEntity<String> result = classUnderTest.buildHttpEntityForRMV(testData);

        assertThat(result.hasBody()).isFalse();
        assertThat(result.getHeaders().getFirst("accessId")).isEqualTo(testData.getAuthorization());
        assertThat(result.getHeaders().values().size()).isEqualTo(1);
    }


}
