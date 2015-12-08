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
 * @version: 1.0
 */
public class RecipeSearchRequest {

    public static final int YUMMLY_API = 1;

    private List<String> ingredients;
    private ArrayList<Recipe> recipes;
    private Context context;
    private IngredientSearch searchActivity;
    private int desiredNumberOfRecipes = 50;

    /**
     * Constructor method that defines the new search.
     *
     * @param context        The application context.
     * @param searchActivity A reference to the Search activity. The results of the search will be returned
     *                       to this activity.
     */
    public RecipeSearchRequest(Context context, IngredientSearch searchActivity) {
        this.context = context;
        this.searchActivity = searchActivity;
    }

    /**
     * Initiates the search cycle.
     *
     * @param searchIngredients The list of ingredients from the Search Activity that will be sent to the API.
     */
    public void run(List<String> searchIngredients) {
        ingredients = searchIngredients;
        recipes = new ArrayList<>();

        searchCycle(ingredients);
    }

    /**
     * Given a list of ingredients, will search the Recipe APIs for ever decreasing subsets until
     * the target number of recipes is hit, or the subset size reaches zero. Provided at least one
     * Recipe is returned during this process, we will go ahead with the Search Results Activity.
     *
     * @param ingredients A list of ingredients.
     */
    private void searchCycle(final List<String> ingredients) {

        searchActivity.createToast("Searching...");

        // End of the line for any recursive search that gets this far
        if (ingredients.size() == 0) {

            if (recipes.size() > 0) {
                searchActivity.launchSearchResultsActivity(recipes);
                return;
            } else {
                searchActivity.createToast("No results found.");
                IngredientSearch.clearSelectedIngredientsList();
                return;
            }
        }

        String url = YummlyHandler.formatYummlySearchURL(ingredients);

        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            List<Recipe> results = YummlyHandler.yummlyToRecipe(response, ingredients);

                            recipes.addAll(results);

                            // Recursive search may also stop here if we reach 50 results
                            if (recipes.size() >= desiredNumberOfRecipes) {

                                searchActivity.launchSearchResultsActivity(recipes);
                                return;

                            } else {
                                Thread.sleep(1000); // lest we exhaust the API
                                ingredients.remove(ingredients.size() - 1);
                                searchCycle(ingredients);
                            }

                        } catch (Exception e) {
                            searchActivity.createToast("An error occurred. Try again.");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchActivity.createToast("Server Error. Please retry.");
            }
        }) {
            // Yummly requires a custom ACCEPT header or it will return XML
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
