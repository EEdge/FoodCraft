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

/**
 * This class manages the workflow between a user clicking a recipe in the Search Results activity
 * and the details of that recipe being loaded in Details Activity.
 *
 * @author: Brook Thomas
 * @version: 1.0
 */
public class RecipeDetailRequest {

    Context mContext;
    CardviewActivity mResultsList;


    RecipeDetailRequest(Context context, CardviewActivity detailsActivity) {
        // Volley always needs to know the UI context
        mContext = context;
        mResultsList = detailsActivity;
    }

    public void run(Recipe recipe) {

        String recipeID = recipe.id;

        String url = YummlyHandler.formatYummlyDetailURL(recipeID);

        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            RecipeDetail detail = YummlyHandler.yummlyToDetail(response);

                            mResultsList.launchDetailActivity(detail);


                        } catch (Exception e) {
                            Log.i("RecipeDetail.run()", "Error.");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("RECIPE_DETAIL", "Recipe Detail Request Unsuccessful");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("ACCEPT", "application/json");
                return headers;
            }
        };

        VolleyRequest.getInstance(mContext).addToRequestQueue(req);
    }
}

