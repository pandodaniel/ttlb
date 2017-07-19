package com.letitgrow.view;

import com.letitgrow.TtlbUI;
import com.letitgrow.domain.POI;
import com.letitgrow.domain.Route;
import com.vaadin.client.data.DataSource;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.GoogleMapControl;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TTlbDashboard extends VerticalLayout {
    private HorizontalLayout poiLayout = new HorizontalLayout();
    private HorizontalLayout routeLayout = new HorizontalLayout();
    private POIGrid poiGrid = new POIGrid();
    private Map<String, POI> poiMap = new HashMap<String, POI>();

    private GoogleMap googleMap;

    public TTlbDashboard() {
        setSpacing(false);
        addComponent(buildTitle());
        addComponent(buildPOIsAndMap());
        addComponent(buildRoutesAndMap());

        //addComponent(googleMap);
    }

    private HorizontalLayout buildRoutesAndMap() {
        return new HorizontalLayout();
    }

    private HorizontalLayout buildPOIsAndMap() {
        poiLayout.setSizeFull();
        VerticalLayout poiGridLayout = new VerticalLayout();
        poiGridLayout.setSpacing(false);
        poiGridLayout.addComponent(buildCreateAndEditButton());
        poiLayout.addComponent(poiGrid.getPoiGrid());
        buildMap(TtlbUI.getDataProvider()
                .getPois(), null);
        poiLayout.addComponent(googleMap);
        return poiLayout;
    }

    private Component buildCreateAndEditButton() {
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        Button createButton = new Button("Add");
        createButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                TtlbUI.getCurrent().getUI().addWindow(new Window());
            }
        });
        buttonsLayout.addComponent(createButton);

        Button editButton = new Button("Edit");
        editButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                TtlbUI.getCurrent().getUI().addWindow(new Window());
            }
        });
        buttonsLayout.addComponent(editButton);
        return buttonsLayout;
    }

    private void buildMap(ArrayList<POI> pois, ArrayList<Route> routes) {
        googleMap = new GoogleMap(null,null,null);
        googleMap.setCenter(new LatLon(38.736946, -9.142685));
        googleMap.setZoom(10);
        googleMap.setSizeFull();
        googleMap.removeControl(GoogleMapControl.StreetView);

        googleMap.addMarkerClickListener(new MarkerClickListener() {
            @Override
            public void markerClicked(GoogleMapMarker clickedMarker) {
                poiGrid.getPoiGrid().select(poiMap.get(clickedMarker.getCaption()));
            }
        });

        if(pois != null && !pois.isEmpty()) {
            for (POI poi : pois) {
                googleMap.addMarker(poi.getName(),poi.getCoordinates(),false,null);
                poiMap.put(poi.getName(), poi);
            }
        }
    }

    private Component buildTitle() {
        Label title = new Label("Tuk Tuk Lisboa e Benfica");
        title.setHeight("30px");
        return title;
    }



}
