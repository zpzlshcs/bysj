package com.gzpzg.gamesearch.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VertexResult{
    VertexList data;

    public VertexList getData() {
        return data;
    }
    public void setData(VertexList data) {
        this.data = data;
    }
}