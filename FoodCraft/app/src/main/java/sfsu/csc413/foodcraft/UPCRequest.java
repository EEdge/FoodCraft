package sfsu.csc413.foodcraft;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the class that handles the calling of UPC API (Outpan) and Toasting the returned product name.
 * Written by Paul Klein
 */
public class UPCRequest {
    private String url;
    private String modifer;
    private String APIkey;
    private TaskCallback callback;

    /**
     * The constructor for the UPCRequest class that establishes our variables.
     * @param url The API url
     * @param modifier The API modifier that specifies the field we're looking for
     * @param APIkey Our API key which is used in the header of the request
     * Written by Paul Klein
     */
    public UPCRequest(String url, String modifier, String APIkey){
        this.url = url;
        this.modifer = modifier;
        this.APIkey = APIkey;
    }

    /**
     * This method crafts the JSONObjectRequest for our UPC API and adds it to the VolleyRequest request queue.
     * Once there is a response to the request, it sends a toast of the product title.
     * @param upc_code
     * @param context
     * Written by Paul Klein
     *  TODO: Parse Item for Ingredients, and return that ingredient as a String instead of a toast.
     */
    public void craftUPCRequest(String upc_code, final Context context, TaskCallback callback){
        final String requesturl = url + upc_code + "/" + modifer;
        this.callback = callback;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requesturl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.getString(modifer).equals("null")) {
                                parse_response(response.getString(modifer));
                            }
                            else {
                                Toast.makeText(context, "Code Not Found!", Toast.LENGTH_LONG).show();
                                parse_response("");
                            }

                        } catch (Exception exception) {
                            VolleyLog.e("Error: ", exception.toString());
                            Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:", APIkey);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        VolleyRequest.getInstance(context).addToRequestQueue(jsObjRequest);
    }
    public void parse_response(String text){
        IngredientList list = new IngredientList();
        String[] split_text = text.split("\\s+");
        List<String> results = new ArrayList<>();
        if (text.length() < 1){
            callback.onTaskCompleted(results);
        }
        else {
            for (String part: split_text) {
                if (list.Contains(part.toLowerCase())){
                    results.add(part.toLowerCase());
                }
            }
            if (results.size() == 0){
                for (String part: split_text) {
                    for (String secondpart : split_text) {
                        String combo = part + " " + secondpart;
                        if (list.Contains(combo.toLowerCase())){
                            results.add(combo.toLowerCase());
                        }
                    }
                }
            }
            callback.onTaskCompleted(results);
        }
    }
}

