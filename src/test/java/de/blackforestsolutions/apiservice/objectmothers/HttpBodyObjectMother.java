package de.blackforestsolutions.apiservice.objectmothers;

import de.blackforestsolutions.apiservice.configuration.AdditionalHttpConfiguration;
import de.blackforestsolutions.generatedcontent.hafas.request.*;
import de.blackforestsolutions.generatedcontent.hafas.request.journey.*;
import de.blackforestsolutions.generatedcontent.hafas.request.locations.HafasRequestLocationBody;
import de.blackforestsolutions.generatedcontent.hafas.request.locations.Input;
import de.blackforestsolutions.generatedcontent.hafas.request.locations.Loc;
import de.blackforestsolutions.generatedcontent.hvv.request.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getRosenhofHvvStation;
import static de.blackforestsolutions.apiservice.objectmothers.TravelPointObjectMother.getStadthausbrueckeHvvStation;

public class HttpBodyObjectMother {

    private static final int FIRST_INDEX = 0;

    public static MultiValueMap<String, String> getLufthansaAuthorizationBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(AdditionalHttpConfiguration.CLIENT_ID, "v6tqqk92k2a6zzaharpz56x6");
        body.add(AdditionalHttpConfiguration.CLIENT_SECRET, "c9kUKEuT3k");
        body.add(AdditionalHttpConfiguration.GRANT_TYPE, "client_credentials");
        return body;
    }

    public static HvvStationListBody getStationListHttpBody() {
        HvvStationListBody body = new HvvStationListBody();

        body.setLanguage("de");
        body.setVersion(37.3);
        body.setFilterType(FilterType.NO_FILTER);
        body.setModificationTypes(ModificationType.MAIN);
        body.setFilterEquivalent(true);

        return body;
    }

    public static HvvTravelPointBody getHvvTravelPointBody() {
        HvvTravelPointBody body = new HvvTravelPointBody();

        body.setLanguage("de");
        body.setVersion(37.3);
        body.setFilterType(FilterType.NO_FILTER);
        body.setTheName(new HvvStation("Ahrensburg"));
        body.setMaxList(1);
        body.setMaxDistance(2500);
        body.setTariffDetails(true);
        body.setAllowTypeSwitch(true);

        return body;
    }

    public static HvvJourneyBody getHvvJourneyBody() {
        HvvJourneyBody body = new HvvJourneyBody();

        body.setLanguage("de");
        body.setVersion(37.3d);
        body.setFilterType(FilterType.NO_FILTER);
        body.setStart(getRosenhofHvvStation());
        body.setDest(getStadthausbrueckeHvvStation());
        body.setTime(
                new Time(
                        "31.01.2020",
                        "13:10:00"
                )
        );
        body.setTimeIsDeparture(true);
        body.setTariffDetails(true);
        body.setSchedulesBefore(2L);
        body.setSchedulesAfter(2L);
        body.setReturnReduced(true);
        body.setTariffInfoSelector(Collections.singletonList(
                new TariffInfoSelector(
                        "all",
                        Arrays.asList(1L, 2L)
                )
        ));
        body.setRealtime(RealtimeType.REALTIME);
        body.setReturnContSearchData(true);
        body.setIntermediateStops(true);

        return body;
    }

    public static HafasRequestLocationBody getVBBLocationRequestBody() {
        HafasRequestLocationBody body = new HafasRequestLocationBody();
        body.setLang("de");
        body.setSvcReqL(getBasicLocationReqlist());
        body.setClient(getVBBClient());
        body.setExt("VBB.1");
        body.setVer("1.16");
        body.setAuth(getVBBAuth());
        return body;
    }

    private static Client getVBBClient() {
        Client client = new Client();
        client.setId("VBB");
        client.setName("vbbPROD");
        client.setV("4010300");
        client.setType("IPA");
        return client;
    }

    private static Auth getVBBAuth() {
        Auth auth = new Auth();
        auth.setType("AID");
        auth.setAid("hafas-vbb-apps");
        return auth;
    }

    public static HafasRequestLocationBody getDBLocationRequestBody() {
        HafasRequestLocationBody body = new HafasRequestLocationBody();
        body.setLang("de");
        List<SvcReqL> requestList = getBasicLocationReqlist();
        requestList.get(0).getCfg().setRtMode("HYBRID");
        body.setSvcReqL(requestList);
        body.setClient(getDBClient());
        body.setExt("DB.R19.04.a");
        body.setVer("1.16");
        body.setAuth(getDBAuth());
        return body;
    }

    private static Client getDBClient() {
        Client client = new Client();
        client.setId("DB");
        client.setName("DB Navigator");
        client.setV("16040000");
        client.setType("IPH");
        return client;
    }

    private static Auth getDBAuth() {
        Auth auth = new Auth();
        auth.setType("AID");
        auth.setAid("n91dB8Z77MLdoR0K");
        return auth;
    }

    public static HafasRequestLocationBody getNahShLocationRequestBody() {
        HafasRequestLocationBody body = new HafasRequestLocationBody();
        body.setLang("de");
        body.setSvcReqL(getBasicLocationReqlist());
        body.setClient(getNahSHClient());
        body.setVer("1.16");
        body.setAuth(getNahShAuth());
        return body;
    }

    private static Client getNahSHClient() {
        Client client = new Client();
        client.setId("NAHSH");
        client.setName("NAHSHPROD");
        client.setV("3000700");
        return client;
    }

    private static Auth getNahShAuth() {
        Auth auth = new Auth();
        auth.setAid("r0Ot9FLFNAFxijLW");
        return auth;
    }

    private static Cfg getBasicCfg() {
        Cfg cfg = new Cfg();
        cfg.setPolyEnc("GPA");
        return cfg;
    }

    private static List<SvcReqL> getBasicLocationReqlist() {
        List<SvcReqL> requestList = new ArrayList<>();
        SvcReqL svcReqL = new SvcReqL();
        svcReqL.setCfg(getBasicCfg());
        svcReqL.setMeth("LocMatch");
        Loc loc = new Loc();
        loc.setType("ALL");
        loc.setName("Eiderstraße 87");
        Input input = new Input();
        input.setLoc(loc);
        input.setMaxLoc(1);
        input.setField("S");
        Req req = new Req();
        req.setInput(input);
        svcReqL.setReq(req);
        requestList.add(svcReqL);
        return requestList;
    }

    public static HafasRequestJourneyBody getDBJourneyHttpBody() {
        HafasRequestJourneyBody body = new HafasRequestJourneyBody();
        body.setLang("de");
        body.setSvcReqL(getDBReqList());
        body.setClient(getDBClient());
        body.setExt("DB.R19.04.a");
        body.setVer("1.16");
        body.setAuth(getDBAuth());
        return body;
    }

    private static List<SvcReqL> getDBReqList() {
        return getBasicReqList()
                .stream()
                .peek(svcReqL -> {
                    svcReqL.setCfg(getDBCfg());
                    svcReqL.setReq(getDbReq());
                })
                .collect(Collectors.toList());
    }

    private static List<SvcReqL> getVBBReqList() {
        return getBasicReqList()
                .stream()
                .peek(svcReqL -> {
                    svcReqL.setCfg(getBasicCfg());
                    svcReqL.setReq(getVBBReq());
                })
                .collect(Collectors.toList());
    }

    private static List<SvcReqL> getBasicReqList() {
        List<SvcReqL> svcReqLS = new ArrayList<>();
        SvcReqL svcReqL = new SvcReqL();
        svcReqL.setMeth("TripSearch");
        svcReqLS.add(svcReqL);
        return svcReqLS;
    }

    private static Cfg getDBCfg() {
        Cfg cfg = new Cfg();
        cfg.setPolyEnc("GPA");
        cfg.setRtMode("HYBRID");
        return cfg;
    }

    private static Req getVBBReq() {
        Req req = getBasicReq();
        req.getJnyFltrL().get(FIRST_INDEX).setValue("127");
        req.setOutDate("20200214");
        req.setOutTime("092653");
        req.setDepLocL(getVBBDepartureLocL());
        req.setArrLocL(getVBBArrivalLocL());
        req.setGisFltrL(getVBBGisFltrL());
        req.setNumF(1L);
        req.setOutFrwd(true);
        return req;
    }

    private static List<GisFltrL> getVBBGisFltrL() {
        List<GisFltrL> gisFltrLS = new ArrayList<>();
        GisFltrL gisFltrL = new GisFltrL();
        gisFltrL.setMeta("foot_speed_normal");
        gisFltrL.setMode("FB");
        gisFltrL.setType("M");
        gisFltrLS.add(gisFltrL);
        return gisFltrLS;
    }

    private static List<DepLocL> getVBBDepartureLocL() {
        List<DepLocL> depLocLList = new ArrayList<>();
        DepLocL depLocL = new DepLocL();
        depLocL.setType("S");
        depLocL.setLid("A=1@L=770000350@");
        depLocLList.add(depLocL);
        return depLocLList;
    }

    private static List<ArrLocL> getVBBArrivalLocL() {
        List<ArrLocL> arrLocLList = new ArrayList<>();
        ArrLocL arrLocL = new ArrLocL();
        arrLocL.setType("S");
        arrLocL.setLid("A=1@L=900985256@");
        arrLocLList.add(arrLocL);
        return arrLocLList;
    }

    private static Req getDbReq() {
        Req req = getBasicReq();
        req.getJnyFltrL().get(FIRST_INDEX).setValue("1023");
        req.setOutDate("20200214");
        req.setOutTime("094429");
        req.setDepLocL(getDBDepartureLocL());
        req.setArrLocL(getDBArrivalLocl());
        req.setGisFltrL(new ArrayList<>());
        req.setNumF(1L);
        req.setOutFrwd(true);
        req.setTrfReq(getDBTrafficRequest());
        return req;
    }

    private static Req getBasicReq() {
        Req req = new Req();
        req.setGetPasslist(true);
        req.setMaxChg(-1L);
        req.setMinChgTime(0L);
        req.setJnyFltrL(getBasicJourneyFilter());
        req.setGetTariff(true);
        req.setUshrp(true);
        req.setGetPT(true);
        req.setGetIV(false);
        req.setGetPolyline(false);
        return req;
    }

    private static List<DepLocL> getDBDepartureLocL() {
        List<DepLocL> depLocLList = new ArrayList<>();
        DepLocL depLocL = new DepLocL();
        depLocL.setType("S");
        depLocL.setLid("A=1@L=981067408@");
        depLocLList.add(depLocL);
        return depLocLList;
    }

    private static List<ArrLocL> getDBArrivalLocl() {
        List<ArrLocL> arrLocLList = new ArrayList<>();
        ArrLocL arrLocL = new ArrLocL();
        arrLocL.setType("S");
        arrLocL.setLid("A=1@L=000362734@");
        arrLocLList.add(arrLocL);
        return arrLocLList;
    }

    private static List<JnyFltrL> getBasicJourneyFilter() {
        List<JnyFltrL> jnyFltrLList = new ArrayList<>();
        JnyFltrL production = new JnyFltrL();
        production.setType("PROD");
        production.setMode("INC");
        jnyFltrLList.add(production);
        JnyFltrL meta = new JnyFltrL();
        meta.setType("META");
        meta.setMode("INC");
        meta.setMeta("notBarrierfree");
        jnyFltrLList.add(meta);
        return jnyFltrLList;
    }

    private static TrfReq getDBTrafficRequest() {
        TrfReq trfReq = new TrfReq();
        trfReq.setJnyCl(2);
        trfReq.setTvlrProf(getDBCard());
        trfReq.setCType("PK");
        return trfReq;
    }

    private static List<TvlrProf> getDBCard() {
        List<TvlrProf> tvlrProfList = new ArrayList<>();
        TvlrProf tvlrProf = new TvlrProf();
        tvlrProf.setType("E");
        tvlrProf.setRedtnCard(null);
        tvlrProfList.add(tvlrProf);
        return tvlrProfList;
    }

    public static HafasRequestJourneyBody getVBBJourneyHttpBody() {
        HafasRequestJourneyBody body = new HafasRequestJourneyBody();
        body.setLang("de");
        body.setSvcReqL(getVBBReqList());
        body.setClient(getVBBClient());
        body.setExt("VBB.1");
        body.setVer("1.16");
        body.setAuth(getVBBAuth());
        return body;
    }

    public static HafasRequestJourneyBody getNahSHJourneyHttpBody() {
        HafasRequestJourneyBody body = new HafasRequestJourneyBody();
        body.setLang("de");
        body.setSvcReqL(getNahSHReqList());
        body.setClient(getNahSHClient());
        body.setVer("1.16");
        body.setAuth(getNahShAuth());
        return body;
    }

    private static List<SvcReqL> getNahSHReqList() {
        return getBasicReqList()
                .stream()
                .peek(svcReqL -> {
                    svcReqL.setCfg(getBasicCfg());
                    svcReqL.setReq(getNahSHReq());
                })
                .collect(Collectors.toList());
    }

    private static Req getNahSHReq() {
        Req req = getBasicReq();
        req.getJnyFltrL().get(FIRST_INDEX).setValue("1023");
        req.setOutDate("20200213");
        req.setOutTime("114650");
        req.setDepLocL(getNahSHDepartureLocL());
        req.setArrLocL(getNahSHArrivalLocL());
        req.setGisFltrL(new ArrayList<>());
        req.setNumF(1L);
        req.setOutFrwd(true);
        return req;
    }

    private static List<DepLocL> getNahSHDepartureLocL() {
        List<DepLocL> depLocLList = new ArrayList<>();
        DepLocL depLocL = new DepLocL();
        depLocL.setType("S");
        depLocL.setLid("A=1@L=981068999@");
        depLocLList.add(depLocL);
        return depLocLList;
    }

    private static List<ArrLocL> getNahSHArrivalLocL() {
        List<ArrLocL> arrLocLList = new ArrayList<>();
        ArrLocL arrLocL = new ArrLocL();
        arrLocL.setType("S");
        arrLocL.setLid("A=1@L=4397734@");
        arrLocLList.add(arrLocL);
        return arrLocLList;
    }

}
