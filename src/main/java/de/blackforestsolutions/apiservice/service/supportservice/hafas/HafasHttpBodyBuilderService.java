package de.blackforestsolutions.apiservice.service.supportservice.hafas;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import de.blackforestsolutions.generatedcontent.hafas.request.*;
import de.blackforestsolutions.generatedcontent.hafas.request.configurations.*;
import de.blackforestsolutions.generatedcontent.hafas.request.journey.*;
import de.blackforestsolutions.generatedcontent.hafas.request.locations.HafasRequestLocationBody;
import de.blackforestsolutions.generatedcontent.hafas.request.locations.Input;
import de.blackforestsolutions.generatedcontent.hafas.request.locations.Loc;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static de.blackforestsolutions.apiservice.service.supportservice.HttpCallBuilder.convertHttpModelToJson;

class HafasHttpBodyBuilderService {

    static String buildLocationHttpBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getLanguage(), "language is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "api version is not allowed to be null");
        HafasRequestLocationBody body = new HafasRequestLocationBody();
        body.setLang(apiTokenAndUrlInformation.getLanguage());
        body.setSvcReqL(buildLocationRequestBodyWith(apiTokenAndUrlInformation, station));
        body.setClient(buildClientBodywith(apiTokenAndUrlInformation));
        Optional.ofNullable(apiTokenAndUrlInformation.getApiName()).ifPresent(body::setExt);
        body.setVer(apiTokenAndUrlInformation.getApiVersion());
        body.setAuth(buildAuthorizationBodyWith(apiTokenAndUrlInformation));
        return convertHttpModelToJson(body);
    }

    static String buildJourneyHttpBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getLanguage(), "language is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getApiVersion(), "api version is not allowed to be null");
        HafasRequestJourneyBody body = new HafasRequestJourneyBody();
        body.setLang(apiTokenAndUrlInformation.getLanguage());
        body.setSvcReqL(buildJourneyRequestBodyWith(apiTokenAndUrlInformation));
        body.setClient(buildClientBodywith(apiTokenAndUrlInformation));
        Optional.ofNullable(apiTokenAndUrlInformation.getApiName()).ifPresent(body::setExt);
        body.setVer(apiTokenAndUrlInformation.getApiVersion());
        body.setAuth(buildAuthorizationBodyWith(apiTokenAndUrlInformation));
        return convertHttpModelToJson(body);
    }

    private static List<SvcReqL> buildJourneyRequestBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getJourneyPathVariable(), "journey path is not allowed to be null");
        List<SvcReqL> requestList = new ArrayList<>();
        SvcReqL requestBody = new SvcReqL();
        requestBody.setCfg(buildCfgBodyWith(apiTokenAndUrlInformation));
        requestBody.setMeth(apiTokenAndUrlInformation.getJourneyPathVariable());
        requestBody.setReq(req(apiTokenAndUrlInformation));
        requestList.add(requestBody);
        return requestList;
    }

    private static List<SvcReqL> buildLocationRequestBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getLocationPath(), "location path is not allowed to be null");
        List<SvcReqL> requestList = new ArrayList<>();
        SvcReqL requestBody = new SvcReqL();
        requestBody.setCfg(buildCfgBodyWith(apiTokenAndUrlInformation));
        requestBody.setMeth(apiTokenAndUrlInformation.getLocationPath());
        requestBody.setReq(buildLocationRequestBodywith(apiTokenAndUrlInformation, station));
        requestList.add(requestBody);
        return requestList;
    }

    private static Req req(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getDepartureDate(), "departure date is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAllowIntermediateStops(), "allow intermediate stops is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getTransfers(), "transfers is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getMinTransferTime(), "min transfer time is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAllowTariffDetails(), "tariff details is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getAllowCoordinates(), "coordinates is not allowed to be null");
        Req req = new Req();
        req.setOutDate(transformDateToString(apiTokenAndUrlInformation.getDepartureDate()));
        req.setOutTime(transformDateToTime(apiTokenAndUrlInformation.getDepartureDate()));
        req.setGetPasslist(apiTokenAndUrlInformation.getAllowIntermediateStops());
        req.setMaxChg(Long.valueOf(apiTokenAndUrlInformation.getTransfers()));
        req.setMinChgTime(Long.valueOf(apiTokenAndUrlInformation.getMinTransferTime()));
        req.setDepLocL(buildDepartureBodyWith(apiTokenAndUrlInformation));
        req.setArrLocL(buildArrivalBodyWith(apiTokenAndUrlInformation));
        req.setJnyFltrL(journeyFltr(apiTokenAndUrlInformation));
        req.setGisFltrL(gisFltrLList(apiTokenAndUrlInformation));
        req.setGetTariff(apiTokenAndUrlInformation.getAllowTariffDetails());
        req.setUshrp(true);
        req.setGetPT(true);
        req.setGetIV(false);
        req.setGetPolyline(apiTokenAndUrlInformation.getAllowCoordinates());
        Optional.ofNullable(apiTokenAndUrlInformation.getResultLengthAfterDepartureTime()).ifPresent(resultLengthAfterDeparture -> req.setNumF(Long.valueOf(resultLengthAfterDeparture)));
        Optional.ofNullable(apiTokenAndUrlInformation.getTimeIsDeparture()).ifPresent(req::setOutFrwd);
        Optional.ofNullable(apiTokenAndUrlInformation.getAllowReducedPrice()).ifPresent(reducedPrice -> req.setTrfReq(trfReq()));
        return req;
    }

    private static TrfReq trfReq() {
        TrfReq trfReq = new TrfReq();
        trfReq.setJnyCl(2);
        trfReq.setTvlrProf(tvlrProfList());
        trfReq.setCType(FieldType.PK.name());
        return trfReq;
    }

    private static List<TvlrProf> tvlrProfList() {
        List<TvlrProf> tvlrProfList = new ArrayList<>();
        TvlrProf tvlrProf = new TvlrProf();
        tvlrProf.setRedtnCard(null);
        tvlrProf.setType(FieldType.E.name());
        tvlrProfList.add(tvlrProf);
        return tvlrProfList;
    }

    private static List<GisFltrL> gisFltrLList(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        List<GisFltrL> gisFltrLS = new ArrayList<>();
        Optional.ofNullable(apiTokenAndUrlInformation.getWalkingSpeed()).ifPresent(walkingSpeed -> {
            GisFltrL gisFltrL = new GisFltrL();
            gisFltrL.setMeta(walkingSpeed);
            gisFltrL.setMode(JourneyFilterMode.FB.name());
            gisFltrL.setType(JourneyFilterType.M.name());
            gisFltrLS.add(gisFltrL);
        });

        return gisFltrLS;
    }

    private static List<JnyFltrL> journeyFltr(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getHafasProductionValue(), "hafas production value is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getForDisabledPersons(), "for disabled persons is not allowed to be null");
        List<JnyFltrL> jnyFltrLList = new ArrayList<>();
        JnyFltrL production = new JnyFltrL();
        production.setType(JourneyFilterType.PROD.name());
        production.setMode(JourneyFilterMode.INC.name());
        production.setValue(apiTokenAndUrlInformation.getHafasProductionValue());
        jnyFltrLList.add(production);
        JnyFltrL meta = new JnyFltrL();
        meta.setType(JourneyFilterType.META.name());
        meta.setMode(JourneyFilterMode.INC.name());
        meta.setMeta(apiTokenAndUrlInformation.getForDisabledPersons());
        jnyFltrLList.add(meta);
        return jnyFltrLList;
    }

    private static List<ArrLocL> buildArrivalBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getArrival(), "arrival is not allowed to be null");
        List<ArrLocL> arrLocLList = new ArrayList<>();
        ArrLocL arrival = new ArrLocL();
        arrival.setType(FieldType.S.name());
        arrival.setLid("A=1@L=".concat(apiTokenAndUrlInformation.getArrival()).concat("@"));
        arrLocLList.add(arrival);
        return arrLocLList;
    }

    private static List<DepLocL> buildDepartureBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getDeparture(), "departure is not allowed to be null");
        List<DepLocL> depLocLList = new ArrayList<>();
        DepLocL departure = new DepLocL();
        departure.setType(FieldType.S.name());
        departure.setLid("A=1@L=".concat(apiTokenAndUrlInformation.getDeparture()).concat("@"));
        depLocLList.add(departure);
        return depLocLList;
    }

    private static Req buildLocationRequestBodywith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        Req req = new Req();
        req.setInput(buildInputBodyWith(apiTokenAndUrlInformation, station));
        return req;
    }

    private static Input buildInputBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation, String station) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getResultLength(), "result length is not allowed to be null");
        Input input = new Input();
        input.setLoc(buildLocBodyWith(station));
        input.setMaxLoc(apiTokenAndUrlInformation.getResultLength());
        input.setField(FieldType.S.name());
        return input;
    }

    private static Loc buildLocBodyWith(String station) {
        Loc loc = new Loc();
        loc.setType(LocationType.ALL.name());
        loc.setName(station);
        return loc;
    }

    private static Cfg buildCfgBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Cfg cfg = new Cfg();
        cfg.setPolyEnc(PolyEnc.GPA.name());
        Optional.ofNullable(apiTokenAndUrlInformation.getHafasRtMode()).ifPresent(cfg::setRtMode);
        return cfg;
    }

    private static Client buildClientBodywith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getClientId(), "client id is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getClientVersion(), "client version is not allowed to be null");
        Objects.requireNonNull(apiTokenAndUrlInformation.getClientName(), "client name is not allowed to be null");
        Client client = new Client();
        client.setId(apiTokenAndUrlInformation.getClientId());
        client.setV(apiTokenAndUrlInformation.getClientVersion());
        Optional.ofNullable(apiTokenAndUrlInformation.getClientType()).ifPresent(client::setType);
        client.setName(apiTokenAndUrlInformation.getClientName());
        return client;
    }

    private static Auth buildAuthorizationBodyWith(ApiTokenAndUrlInformation apiTokenAndUrlInformation) {
        Objects.requireNonNull(apiTokenAndUrlInformation.getAuthorization(), "authorization is not allowed to be null");
        Auth auth = new Auth();
        Optional.ofNullable(apiTokenAndUrlInformation.getAuthentificationType()).ifPresent(auth::setType);
        auth.setAid(apiTokenAndUrlInformation.getAuthorization());
        return auth;
    }

    private static String transformDateToString(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private static String transformDateToTime(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("HHmmss"));
    }
}
