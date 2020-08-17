package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilderServiceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.HvvStation;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getHvvTokenAndUrl;
import static de.blackforestsolutions.apiservice.objectmothers.HttpBodyObjectMother.getHvvJourneyBody;
import static de.blackforestsolutions.apiservice.objectmothers.HttpBodyObjectMother.getHvvTravelPointBody;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getRosenhofHvvStation;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getStadthausbrueckeHvvStation;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.convertObjectToJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HvvHttpCallBuilderServiceTest {

    private final HvvHttpCallBuilderService classUnderTest = new HvvHttpCallBuilderServiceImpl();

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_start_destination_and_httpBody_returns_correct_header() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        HvvStation start = getRosenhofHvvStation();
        HvvStation destination = getStadthausbrueckeHvvStation();

        HttpHeaders result = classUnderTest.buildJourneyHttpEntityForHvv(testData, start, destination).getHeaders();

        assertThat(result.getFirst(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(result.getFirst(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_TYPE)).isEqualTo("HmacSHA1");
        assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_USER)).isEqualTo("janhendrikhausner");
        assertThat(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_SIGNATURE)).isEqualTo("0fsMnAtZjmIUIk20j1F3RhI/B1Q=");
        assertThat(result.getFirst(AdditionalHttpConfiguration.X_TRACE_ID)).isNotNull();
        assertThat(result.values().size()).isEqualTo(6);
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_returns_correct_http_body() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        String testBody = convertObjectToJsonString(getHvvJourneyBody());
        HvvStation start = getRosenhofHvvStation();
        HvvStation destination = getStadthausbrueckeHvvStation();

        //noinspection rawtypes (justification: not known until runtime)
        HttpEntity result = classUnderTest.buildJourneyHttpEntityForHvv(testData, start, destination);

        assertThat(result.getBody()).isEqualTo(testBody);
    }

    @Test
    void test_buildJourneyPathWith_pathvariable_journeyPathVariable_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();

        String result = classUnderTest.buildJourneyPathWith(testData);

        assertThat(result).isEqualTo("/gti/public/getRoute");
    }

    @Test
    void test_buildJourneyPathWith_pathvariable_as_null_journeyPathVariable_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setPathVariable(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyPathWith_pathvariable_journeyPathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setJourneyPathVariable(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyPathWith(testData.build()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_allowTariffDetails_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAllowTariffDetails(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_language_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setLanguage(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_apiVersion_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setApiVersion(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_authentificationType_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAuthentificationType(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_authentificationUser_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAuthentificationUser(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_authentificationPassword_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAuthentificationPassword(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_departureDate_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setDepartureDate(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_timeIsDeparture_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setTimeIsDeparture(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_allowTarifDetails_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAllowTariffDetails(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_resultLengthBeforeDepartureTime_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setResultLengthBeforeDepartureTime(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_resultLengthAfterDepartureTime_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setResultLengthAfterDepartureTime(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_allowReducedPrice_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAllowReducedPrice(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_allowIntermediateStops_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAllowIntermediateStops(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_tariff_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setTariff(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildJourneyHttpEntityForHvv_with_apiToken_and_hvvReturnContSearchData_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setHvvReturnContSearchData(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildJourneyHttpEntityForHvv(testData.build(), getRosenhofHvvStation(), getStadthausbrueckeHvvStation()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_with_apiToken_andHttpBody_returns_correct_header_and_body() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();

        HttpHeaders result = classUnderTest.buildTravelPointHttpEntityForHvv(testData, testData.getDeparture()).getHeaders();

        assertEquals(result.getFirst(HttpHeaders.ACCEPT), MediaType.APPLICATION_JSON_VALUE);
        assertEquals(result.getFirst(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON_VALUE);
        assertEquals(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_TYPE), "HmacSHA1");
        assertEquals(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_USER), "janhendrikhausner");
        assertEquals(result.getFirst(AdditionalHttpConfiguration.GEO_FEX_AUTH_SIGNATURE), "OZ+Z1ogdVX5jSiltjtxMG8NQXXE=");
        assertNotNull(result.getFirst(AdditionalHttpConfiguration.X_TRACE_ID));
        assertEquals(6, result.values().size());
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_with_jsonBody_is_equal_to_http_body() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();
        String testBody = convertObjectToJsonString(getHvvTravelPointBody());

        HttpEntity<String> result = classUnderTest.buildTravelPointHttpEntityForHvv(testData, testData.getDeparture());

        assertThat(result.getBody()).isEqualTo(testBody);
    }

    @Test
    void test_buildTravelPointPathWith_pathvariable_travelPointPathVariable_returns_valid_path_string() {
        ApiTokenAndUrlInformation testData = getHvvTokenAndUrl();

        String result = classUnderTest.buildTravelPointPathWith(testData);

        assertThat(result).isEqualTo("/gti/public/checkName");
    }

    @Test
    void test_buildTravelPointPathWith_apiToken_and_pathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setPathVariable(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointPathWith(testData.build()));
    }

    @Test
    void test_buildTravelPointPathWith_apiToken_and_travelPointPathVariable_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setTravelPointPathVariable(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointPathWith(testData.build()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_hvvAllowTypeSwitch_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setHvvAllowTypeSwitch(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_resultLength_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setResultLength(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_distanceFromTravelPoint_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setDistanceFromTravelPoint(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_allowTariffDetails_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAllowTariffDetails(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_language_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setLanguage(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_apiVersion_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setApiVersion(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_authentificationType_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAuthentificationType(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_authentificationUser_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAuthentificationUser(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

    @Test
    void test_buildTravelPointHttpEntityForHvv_apiToken_and_authentificationPassword_as_null_throws_NullPointerException() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder testData = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getHvvTokenAndUrl());
        testData.setAuthentificationPassword(null);

        assertThrows(NullPointerException.class, () -> classUnderTest.buildTravelPointHttpEntityForHvv(testData.build(), testData.getDeparture()));
    }

}
