package de.blackforestsolutions.apiservice.service.supportservice.hafas;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.xml.bind.DatatypeConverter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpBodyBuilderService.buildJourneyHttpBodyWith;
import static de.blackforestsolutions.apiservice.service.supportservice.hafas.HafasHttpBodyBuilderService.buildLocationHttpBodyWith;

@Slf4j
@Service
public class HafasHttpCallBuilderServiceImpl implements HafasHttpCallBuilderService {

    private static String generateChecksumWithLocationOrJourneyBody(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        return Optional.ofNullable(station)
                .map(stat -> generateChecksumWith(apiTokenAndUrlInformation, buildLocationHttpBodyWith(apiTokenAndUrlInformation, station)))
                .orElseGet(() -> generateChecksumWith(apiTokenAndUrlInformation, buildJourneyHttpBodyWith(apiTokenAndUrlInformation)));
    }

    private static String generateMicWithLocationOrJourneyBody(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        return Optional.ofNullable(station)
                .map(stat -> generateMicWith(buildLocationHttpBodyWith(apiTokenAndUrlInformation, station)))
                .orElseGet(() -> generateMicWith(buildJourneyHttpBodyWith(apiTokenAndUrlInformation)));
    }

    private static String generateMacWithLocationOrJourneyBody(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        return Optional.ofNullable(station)
                .map(stat -> generateMacWith(apiTokenAndUrlInformation, buildLocationHttpBodyWith(apiTokenAndUrlInformation, station)))
                .orElseGet(() -> generateMacWith(apiTokenAndUrlInformation, buildJourneyHttpBodyWith(apiTokenAndUrlInformation)));
    }

    private static String generateChecksumWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String requestBody) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorizationKey(), "authorization key is not allowed to be null");
        byte[] saltBytes = apiTokenAndUrlInformation.getAuthorizationKey().getBytes(StandardCharsets.UTF_8);
        byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
        return DigestUtils.md5DigestAsHex(ArrayUtils.addAll(requestBodyBytes, saltBytes));
    }

    private static String generateMicWith(String requestBody) {
        return DigestUtils.md5DigestAsHex(requestBody.getBytes(StandardCharsets.UTF_8));
    }

    private static String generateMacWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String requestBody) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorizationKey(), "authorization key is not allowed to be null");
        byte[] micBytes = generateMicWith(requestBody).getBytes(StandardCharsets.UTF_8);
        byte[] saltBytes = DatatypeConverter.parseHexBinary(apiTokenAndUrlInformation.getAuthorizationKey());
        return DigestUtils.md5DigestAsHex(ArrayUtils.addAll(micBytes, saltBytes));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public HttpEntity buildHttpEntityStationForHafas(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        return new HttpEntity<>(buildLocationHttpBodyWith(apiTokenAndUrlInformation, station));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public HttpEntity buildHttpEntityJourneyForHafas(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return new HttpEntity<>(buildJourneyHttpBodyWith(apiTokenAndUrlInformation));
    }

    @Override
    public String buildPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getPathVariable(), "path variable is not allowed to be null");
        return "/"
                .concat(apiTokenAndUrlInformation.getPathVariable())
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getChecksum())
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getChecksum())
                        .map(checksum -> "=")
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getChecksum())
                        .map(mac -> generateChecksumWithLocationOrJourneyBody(apiTokenAndUrlInformation, station))
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getMic())
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getMic())
                        .map(mic -> "=")
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getMic())
                        .map(mic -> generateMicWithLocationOrJourneyBody(apiTokenAndUrlInformation, station))
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getMac())
                        .map(mac -> "&")
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getMac())
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getMac())
                        .map(mac -> "=")
                        .orElse(""))
                .concat(Optional.ofNullable(apiTokenAndUrlInformation.getMac())
                        .map(mac -> generateMacWithLocationOrJourneyBody(apiTokenAndUrlInformation, station))
                        .orElse(""));
    }
}
