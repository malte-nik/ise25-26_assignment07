package de.seuhd.campuscoffee.data.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for OpenStreetMap API integration.
 */
@FeignClient(
        name = "osm-api",
        url = "${osm.api.base-url}",
        configuration = OsmFeignClientConfig.class
)
public interface OsmFeignClient {
    /**
     * Fetches a node by its ID from the OpenStreetMap API.
     *
     * @param nodeId the OSM node ID
     * @return XML response as a String
     */
    @GetMapping("/node/{id}")
    String fetchNode(@PathVariable("id") Long nodeId);
}
