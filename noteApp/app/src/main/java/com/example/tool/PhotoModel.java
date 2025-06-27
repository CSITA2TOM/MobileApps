package com.example.tool;

public class PhotoModel {
    String id;
    String lat;
    String lon;
    String imgPath;
    String updateTime;


    public PhotoModel(String id, String lat, String lon, String imgPath, String updateTime) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.imgPath = imgPath;
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getId() {
        return id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
