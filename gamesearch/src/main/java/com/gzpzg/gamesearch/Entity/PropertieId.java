package com.gzpzg.gamesearch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertieId{
    PropertieIdValue value;

    public PropertieIdValue getValue() {
        return value;
    }

    public void setValue(PropertieIdValue value) {
        this.value = value;
    }
}
