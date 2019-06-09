package com.gzpzg.gamesearch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class VertexContent{
    VertexID id;
    Map<String,List<VertexPropertie>> properties;

    public VertexID getId() {
        return id;
    }

    public void setId(VertexID id) {
        this.id = id;
    }

    public Map<String, List<VertexPropertie>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, List<VertexPropertie>> properties) {
        this.properties = properties;
    }
}
