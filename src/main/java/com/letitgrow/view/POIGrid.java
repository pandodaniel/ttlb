package com.letitgrow.view;

import com.letitgrow.TtlbUI;
import com.letitgrow.domain.POI;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;

public class POIGrid extends CustomComponent {
    private Grid<POI> poiGrid = new Grid<>();

    public POIGrid() {
        poiGrid.addColumn(POI::getName).setCaption("Name");
        poiGrid.addColumn(POI::getCoordinatesString).setCaption("Coords");
        poiGrid.addColumn(POI::getMovie).setCaption("Movie");
        poiGrid.setSizeFull();
        poiGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        loadPois();
    }

    private void loadPois() {
        ListDataProvider<POI> dataProvider = com.vaadin.data.provider.DataProvider
                .ofCollection(TtlbUI.getDataProvider()
                        .getPois());
        poiGrid.setDataProvider(dataProvider);
    }

    public Grid<POI> getPoiGrid() {
        return poiGrid;
    }
}
