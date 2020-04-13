package de.blackforestsolutions.apiservice.service.supportservice;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.Objects;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.buildUrlWith;

@Service
public class OSMHttpCallBuilderServiceImpl implements OSMHttpCallBuilderService {

    private static final String FORMAT = "format";
    private static final String QUERY = "q";
    private static final String ADDRESS_DETAILS = "addressdetails";
    private static final String LIMIT = "limit";


    @Override
    public URL buildOSMUrlWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        return buildUrlWith(apiTokenAndUrlInformation);
    }

    @Override
    public String buildOSMPathWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String address) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getOutputFormat(), "outputFormat  is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLength(), "resultLength is not allowed to be null");
        return "/"
                .concat("?")
                .concat(ADDRESS_DETAILS)
                .concat("=")
                .concat(String.valueOf(1))
                .concat("&")
                .concat(QUERY)
                .concat("=")
                .concat(replaceWhitespace(address))
                .concat("&")
                .concat(FORMAT)
                .concat("=")
                .concat(apiTokenAndUrlInformation.getOutputFormat())
                .concat("&")
                .concat(LIMIT)
                .concat("=")
                .concat(String.valueOf(apiTokenAndUrlInformation.getResultLength()));
    }

    private String replaceWhitespace(String address) {
        return StringUtils.replace(address, " ", "+");
    }
}
