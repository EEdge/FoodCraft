package sfsu.csc413.foodcraft;

import org.json.JSONObject;

/**
 * Place class is used to hold data for Google Places API search results
 *
 * @author: Evan Edge
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
