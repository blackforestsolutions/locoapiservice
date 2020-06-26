package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilder.combineBaseHttpBodyWithApiCallBody;

class HvvHttpBodyService {

    private static final int NORMAL_TARIFF = 1;
    private static final int CHILD_TARIFF = 2;

    static String buildStationListHttpBodyForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        HvvStationListBody hvvStationListBody = new HvvStationListBody();
        hvvStationListBody.setFilterEquivalent(apiTokenAndUrlInformation.getHvvFilterEquivalent());
        hvvStationListBody.setModificationTypes(ModificationType.MAIN);

        return HvvHttpCallBuilder.combineBaseHttpBodyWithApiCallBody(hvvStationListBody, apiTokenAndUrlInformation);
    }

    static String buildTravelPointHttpBodyForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        HvvTravelPointBody hvvTravelPointBody = new HvvTravelPointBody();

        hvvTravelPointBody.setTheName(new HvvStation(station));
        hvvTravelPointBody.setAllowTypeSwitch(apiTokenAndUrlInformation.getHvvAllowTypeSwitch());
        hvvTravelPointBody.setMaxList(apiTokenAndUrlInformation.getResultLength());
        hvvTravelPointBody.setMaxDistance(apiTokenAndUrlInformation.getDistanceFromTravelPoint());
        hvvTravelPointBody.setTariffDetails(apiTokenAndUrlInformation.getAllowTariffDetails());

        return HvvHttpCallBuilder.combineBaseHttpBodyWithApiCallBody(hvvTravelPointBody, apiTokenAndUrlInformation);

    }

    static String buildJourneyHttpBodyForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, HvvStation start, HvvStation destination) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departure date is not allowed to be null");
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
        journeyBody.setSchedulesBefore(apiTokenAndUrlInformation.getResultLengthBeforeDepartureTime());
        journeyBody.setSchedulesAfter(apiTokenAndUrlInformation.getResultLengthAfterDepartureTime());
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

    private static Time getHvvTimeFormat(Date depatureDate) {
        return new Time(
                buildDateStringFrom(depatureDate),
                buildTimeStringFrom(depatureDate)
        );
    }

    private static String buildDateStringFrom(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    private static String buildTimeStringFrom(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(date);
    }
}
