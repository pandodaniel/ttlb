package com.letitgrow.data;

import com.letitgrow.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * QuickTickets Dashboard backend API.
 */
public interface DataProvider {
    /**
     * @param userName
     * @param password
     * @return Authenticated used.
     */
    User authenticate(String userName, String password);

    ArrayList<POI> getPois();
}
