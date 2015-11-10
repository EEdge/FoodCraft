package sfsu.csc413.foodcraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


public class RecipeSearch {

    public static final int YUMMLY_API = 1;

    private List<String> ingredients;
    private ArrayList<Recipe> recipes;
    private Context context;
    private YummlySearchActivity searchActivity;
    private int desiredNumberOfRecipes = 20;

    public RecipeSearch (List<String> ingredients, Context context, YummlySearchActivity searchActivity)  {
        recipes = new ArrayList<>();
        this.ingredients = ingredients;
        this.context = context;
        this.searchActivity = searchActivity;
    }

    public void run () {
        searchCycle(ingredients);
    }

    private void searchCycle (final List<String> ingredients) {

        if (ingredients.size()==0) {
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
                                Log.i("XXX","Attempting to call searchActivity methods");
                                searchActivity.launchSearchResultsActivity(recipes);

                            } else {
                                Log.i("ON_RESPONSE","Size insufficent");
                                searchCycle(Utilities.getRandomSubset(ingredients, ingredients.size()-1));
                            }


                        } catch (Exception e) {
                            Log.i("RECIPE_SEARCH", "Error parsing JSON");
                            e.printStackTrace();
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
