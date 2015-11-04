package sfsu.csc413.foodcraft;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class YummlyHandler {

    private static final String YUMMLY_ID = "222ff589";
    private static final String YUMMLY_KEY = "2c26bf6069830ab90b07f6fa26cbef3b";
    private static final String YUMMLY_ENDPOINT_SEARCH = "http://api.yummly.com/v1/api/recipes";
    static final int api = 1;
    /**
     * Formats yummly search url from search string
     * @param searchText
     * @return url
     */
    public static String formatYummlySearchURL(String searchText) {

        String ingredientSearch[] = searchText.split(" ");
        String encodedIngredient = "";

        for (int i = 0; i < ingredientSearch.length; i++)
            encodedIngredient += "&allowedIngredient[]=" + ingredientSearch[i];

        String url = YUMMLY_ENDPOINT_SEARCH +
                "?_app_id=" + YUMMLY_ID +
                "&_app_key=" + YUMMLY_KEY +
                encodedIngredient +
                "&maxResult=20" +
                "&requirePictures=true";

        Log.i("API_CALL", url);

        return url;
    }

    /**
     * Converts JSON response to an array of Recipe objects.
     * API is defined as 1 for Recipe object.
     * @param response
     * @return recipeList
     */
    public static Recipe[] yummlytoRecipe(JSONObject response) {
        JSONArray results;
        Recipe[] recipeList = new Recipe[0];
        try {
            results = response.getJSONArray("matches");

            int length = results.length();
            recipeList = new Recipe[length + 1];

            for (int x = 0; x < length; x++) {
                JSONObject recipe = results.getJSONObject(x);
                String id = recipe.getString("id");
                recipeList[x] = new Recipe(api, id);
            }
            return recipeList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipeList;
    }


}
