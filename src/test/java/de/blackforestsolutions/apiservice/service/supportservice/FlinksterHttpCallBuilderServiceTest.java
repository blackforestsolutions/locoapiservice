package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.apiservice.objectmothers.ApiTokenAndUrlInformationObjectMother;
import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

class FlinksterHttpCallBuilderServiceTest {

    private final FlinksterHttpCallBuilderService classUnderTest = new FlinksterHttpCallBuilderServiceImpl();

    @Test
    void test_buildFlinksterPathWith_Api_Token_returns_Correct_Path() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getFlinksterJourneyDetailsTokenAndUrl();

        String result = classUnderTest.buildFlinksterPathWith(testData);

        Assertions.assertEquals(result, "/flinkster-api-ng/v1/bookingproposals?lat=50.776518&lon=6.97679&radius=10000&providernetwork=1&begin=2020-05-07T12%3A02%3A00%2B02%3A00&expand=rentalobject%2Carea%2Cprice");
    }

    @Test
    void test_buildHttpEntityForFlinkster_with_Api_Token_returns_Correct_Bearer_Token_AND_AuthorizationKey() {
        ApiTokenAndUrlInformation testData = ApiTokenAndUrlInformationObjectMother.getFlinksterJourneyDetailsTokenAndUrl();

        HttpEntity result = classUnderTest.buildHttpEntityForFlinkster(testData);

        Assertions.assertEquals("Bearer 4d3c7b35a42c7ecadeb41b905e0007f8", result.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
        Assertions.assertTrue(result.getHeaders().containsKey(HttpHeaders.AUTHORIZATION));
    }

}
