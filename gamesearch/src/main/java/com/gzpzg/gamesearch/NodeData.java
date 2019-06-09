package com.gzpzg.gamesearch;

public class NodeData {

    public NodeData(String uuid,String name){
        this.uuid = uuid;
        this.name = name;
    }
    public NodeData(String name,String sourceid,String targetid){
        this.name = name;
        this.sourceid = sourceid;
        this.targetid = targetid;

    }
    String uuid;
    String name;



    String sourceid;
    String targetid;

    String color;
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSourceid() {
        return sourceid;
    }

    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }

    public String getTargetid() {
        return targetid;
    }

    public void setTargetid(String targetid) {
        this.targetid = targetid;
    }
}
