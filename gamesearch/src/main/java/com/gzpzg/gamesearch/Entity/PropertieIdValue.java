package com.gzpzg.gamesearch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertieIdValue{
    String relationId;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
