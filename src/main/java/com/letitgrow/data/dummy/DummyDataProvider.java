package com.letitgrow.data.dummy;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.letitgrow.data.DataProvider;
import com.letitgrow.domain.*;
import com.vaadin.server.VaadinRequest;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.util.CurrentInstance;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A dummy implementation for the backend API.
 */
public class DummyDataProvider implements DataProvider {

    // TODO: Get API key from http://developer.rottentomatoes.com
    private static final String ROTTEN_TOMATOES_API_KEY = null;

    /* List of countries and cities for them */
    private static Multimap<String, String> countryToCities;
    private static Date lastDataUpdate;

    /**
     * Initialize the data for this application.
     */
    public DummyDataProvider() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        if (lastDataUpdate == null || lastDataUpdate.before(cal.getTime())) {
            lastDataUpdate = new Date();
        }
    }

    /* JSON utility method */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /* JSON utility method */
    private static JsonObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JsonElement jelement = new JsonParser().parse(jsonText);
            JsonObject jobject = jelement.getAsJsonObject();
            return jobject;
        } finally {
            is.close();
        }
    }

    /* JSON utility method */
    private static JsonObject readJsonFromFile(File path) throws IOException {
        BufferedReader rd = new BufferedReader(new FileReader(path));
        String jsonText = readAll(rd);
        JsonElement jelement = new JsonParser().parse(jsonText);
        JsonObject jobject = jelement.getAsJsonObject();
        return jobject;
    }

    /**
     * =========================================================================
     * Countries, cities, theaters and rooms
     * =========================================================================
     */

    static List<String> theaters = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("Threater 1");
            add("Threater 2");
            add("Threater 3");
            add("Threater 4");
            add("Threater 5");
            add("Threater 6");
        }
    };

    static List<String> rooms = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("Room 1");
            add("Room 2");
            add("Room 3");
            add("Room 4");
            add("Room 5");
            add("Room 6");
        }
    };

    /**
     * Parse the list of countries and cities
     */
    private static Multimap<String, String> loadTheaterData() {

        /* First, read the text file into a string */
        StringBuffer fileData = new StringBuffer(2000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                DummyDataProvider.class.getResourceAsStream("cities.txt")));

        char[] buf = new char[1024];
        int numRead = 0;
        try {
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String list = fileData.toString();

        /*
         * The list has rows with tab delimited values. We want the second (city
         * name) and last (country name) values, and build a Map from that.
         */
        Multimap<String, String> countryToCities = MultimapBuilder.hashKeys()
                .arrayListValues().build();
        for (String line : list.split("\n")) {
            String[] tabs = line.split("\t");
            String city = tabs[1];
            String country = tabs[tabs.length - 2];

            if (!countryToCities.containsKey(country)) {
                countryToCities.putAll(country, new ArrayList<String>());
            }
            countryToCities.get(country).add(city);
        }

        return countryToCities;

    }

    @Override
    public User authenticate(String userName, String password) {
        if("admin".equals(userName) && "admin".equals(password)) {
            User user = new User();
            user.setFirstName(DummyDataGenerator.randomFirstName());
            user.setLastName(DummyDataGenerator.randomLastName());
            user.setRole("admin");
            String email = user.getFirstName().toLowerCase() + "."
                    + user.getLastName().toLowerCase() + "@"
                    + DummyDataGenerator.randomCompanyName().toLowerCase() + ".com";
            user.setEmail(email.replaceAll(" ", ""));
            user.setLocation(DummyDataGenerator.randomWord(5, true));
            user.setBio("Quis aute iure reprehenderit in voluptate velit esse."
                    + "Cras mattis iudicium purus sit amet fermentum.");
            return user;
        }
        return null;
    }

    @Override
    public ArrayList<POI> getPois() {
            ArrayList<POI> pois = new ArrayList<POI>();
            POI poi1 = new POI();

            poi1.setName("Estádio da Luz");
            poi1.setLanguage("PT");
            poi1.setCoordinates(new LatLon(38.752711,-9.1869627));
            poi1.setMovie("estadiodaluz.mpg");
            pois.add(poi1);

            POI poi2 = new POI();

            poi2.setName("Torre de Belém");
            poi2.setCoordinates(new LatLon(38.6915837,-9.218166));
            poi2.setLanguage("PT");
            poi2.setMovie("torredebelem.mpg");

            pois.add(poi2);

            POI poi3 = new POI();

            poi3.setName("Panteão");
            poi3.setCoordinates(new LatLon(38.7149796,-9.1268735));
            poi3.setLanguage("PT");
            poi3.setMovie("panteao.mpg");

            pois.add(poi3);

            POI poi4 = new POI();

            poi4.setName("Amoreiras");
            poi4.setLanguage("PT");
            poi4.setCoordinates(new LatLon(38.723122,-9.1637355));
            poi4.setMovie("amoreiras.mpg");

            pois.add(poi4);

            return pois;
    }

}
