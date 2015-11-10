package sfsu.csc413.foodcraft;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class YummlyHandler {

    private static final String YUMMLY_ID = "222ff589";
    private static final String YUMMLY_KEY = "2c26bf6069830ab90b07f6fa26cbef3b";
    private static final String YUMMLY_ENDPOINT_SEARCH = "http://api.yummly.com/v1/api/recipes";

    /**
     * Given a list of ingredients, returns a formatted API search call for the Yummly API.
     * @param ingredients A String list of ingredients.
     * @return url A formatted URL ready for Volley.
     */
    public static String formatYummlySearchURL(List<String> ingredients) {

        String encodedIngredient = "";

        for (int i = 0; i < ingredients.size(); i++) {
            encodedIngredient += "&allowedIngredient[]=" + ingredients.get(i).replace(" ","%20");
        }

        String url = YUMMLY_ENDPOINT_SEARCH +
                "?_app_id=" + YUMMLY_ID +
                "&_app_key=" + YUMMLY_KEY +
                encodedIngredient +
                "&maxResult=10";

        Log.i("API_CALL", url);

        return url;
    }

    /**
     * Given the raw JSON response of search results from Yummly, will return a list of formatted
     * Recipe objects ready to be sent to the Results activity.
     * @param response a raw JSON blob of search results.
     * @return A list of Recipe objects.
     */
    public static List<Recipe> yummlyToRecipe(JSONObject response) {

        JSONArray results;
        List<Recipe> recipeList = new ArrayList<>();

        try {

            results = response.getJSONArray("matches");

            for (int x = 0; x < results.length(); x++) {

                Recipe buildRecipe = new Recipe();
                JSONObject recipe = results.getJSONObject(x);

                buildRecipe.id = recipe.getString("id");
                buildRecipe.api = RecipeSearch.YUMMLY_API;

                JSONArray ingredientList = recipe.getJSONArray("ingredients");

                for (int y = 0; y < ingredientList.length(); y++) {
                    String ing = ingredientList.getString(y);
                    buildRecipe.ingredients.add(ing);
                }

                recipeList.add(buildRecipe);
            }

            return recipeList;

        } catch (JSONException e) {
            Log.i("yummlyToRecipe()","Error.");
        }

        return recipeList;
    }

}
