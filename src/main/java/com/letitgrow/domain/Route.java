package com.letitgrow.domain;

import java.util.Map;

public class Route {
    String name;
    String title;
    Map<Integer, POI> poiMap;

    public Route() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<Integer, POI> getPoiMap() {
        return poiMap;
    }

    public void setPoiMap(Map<Integer, POI> poiMap) {
        this.poiMap = poiMap;
    }
}
