package de.blackforestsolutions.apiservice.service.communicationservice;

import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.apiservice.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.apiservice.service.mapper.SearchChMapperService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderService;
import de.blackforestsolutions.apiservice.service.supportservice.SearchChHttpCallBuilderServiceImpl;
import de.blackforestsolutions.apiservice.stubs.RestTemplateBuilderStub;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

class SearchChApiServiceTest {

    private static final RestTemplate REST_TEMPLATE = Mockito.mock(RestTemplate.class);

    private final RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilderStub(REST_TEMPLATE);

    private final SearchChHttpCallBuilderService searchChHttpCallBuilderService = new SearchChHttpCallBuilderServiceImpl();

    private final CallService callService = new CallServiceImpl(restTemplateBuilder);

    @Mock
    private final SearchChMapperService searchChMapperService = Mockito.mock(SearchChMapperService.class);

    @InjectMocks
    private final SearchChApiService classUnderTest = new SearchChApiServiceImpl(callService, searchChHttpCallBuilderService, searchChMapperService);


}
