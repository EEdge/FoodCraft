package sfsu.csc413.foodcraft;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Place class is used to hold data for Google Places API search results
 *
 * Created by evanedge on 11/27/15.
 */
public class Place {

    protected String name;
    protected String id;
    protected String address;
    protected String phone;
    protected JSONObject location;
    protected JSONObject coordinates;
    protected double lat;
    protected double lng;

}
