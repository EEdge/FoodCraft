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
    private static final String YUMMLY_ENDPOINT_DETAIL = "http://api.yummly.com/v1/api/recipe/";

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
                "&maxResult=50";

        Log.i("API_CALL", url);

        return url;
    }

    /**
     * Given a recipe ID, will return a formatted API call for getting Recipe Details.
     * @param recipeID the ID provided by Yummly for the recipe.
     * @return A formatted URL ready for Volley.
     */
    public static String formatYummlyDetailURL (String recipeID) {

        String url = YUMMLY_ENDPOINT_DETAIL +
                recipeID +
                "?_app_id=" + YUMMLY_ID +
                "&_app_key=" + YUMMLY_KEY;

        return url;

    }


    /**
     * Given the raw JSON response of search results from Yummly, will return a list of formatted
     * Recipe objects ready to be sent to the Results activity.
     * @param response a raw JSON blob of search results.
     * @return A list of Recipe objects.
     */
    public static List<Recipe> yummlyToRecipe(JSONObject response, List<String> ingredients) {

        JSONArray results;
        List<Recipe> recipeList = new ArrayList<>();

        try {

            results = response.getJSONArray("matches");

            for (int x = 0; x < results.length(); x++) {

                Recipe buildRecipe = new Recipe();
                JSONObject recipe = results.getJSONObject(x);

                buildRecipe.id = recipe.getString("id");
                buildRecipe.api = RecipeSearchRequest.YUMMLY_API;
                buildRecipe.name = recipe.getString("recipeName");

                buildRecipe.imageURL = recipe.getJSONObject("imageUrlsBySize").getString("90");

                /* Builds Ingredient List */
                JSONArray ingredientList = recipe.getJSONArray("ingredients");

                for (int y = 0; y < ingredientList.length(); y++) {
                    String ing = ingredientList.getString(y);
                    //The following if logic does the matching for each ingredient while we are parsing the JSON array
                    //This was added by Paul due to the lagging performance we were experiencing in the ResultsListActivity
                    if (ingredients.contains(ing.toLowerCase())) {
                        buildRecipe.matchedingredients++;
                    } else if (ingredients.contains(ing.toLowerCase().substring(0, ing.length()-1))) {
                        buildRecipe.matchedingredients++;
                    } else if (ingredients.contains(ing.toLowerCase().substring(0, ing.length()-2))) {
                        buildRecipe.matchedingredients++;
                    } else if (ingredients.contains(ing.toLowerCase().substring(0, ing.length()-3)+"y")) {
                        buildRecipe.matchedingredients++;
                    }
                    buildRecipe.ingredients.add(ing);
                }
                /* Retrieve Course */
                JSONObject attributes = recipe.getJSONObject("attributes");
                if (attributes.has("course")) {
                    JSONArray courseArray = attributes.getJSONArray("course");
                    if (courseArray.length() > 0) {
                        buildRecipe.course = courseArray.getString(0);
                    }
                }
                else {
                    buildRecipe.course = "Unknown";
                }

                /* Done */
                //Potentially discard entries with zero parsed matches using the below code
                /*
                if (buildRecipe.matchedingredients > 0) recipeList.add(buildRecipe);
                 */
                recipeList.add(buildRecipe);
            }

            return recipeList;

        } catch (JSONException e) {
            Log.i("yummlyToRecipe()", "Error.");
            e.printStackTrace();
        }

        return recipeList;
    }

    /**
     * Given a Yummly Recipe ID, will return a formatted RecipeDetail object.
     *
     * @param response A raw JSON recipe detail object.
     * @return A formatted RecipeDetail
     */

    public static RecipeDetail yummlyToDetail (JSONObject response) {

        RecipeDetail detail = new RecipeDetail();

        try {

            // The simple stuff
            detail.title = response.getString("name");
            detail.totalTime = response.getString("totalTime");
            detail.numberServings = response.getInt("numberOfServings");

            // Get image
            JSONObject images = response.getJSONArray("images").getJSONObject(0);
            detail.imageURL = images.getString("hostedLargeUrl");

            // Get ingredients
            JSONArray ingredientList = response.getJSONArray("ingredientLines");
            Log.i("YTD","6" + " Ingredient List Length:" + ingredientList.length());
            for (int x = 0; x < ingredientList.length(); x++) {

                String ing = ingredientList.getString(x);

                detail.ingredients.add(ing);
            }

            // Get nutritional info
            JSONArray ingredients = response.getJSONArray("nutritionEstimates");

            for (int x = 0; x < ingredients.length(); x++) {

                JSONObject element = ingredients.getJSONObject(x);

                // We only care about some of the metrics in the array
                switch (element.getString("attribute")) {

                    case "ENERC_KCAL":
                        detail.nutrition.put("calories",element.getString("value"));
                        break;

                    case "FAT":
                        detail.nutrition.put("fat",element.getString("value"));
                        break;

                    case "PROCNT":
                        detail.nutrition.put("protein",element.getString("value"));
                        break;

                    case "FIBTG":
                        detail.nutrition.put("fiber",element.getString("value"));
                        break;

                    case "SUGAR":
                        detail.nutrition.put("sugar",element.getString("value"));
                        break;

                    default:
                        break;

                }

            }

            return detail;

        } catch (JSONException e) {
            Log.i("yummlyToDetail()","Error.");
        }

       return detail;
    }

}
