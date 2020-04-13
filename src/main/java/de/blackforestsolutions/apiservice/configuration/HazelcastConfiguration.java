package de.blackforestsolutions.apiservice.configuration;

import de.blackforestsolutions.datamodel.ApiTokenAndUrlInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class HazelcastConfiguration {

    @Value("${hazelcast.host}")
    private String hazelcastHost;
    @Value("${hazelcast.read.path}")
    private String hazelcastReadPath;
    @Value("${hazelcast.write.path}")
    private String hazelcastWritePath;
    @Value("${hazelcast.readall.path}")
    private String hazelCastReadAllPath;
    @Value("${accessProtocol}")
    private String acesProtocol;

    @Bean(name = "hazelcastApiTokenAndUrlInformation")
    public ApiTokenAndUrlInformation apiTokenAndUrlInformation() {
        ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder builder = new ApiTokenAndUrlInformation.ApiTokenAndUrlInformationBuilder();
        builder.setProtocol(acesProtocol);
        builder.setHost(hazelcastHost);
        builder.setHazelcastPath(hazelcastReadPath);
        builder.setHazelcastWritePath(hazelcastWritePath);
        builder.setHazelcastReadAllPath(hazelCastReadAllPath);
        return builder.build();
    }
}
