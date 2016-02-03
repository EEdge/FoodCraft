package sfsu.csc413.foodcraft;

import android.content.Context;
import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the class that handles the calling of UPC API (Outpan) and Toasting the returned product name.
 * @file:UPCRequest.java
 * @author: Paul Klein
 * @version: 1.0
 */
public class UPCRequest {
    private static final String UPC_KEYWORD = "name";
    private static final String UPC_KEY = "XXXXXXXXXXXXX";
    private static final String UPC_URL = "https://api.outpan.com/v1/products/";
    private TaskCallback callback;
    private Context myActivity;

    /**
     * The constructor for the UPCRequest class that establishes our variables.
     * Written by Paul Klein
     */
    public UPCRequest(Context myActivity) {
        this.myActivity = myActivity;
    }

    /**
     * This method crafts the JSONObjectRequest for our UPC API and adds it to the VolleyRequest request queue.
     * Once there is a response to the request, it sends a toast of the product title.
     *
     * @param upc_code
     * @param context
     * @param callback, this callback is used to return the response to calling fragment asynchronously.
     */
    public void craftUPCRequest(final String upc_code, final Context context, TaskCallback callback) {
        final String requesturl = UPC_URL + upc_code + "/" + UPC_KEYWORD;
        this.callback = callback;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requesturl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.getString(UPC_KEYWORD).equals("null")) {
                                parse_response(response.getString(UPC_KEYWORD), upc_code);
                            } else {
                                parse_response("", upc_code);
                            }

                        } catch (Exception exception) {
                            parse_response("", upc_code);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        parse_response("", upc_code);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:", UPC_KEY);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        VolleyRequest.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    /**
     * Checks the database for a cached code. If found, the product title set to the title returned from the database and,
     * it returns a UPC object with the original code.
     * If the code is not found, it still returns a UPCObject, but it sets the product title as null.
     *
     * @param upc, a string containing the UPC code.
     * @return UPCObject, an object that encapsulates the product title, UPC code, and original scanned title
     */
    public UPCObject getCachedCode(String upc) {
        //get product name from database with matching UPC
        UPCDatabase database = new UPCDatabase(myActivity);
        String result = database.getProductByCode(upc);
        UPCObject obj = new UPCObject(upc, result, result);
        return obj;
    }

    /**
     * Inserts the UPC and title into the database. This stores the code for quick reference later, and keeps
     * the data persistent.
     *
     * @param upc
     * @param title
     * @param context, the context is necessary for the database
     */
    public void insertCachedCode(String upc, String title, Context context) {
        UPCDatabase database = new UPCDatabase(context);
        database.insert(upc, title);
    }

    /**
     * Parses the response of the original API request. Takes various combinations of the response from the Outpan API
     * and checks it against the IngredientList. When the request is complete, it uses the callback to return the response
     * to the original calling class.
     *
     * @param scanned_product
     * @param upc_code
     */
    public void parse_response(String scanned_product, String upc_code) {
        IngredientList list = new IngredientList();
        String[] split_text = scanned_product.split("\\s+");
        List<UPCObject> results = new ArrayList<UPCObject>();
        if (scanned_product.length() < 1) {
            //If the length is less than one, we probably won't
            results.add(new UPCObject(upc_code, null, scanned_product));
        } else {
            for (String part : split_text) {
                //This checks each individual part of the response against the ingredientList
                if (list.Contains(part.toLowerCase())) {
                    results.add(new UPCObject(upc_code, part.toLowerCase(), scanned_product));
                }
            }
            for (int i = 1; i <= split_text.length - 2; i++) {
                //This checks combinations of previous and post words against the ingredientList
                //For example, if the response contained "Happy Orange Juice" it would create the strings
                //"Happy Orange" and "Orange Juice"
                String previouscombo = split_text[i - 1] + " " + split_text[i];
                String postcombo = split_text[i] + " " + split_text[i + 1];
                if (list.Contains(previouscombo.toLowerCase())) {
                    results.add(new UPCObject(upc_code, previouscombo.toLowerCase(), scanned_product));
                }
                if (list.Contains(postcombo.toLowerCase())) {
                    results.add(new UPCObject(upc_code, postcombo.toLowerCase(), scanned_product));
                }
            }
            if (results.size() == 0) {
                results.add(new UPCObject(upc_code, null, scanned_product));
            }
        }
        callback.onTaskCompleted(results);
    }
}
