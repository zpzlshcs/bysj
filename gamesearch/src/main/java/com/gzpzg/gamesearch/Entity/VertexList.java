package com.gzpzg.gamesearch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class VertexList{
    List<VertexValue> value;

    public List<VertexValue> getValue() {
        return value;
    }

    public void setValue(List<VertexValue> value) {
        this.value = value;
    }
}