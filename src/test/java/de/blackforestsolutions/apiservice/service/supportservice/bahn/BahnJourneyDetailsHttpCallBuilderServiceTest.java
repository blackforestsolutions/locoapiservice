package de.blackforestsolutions.apiservice.service.supportservice.bahn;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderSeviceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.UncheckedIOException;
import java.net.URL;

@Service
class BahnJourneyDetailsHttpCallBuilderServiceTest {

    private final BahnHttpCallBuilderService classUnderTest = new BahnHttpCallBuilderSeviceImpl();

    @Test
    void test_buildHttpHeadersForBahnJourneyDetailsWithBahnArrivalAndTokenUrlInfo_return_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        HttpHeaders result = classUnderTest.buildHttpHeadersForBahnWith(apiTokenAndUrlInformation);
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey())).contains(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_apiVersion_journeyDetailsPath_detailsID_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();

        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPath()).contains(testData.getApiVersion());
        Assertions.assertThat(result.getPath()).contains(testData.getGermanRailJourneyDeatilsPath());
        Assertions.assertThat(result.getPath().replaceAll("%25", "%")).contains(testData.getJourneyDetailsId());
    }

    @Test
    void test_buildUrlWith_protocolte_host_port_path_apiVersion_journeyDetailsPath_detailsID_returns_correctUrl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        testData = builder.build();


        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPath()).contains(testData.getApiVersion());
        Assertions.assertThat(result.getPath()).contains(testData.getGermanRailJourneyDeatilsPath());
        Assertions.assertThat(result.getPath().replaceAll("%25", "%")).contains(testData.getJourneyDetailsId());
    }

    @Test
    void test_buildUrlWith_protocolte_host_port_path_apiVersion_journeyDetailsPath_detailsID_throws_MalformedException() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("falseProtcol");
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildBahnUrlWith(finalTestData));
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_apiVersion_journeyDetailsPath_detailsID() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPort(-1);
        testData = builder.build();


        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPath()).contains(testData.getApiVersion());
        Assertions.assertThat(result.getPath()).contains(testData.getGermanRailJourneyDeatilsPath());
        Assertions.assertThat(result.getPath().replaceAll("%25", "%")).contains(testData.getJourneyDetailsId());
    }

    @Test
    void test_buildUrlWith_protocol_host_port_path_apiVersion_journeyDetailsPath_detailsID_return_correcturl() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("https");
        builder.setPort(-1);
        testData = builder.build();


        URL result = classUnderTest.buildBahnUrlWith(testData);

        Assertions.assertThat(result.getProtocol()).isEqualToIgnoringCase(testData.getProtocol());
        Assertions.assertThat(result.getPort()).isEqualTo(testData.getPort());
        Assertions.assertThat(result.getHost()).isEqualToIgnoringCase(testData.getHost());
        Assertions.assertThat(result.getPath()).contains(testData.getApiVersion());
        Assertions.assertThat(result.getPath()).contains(testData.getGermanRailJourneyDeatilsPath());
        Assertions.assertThat(result.getPath().replaceAll("%25", "%")).contains(testData.getJourneyDetailsId());
    }

    @Test
    void test_buildUrlWith_protocolte_host_port_path_apiVersion_journeyDetailsPath_detailsID_throws_MalformedException2() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setProtocol("falseProtcol");
        builder.setPort(-1);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(UncheckedIOException.class, () -> classUnderTest.buildBahnUrlWith(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_departureJounrneyDetailsVariable_detailsId() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        String result = classUnderTest.buildBahnJourneyDetailsPath(testData);
        Assertions.assertThat(result).isEqualTo("/fahrplan-plus/v1/journeyDetails/715770%252F254084%252F898562%252F210691%252F80%253fstation_evaId%253D8000312");
    }

    @Test
    void test_buildPathWith_pathvariable_as_null_apiVersion_departureJounrneyDetailsVariable_detailsId() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setPathVariable(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnJourneyDetailsPath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_as_null_departureJounrneyDetailsVariable_detailsId() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setApiVersion(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnJourneyDetailsPath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_departureJounrneyDetailsVariable_as_null_detailsId() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setGermanRailJourneyDeatilsPath(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnJourneyDetailsPath(finalTestData));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_departureJounrneyDetailsVariable_detailsId_as_null() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getBahnJourneyDetailsTokenAndUrl();
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder = builder.buildFrom(testData);
        builder.setJourneyDetailsId(null);
        testData = builder.build();

        ApiTokenAndUrlInformation finalTestData = testData;
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnJourneyDetailsPath(finalTestData));
    }
}






