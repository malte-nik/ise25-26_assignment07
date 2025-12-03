package de.seuhd.campuscoffee.data.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * DTO for OSM API XML responses.
 * Combines the root osm element and nested node element into a single class.
 */
@Data
@Builder(toBuilder = true)
@JacksonXmlRootElement(localName = "osm")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = OsmResponseDeserializer.class)
public class OsmResponse {
    private Long id;
    private Map<String, String> tags;
}
