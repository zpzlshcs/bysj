package com.gzpzg.gamesearch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public  class VertexPropertie{
    PropertieValue value;

    public PropertieValue getValue() {
        return value;
    }

    public void setValue(PropertieValue value) {
        this.value = value;
    }
}
