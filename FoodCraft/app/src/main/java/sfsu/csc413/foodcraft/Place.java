package sfsu.csc413.foodcraft;

import org.json.JSONObject;

/**
 * Place class is used to hold data for Google Places API search results
 * <p/>
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
