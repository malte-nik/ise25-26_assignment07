package de.seuhd.campuscoffee.data.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OSM Feign client.
 */
@Configuration
public class OsmFeignClientConfig {
    /**
     * Adds User-Agent header to all OSM API requests.
     *
     * @return RequestInterceptor that adds the User-Agent header
     */
    @Bean
    public RequestInterceptor userAgentInterceptor() {
        return requestTemplate ->
            requestTemplate.header("User-Agent", "CampusCoffee/0.0.2");
    }
}
