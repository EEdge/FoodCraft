package sfsu.csc413.foodcraft;

import android.content.Context;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONObject;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecipeSearchRequest is a core class that intermediates between the Search activity and the Search Results
 * activity. An instance of the RecipeSearchRequest class should be considered 'one time use', and every
 * new search should begin with instantiating a new instance.
 *
 * @author: Maria Lienkaemper, Brook Thomas
 * @version: 0.1
 *
 */
public class RecipeSearchRequest {

    public static final int YUMMLY_API = 1;

    private List<String> ingredients;
    private ArrayList<Recipe> recipes;
    private Context context;
    private YummlySearchActivity searchActivity;
    private int desiredNumberOfRecipes = 20;

    /**
     * Constructor method that defines the new search.
     *
     * @param context The application context.
     * @param searchActivity A reference to the Search activity. The results of the search will be returned
     *                       to this activity.
     */
    public RecipeSearchRequest(Context context, YummlySearchActivity searchActivity)  {
        this.context = context;
        this.searchActivity = searchActivity;
    }

    public void run (List<String> ingredients) {
        this.ingredients = ingredients;
        recipes = new ArrayList<>();

        searchCycle(ingredients);
    }

    /**
     * Given a list of ingredients, will search the Recipe APIs for ever decreasing subsets until
     * the target number of recipes is hit, or the subset size reaches zero.
     *
     * @param ingredients A list of ingredients.
     */
    private void searchCycle (final List<String> ingredients) {

        if (ingredients.size()==0) {
            // we can call a newSearch method on the searchActivity from here
            // should check if we have any results at all, can potentially roll with just a few
            return;
        }

        String url = YummlyHandler.formatYummlySearchURL(ingredients);

        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            List<Recipe> results = YummlyHandler.yummlyToRecipe(response);

                            recipes.addAll(results);

                            if (recipes.size() >= desiredNumberOfRecipes) {

                                searchActivity.launchSearchResultsActivity(recipes);

                            } else {
                                searchCycle(Utilities.getRandomSubset(ingredients, ingredients.size()-1));
                            }

                        } catch (Exception e) {
                            Log.i("searchCycle()", "Error.");
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

        VolleyRequest.getInstance(context).addToRequestQueue(req);
    }

}
