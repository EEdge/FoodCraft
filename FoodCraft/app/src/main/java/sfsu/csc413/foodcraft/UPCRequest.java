package sfsu.csc413.foodcraft;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the class that handles the calling of UPC API (Outpan) and Toasting the returned product name.
 * Written by Paul Klein
 */
public class UPCRequest {
    private String url;
    private String modifer;
    private String APIkey;

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
    public void craftUPCRequest(String upc_code, final Context context){
        final String requesturl = url + upc_code + "/" + modifer;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requesturl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.getString(modifer).equals("null")) {
                                send_toast(response.getString(modifer), context);
                            }
                            else {
                                send_toast("UPC Not Found!!", context);
                            }

                        } catch (Exception exception) {
                            VolleyLog.e("Error: ", exception.toString());
                            send_toast("Error!", context);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        send_toast("Error!", context);
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

    /**
     * A simple toast abstraction that reduces code size.
     * @param message
     * @param context
     * Written by Paul Klein
     */
    public void send_toast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
