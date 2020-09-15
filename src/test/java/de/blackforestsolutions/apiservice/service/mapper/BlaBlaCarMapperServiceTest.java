package de.blackforestsolutions.apiservice.service.mapper;

import de.blackforestsolutions.apiservice.service.supportservice.UuidService;
import de.blackforestsolutions.datamodel.CallStatus;
import de.blackforestsolutions.datamodel.Journey;
import de.blackforestsolutions.datamodel.Status;
import de.blackforestsolutions.generatedcontent.blablacar.Rides;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getBerlinHbfToHamburgLandwehrJourney;
import static de.blackforestsolutions.apiservice.objectmothers.JourneyObjectMother.getFlughafenBerlinToHamburgHbfJourney;
import static de.blackforestsolutions.apiservice.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.apiservice.testutils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlaBlaCarMapperServiceTest {

    private final UuidService uuidService = mock(UuidService.class);

    private final BlaBlaCarMapperService classUnderTest = new BlaBlaCarMapperServiceImpl(uuidService);

    @BeforeEach
    void init() {
        when(uuidService.createUUID())
                .thenReturn(TEST_UUID_1)
                .thenReturn(TEST_UUID_2)
                .thenReturn(TEST_UUID_3)
                .thenReturn(TEST_UUID_4);
    }

    @Test
    void test_buildJourneysWith_rides_returns_correct_journeys() {
        String json = getResourceFileAsString("json/blaBlaCarJourney.json");
        Rides testData = retrieveJsonToPojo(json, Rides.class);

        Flux<CallStatus<Journey>> result = classUnderTest.buildJourneysWith(testData);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(toJson(journey.getCalledObject())).isEqualTo(toJson(getFlughafenBerlinToHamburgHbfJourney())))
                .assertNext(journey -> assertThat(toJson(journey.getCalledObject())).isEqualTo(toJson(getBerlinHbfToHamburgLandwehrJourney())))
                .verifyComplete();
    }

    @Test
    void test_buildJourneysWith_wrong_pojo_returns_one_problem_with_nullPointerException() {
        String json = getResourceFileAsString("json/blaBlaCarJourney.json");
        Rides testData = retrieveJsonToPojo(json, Rides.class);
        testData.getTrips().get(0).getWaypoints().get(0).setDateTime(null);

        Flux<CallStatus<Journey>> result = classUnderTest.buildJourneysWith(testData);

        StepVerifier.create(result)
                .assertNext(error -> {
                    assertThat(error.getStatus()).isEqualTo(Status.FAILED);
                    assertThat(error.getThrowable()).isInstanceOf(NullPointerException.class);
                })
                .assertNext(journey -> assertThat(journey.getCalledObject()).isInstanceOf(Journey.class))
                .verifyComplete();
    }

}
