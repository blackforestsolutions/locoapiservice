package de.blackforestsolutions.apiservice.service.supportservice.bahn;

import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.BahnHttpCallBuilderSeviceImpl;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import static de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother.getBahnTokenAndUrl;

@Service
class BahnJourneyDetailsHttpCallBuilderServiceTest {

    private final BahnHttpCallBuilderService classUnderTest = new BahnHttpCallBuilderSeviceImpl();

    @Test
    void test_buildHttpHeadersForBahnJourneyDetailsWithBahnArrivalAndTokenUrlInfo_return_correct_header() {
        ApiTokenAndUrlInformation apiTokenAndUrlInformation = getBahnTokenAndUrl();
        HttpHeaders result = classUnderTest.buildHttpHeadersForBahnWith(apiTokenAndUrlInformation);
        Assertions.assertThat(result.get(apiTokenAndUrlInformation.getAuthorizationKey())).contains(apiTokenAndUrlInformation.getAuthorization());
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_departureJounrneyDetailsVariable_detailsId() {
        ApiTokenAndUrlInformation testData = getBahnTokenAndUrl();

        String result = classUnderTest.buildBahnJourneyDetailsPath(testData);

        Assertions.assertThat(result).isEqualTo("/fahrplan-plus/v1/journeyDetails/715770%2F254084%2F898562%2F210691%2F80%3fstation_evaId%3D8000312");
    }

    @Test
    void test_buildPathWith_pathvariable_as_null_apiVersion_departureJounrneyDetailsVariable_detailsId() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setPathVariable(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnJourneyDetailsPath(builder.build()));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_as_null_departureJounrneyDetailsVariable_detailsId() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setApiVersion(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnJourneyDetailsPath(builder.build()));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_departureJounrneyDetailsVariable_as_null_detailsId() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setGermanRailJourneyDeatilsPath(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnJourneyDetailsPath(builder.build()));
    }

    @Test
    void test_buildPathWith_pathvariable_apiVersion_departureJounrneyDetailsVariable_detailsId_as_null() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder(getBahnTokenAndUrl());
        builder.setJourneyDetailsId(null);

        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.buildBahnJourneyDetailsPath(builder.build()));
    }
}






