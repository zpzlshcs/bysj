package com.gzpzg.gamesearch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VertexValue{
    VertexContent value;

    public VertexContent getValue() {
        return value;
    }

    public void setValue(VertexContent value) {
        this.value = value;
    }
}