package com.letitgrow.domain;

import com.vaadin.tapio.googlemaps.client.LatLon;

public final class POI {
    private String name;
    private LatLon coordinates;
    private String language;
    private String movie;

    public POI(){
    }

    public POI(String name, LatLon coordinates, String movie, String language){
        this.name = name;
        this.coordinates = coordinates;
        this.movie = movie;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLon getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLon coordinates) {
        this.coordinates = coordinates;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getCoordinatesString() {
        if(coordinates != null)
            return Double.toString(coordinates.getLat()) + Double.toString(coordinates.getLon());
        else return null;
    }
}
