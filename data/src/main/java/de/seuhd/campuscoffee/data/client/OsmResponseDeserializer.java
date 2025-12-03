package de.seuhd.campuscoffee.data.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Custom deserializer that extracts node id and tags from the OSM XML nested structure.
 */
public class OsmResponseDeserializer extends JsonDeserializer<OsmResponse> {
    @Override
    public OsmResponse deserialize(JsonParser p, DeserializationContext context)
            throws IOException {
        JsonNode root = p.getCodec().readTree(p);
        JsonNode nodeElement = root.get("node");

        if (nodeElement == null || !nodeElement.has("id") || !nodeElement.has("tag")) {
            throw new JsonMappingException(p, "Missing required elements or attributes in OSM XML response.");
        }

        return OsmResponse.builder()
                .id(nodeElement.get("id").asLong())
                .tags(deserializeTags(nodeElement.get("tag")))
                .build();
    }

    private Map<String, String> deserializeTags(JsonNode tagNode) {
        if (tagNode == null || !tagNode.isArray()) {
            return Collections.emptyMap();
        }

        return StreamSupport.stream(tagNode.spliterator(), false)
                .collect(Collectors.toMap(
                        node -> node.get("k").asText(),
                        node -> node.get("v").asText()
                ));
    }
}
