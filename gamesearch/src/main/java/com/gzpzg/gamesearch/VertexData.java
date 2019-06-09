package com.gzpzg.gamesearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gzpzg.gamesearch.Entity.VertexResult;
import com.gzpzg.gamesearch.Entity.VertexStatus;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VertexData {
    VertexResult result;
    VertexStatus status;

    public VertexStatus getStatus() {
        return status;
    }
    public void setStatus(VertexStatus status) {
        this.status = status;
    }
    public VertexResult getResult() {
        return result;
    }
    public void setResult(VertexResult result) {
        this.result = result;
    }












}

