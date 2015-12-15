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
 *  The RecipeDetailRequest class is a controller class between the Search Results Activity and the
 *  Recipe Detail Activity. When initialized and given a Recipe object, the class will retrieve
 *  Recipe details from the relevant API, construct a RecipeDetail object, and launch the Recipe
 *  Detail Activity.
 *
 * @author: Brook Thomas
 * @version: 1.0
 */
public class RecipeDetailRequest {

    Context mContext;
    CardviewActivity mResultsList;


    RecipeDetailRequest(Context context, CardviewActivity detailsActivity) {
        mContext = context;
        mResultsList = detailsActivity;
    }

    /**
     * When given a Recipe object, will retrieve Recipe details from the API, construct a
     * RecipeDetail object, and launch the RecipeDetail Activity with the detail object in
     * the Bundle.
     *
     * @param recipe The Recipe object for which you want details.
     */
    public void run (Recipe recipe) {

        String url = YummlyHandler.formatYummlyDetailURL(recipe.id);

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
            // Yummly requires a custom ACCEPT header or it will return XML.
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

