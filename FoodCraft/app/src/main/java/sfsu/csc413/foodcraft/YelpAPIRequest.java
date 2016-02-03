package sfsu.csc413.foodcraft;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * YelpAPIRequest class handles making the request to the Yelp API and parsing the results to send
 * back to MapsActivity.
 *
 * @author: Evan Edge
 */

public class YelpAPIRequest {

    private static String term;
    private static String ll;
    private static String location;
    private Context context;
    private TaskCallback callback;

    //API keys:

    private static final String CONSUMER_KEY = "XXXXXXXXXXXXX";
    private static final String CONSUMER_SECRET = "XXXXXXXXXXXXX";
    private static final String TOKEN = "XXXXXXXXXXXXX";
    private static final String TOKEN_SECRET = "XXXXXXXXXXXXX";

    //creating the request:

    public YelpAPIRequest(String term, String ll, Context context, TaskCallback callback) {

        this.callback = callback;
        YelpAPIRequest.term = term;
        YelpAPIRequest.ll = ll;
        this.context = context;
        location = "america";

    }

    public void makeRequest() {
        VolleyOAuthRequest request = new VolleyOAuthRequest(Request.Method.GET, "http://api.yelp.com/v2/search", new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }, CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);

        //creating request URL:

        request.addParameter("term", term);
        request.addParameter("ll", ll);
        //request.addParameter("location", location);
        String url = request.getUrl();
        Log.i("Yelp url:", url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseYelpResponse(response);//on response, parse response
                        } catch (Exception exception) {
                            VolleyLog.e("Parse catch: ", exception.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error : ", error.toString());
                        String body;
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        Log.i("volley statusCode:::", statusCode);
                        Toast toast = Toast.makeText(context, "Request error- please try again", Toast.LENGTH_LONG);
                        toast.show();
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse.data != null) {
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                                Log.i("volley network resp:::", body);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
        //Volley does retry for you if you have specified the policy.
        jsObjRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        VolleyRequest.getInstance(context).addToRequestQueue(jsObjRequest);
        Log.i("yelp response.:", "added request to queue in yelpAPIReq");
    }

    /**
     * This method parses the Yelp API response into Place objects and places them into
     * an ArrayList to be sent to MapsActivity
     *
     * @param response the response from the Yelp API call
     * @throws JSONException
     */
    public void parseYelpResponse(JSONObject response) throws JSONException {
        JSONArray JSONplaceArray = response.getJSONArray("businesses");
        ArrayList<Place> placeArray = new ArrayList<>();

        for (int i = 0; i < JSONplaceArray.length(); i++) {
            JSONObject object = JSONplaceArray.getJSONObject(i);

            Place place = new Place();
            if (object.has("name")) place.name = object.getString("name");
            if (object.has("id")) place.id = object.getString("id");
            if (object.has("location")) place.location = object.getJSONObject("location");
            if (place.location != null) place.address = place.location.getString("address");
            if (object.has("display_phone")) place.phone = object.getString("display_phone");
            if (place.location != null)
                place.coordinates = place.location.getJSONObject("coordinate");
            if (place.coordinates != null) {
                place.lat = place.coordinates.getDouble("latitude");
                place.lng = place.coordinates.getDouble("longitude");
            }
            placeArray.add(place);
        }
        callback.onTaskCompleted(placeArray);
    }


}
