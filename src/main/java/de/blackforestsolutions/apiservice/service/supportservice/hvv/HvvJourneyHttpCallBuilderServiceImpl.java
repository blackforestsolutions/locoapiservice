package de.blackforestsolutions.apiservice.service.supportservice.hvv;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hvv.request.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.setFormatToJsonFor;
import static de.blackforestsolutions.apiservice.service.supportservice.hvv.HvvHttpCallBuilder.*;

@Slf4j
@Service
public class HvvJourneyHttpCallBuilderServiceImpl implements HvvJourneyHttpCallBuilderService {

    private static final int NORMAL_TARIFF = 1;
    private static final int CHILD_TARIFF = 2;

    @SuppressWarnings("rawtypes")
    @Override
    public HttpEntity buildJourneyHttpEntityForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, HvvStation start, HvvStation destination) {
        String body;
        body = buildJourneyHttpBodyForHvv(apiTokenAndUrlInformation, start, destination);
        HttpHeaders httpHeaders = buildHttpHeadersForHvvJourneyWith(apiTokenAndUrlInformation, start, destination);
        return new HttpEntity<>(
                body,
                httpHeaders
        );
    }

    @Override
    public String buildJourneyPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "path variable is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyPathVariable(), "journey path variable is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat(apiTokenAndUrlInformation.getJourneyPathVariable());
    }

    private HttpHeaders buildHttpHeadersForHvvJourneyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, HvvStation start, HvvStation destination) {
        HttpHeaders httpHeaders = new HttpHeaders();
        setFormatToJsonFor(httpHeaders);
        setBaseHttpHeaderFor(httpHeaders, apiTokenAndUrlInformation);

        String jsonBody = buildJourneyHttpBodyForHvv(apiTokenAndUrlInformation, start, destination);
        setHvvAuthentificationSignatureFor(httpHeaders, apiTokenAndUrlInformation, jsonBody.getBytes(StandardCharsets.UTF_8));

        return httpHeaders;
    }

    private String buildJourneyHttpBodyForHvv(ApiTokenAndUrlInformation apiTokenAndUrlInformation, HvvStation start, HvvStation destination) {
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

    private Time getHvvTimeFormat(Date depatureDate) {
        return new Time(
                buildDateStringFrom(depatureDate),
                buildTimeStringFrom(depatureDate)
        );
    }

    private String buildDateStringFrom(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    private String buildTimeStringFrom(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(date);
    }
}
