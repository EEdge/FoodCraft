package sfsu.csc413.foodcraft;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static sfsu.csc413.foodcraft.YummlyHandler.yummlytoRecipe;

/**
 * This class requests JSONObject using the url string formatted by the handler.
 * Adds the request to volley request queue
 */
public class APIRecipeController {

    public static final int YUMMLY_API = 1;

    /**
     * requests JSONObject from formatted url and sends the response to be converted to an array of
     * Recipe
     * @param url
     * @param context
     */
public void recipeRequest(String url, final Context context) {
    /* Volley JsonRequest Object with Modified headers modified from examples at:
     * http://arnab.ch/blog/2013/08/asynchronous-http-requests-in-android-using-volley/
     */
    JsonObjectRequest req = new JsonObjectRequest(url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        yummlytoRecipe(response);

                    } catch (Exception e) {
                        Log.i("RECIPE_SEARCH", "Error parsing JSON");
                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("RECIPE_SEARCH", "Recipe Search Request Unsuccessful");
        }
    }) {

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("ACCEPT", "application/json");
            return headers;
        }
    };

    // Send request to Volley
    VolleyRequest.getInstance(context.getApplicationContext()).addToRequestQueue(req);


    }
}
