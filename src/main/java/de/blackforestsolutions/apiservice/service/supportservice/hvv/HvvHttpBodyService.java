package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilder.combineBaseHttpBodyWithApiCallBody;

class HvvHttpBodyService {

    private static final long NORMAL_TARIFF = 1;
    private static final long CHILD_TARIFF = 2;

    static String buildTravelPointHttpBodyForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getHvvAllowTypeSwitch(), "hvvAllowTypeSwitch is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLength(), "resultLength is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getDistanceFromTravelPoint(), "distanceFromTravelPoint is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAllowTariffDetails(), "allowTariffDetails is not allowed to be null");
        HvvTravelPointBody hvvTravelPointBody = new HvvTravelPointBody();

        hvvTravelPointBody.setTheName(new HvvStation(station));
        hvvTravelPointBody.setAllowTypeSwitch(apiTokenAndUrlInformation.getHvvAllowTypeSwitch());
        hvvTravelPointBody.setMaxList(apiTokenAndUrlInformation.getResultLength());
        hvvTravelPointBody.setMaxDistance(apiTokenAndUrlInformation.getDistanceFromTravelPoint());
        hvvTravelPointBody.setTariffDetails(apiTokenAndUrlInformation.getAllowTariffDetails());

        return combineBaseHttpBodyWithApiCallBody(hvvTravelPointBody, apiTokenAndUrlInformation);

    }

    static String buildJourneyHttpBodyForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, HvvStation start, HvvStation destination) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departureDate is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getTimeIsDeparture(), "timeIsDeparture date is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAllowTariffDetails(), "allowTarifDetails date is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLengthBeforeDepartureTime(), "resultLengthBeforeDepartureTime is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLengthAfterDepartureTime(), "resultLengthAfterDepartureTime is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAllowReducedPrice(), "allowReducedPrice date is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAllowIntermediateStops(), "allowIntermediateStops is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getTariff(), "tariff is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getHvvReturnContSearchData(), "hvvReturnContSearchData is not allowed to be null");

        HvvJourneyBody journeyBody = new HvvJourneyBody();

        journeyBody.setStart(start);
        journeyBody.setDest(destination);
        journeyBody.setTime(getHvvTimeFormat(apiTokenAndUrlInformation.getDepartureDate()));
        journeyBody.setTimeIsDeparture(apiTokenAndUrlInformation.getTimeIsDeparture());
        journeyBody.setTariffDetails(apiTokenAndUrlInformation.getAllowTariffDetails());
        journeyBody.setSchedulesBefore(Long.valueOf(apiTokenAndUrlInformation.getResultLengthBeforeDepartureTime()));
        journeyBody.setSchedulesAfter(Long.valueOf(apiTokenAndUrlInformation.getResultLengthAfterDepartureTime()));
        journeyBody.setReturnReduced(apiTokenAndUrlInformation.getAllowReducedPrice());
        journeyBody.setIntermediateStops(apiTokenAndUrlInformation.getAllowIntermediateStops());
        journeyBody.setTariffInfoSelector(Collections.singletonList(
                new TariffInfoSelector(
                        apiTokenAndUrlInformation.getTariff(),
                        Arrays.asList(
                                NORMAL_TARIFF,
                                CHILD_TARIFF
                        )
                )
        ));
        journeyBody.setRealtime(RealtimeType.REALTIME);
        journeyBody.setReturnContSearchData(apiTokenAndUrlInformation.getHvvReturnContSearchData());

        return combineBaseHttpBodyWithApiCallBody(journeyBody, apiTokenAndUrlInformation);
    }

    private static Time getHvvTimeFormat(ZonedDateTime depatureDate) {
        return new Time(
                buildDateStringFrom(depatureDate),
                buildTimeStringFrom(depatureDate)
        );
    }

    private static String buildDateStringFrom(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private static String buildTimeStringFrom(ZonedDateTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
