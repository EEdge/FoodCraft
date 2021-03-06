package sfsu.csc413.foodcraft;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The YummlyHandler class is used to construct Yummly-specific API request strings, as well
 * as interpret Yummly API return data into the application's native Recipe and RecipeDetail objects.
 *
 * @author: Brook Thomas, Maria Lienkaemper
 * @version: 1.0
 */
public class YummlyHandler {

    private static final String YUMMLY_ID = "XXXXXXXXXXXXX";
    private static final String YUMMLY_KEY = "XXXXXXXXXXXXX";
    private static final String YUMMLY_ENDPOINT_SEARCH = "http://api.yummly.com/v1/api/recipes";
    private static final String YUMMLY_ENDPOINT_DETAIL = "http://api.yummly.com/v1/api/recipe/";

    /**
     * Given a list of ingredients, returns a formatted API search call for the Yummly API.
     *
     * @param ingredients A String list of ingredients.
     * @return url A formatted URL ready for Volley.
     */
    public static String formatYummlySearchURL(List<String> ingredients) {

        String encodedIngredient = "";

        for (int i = 0; i < ingredients.size(); i++) {
            encodedIngredient += "&allowedIngredient[]=" + ingredients.get(i).replace(" ", "%20");
        }

        String url = YUMMLY_ENDPOINT_SEARCH +
                "?_app_id=" + YUMMLY_ID +
                "&_app_key=" + YUMMLY_KEY +
                encodedIngredient +
                "&maxResult=50" +
                "&requirePictures=true";

        Log.i("API_CALL", url);

        return url;
    }

    /**
     * Given a recipe ID, will return a formatted API call for getting Recipe Details.
     *
     * @param recipeID the ID provided by Yummly for the recipe.
     * @return A formatted URL ready for Volley.
     */
    public static String formatYummlyDetailURL(String recipeID) {

        String url = YUMMLY_ENDPOINT_DETAIL +
                recipeID +
                "?_app_id=" + YUMMLY_ID +
                "&_app_key=" + YUMMLY_KEY;

        return url;

    }


    /**
     * Given the raw JSON response of search results from Yummly, will return a list of formatted
     * Recipe objects ready to be sent to the Results activity.
     *
     * @param response a raw JSON blob of search results.
     * @return A list of Recipe objects.
     */
    public static List<Recipe> yummlyToRecipe(JSONObject response, List<String> ingredients) throws Exception {

        JSONArray results;
        List<Recipe> recipeList = new ArrayList<>();

        results = response.getJSONArray("matches");

        for (int x = 0; x < results.length(); x++) {

            Recipe buildRecipe = new Recipe();
            JSONObject recipe = results.getJSONObject(x);

            buildRecipe.id = recipe.getString("id");
            buildRecipe.api = RecipeSearchRequest.YUMMLY_API;
            buildRecipe.name = recipe.getString("recipeName");

            String small_image = recipe.getJSONObject("imageUrlsBySize").getString("90");
            String choppedImage = small_image.substring(0, small_image.length() - 4);
            buildRecipe.imageURL = choppedImage + "360";

                /* Builds Ingredient List */
            JSONArray ingredientList = recipe.getJSONArray("ingredients");

            for (int y = 0; y < ingredientList.length(); y++) {
                String ing = ingredientList.getString(y);
                //The following if logic does the matching for each ingredient while we are parsing the JSON array
                //This was added by Paul due to the lagging performance we were experiencing in the activity_cardview
                if (ingredients.contains(ing.toLowerCase())) {
                    buildRecipe.matchedingredients++;
                } else if (ingredients.contains(ing.toLowerCase().substring(0, ing.length() - 1))) {
                    buildRecipe.matchedingredients++;
                } else if (ingredients.contains(Utilities.cleanString(ing))) {
                    buildRecipe.matchedingredients++;
                } else if (ingredients.contains(ing.toLowerCase().substring(0, ing.length() - 2))) {
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
            } else {
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

    }

    /**
     * Given a Yummly Recipe ID, will return a formatted RecipeDetail object.
     *
     * @param response A raw JSON recipe detail object.
     * @return A formatted RecipeDetail
     */

    public static RecipeDetail yummlyToDetail(JSONObject response) {

        RecipeDetail detail = new RecipeDetail();

        try {

            // The simple stuff
            detail.title = response.getString("name");
            detail.totalTime = response.getString("totalTime");
            detail.numberServings = response.getInt("numberOfServings");

            // Get image
            JSONObject images = response.getJSONArray("images").getJSONObject(0);
            detail.imageURL = images.getString("hostedLargeUrl");

            // Get recipe URL
            detail.recipeURL = response.getJSONObject("source").getString("sourceRecipeUrl");
            Log.i("RECIPE_URL", detail.recipeURL);

            // Get ingredients
            JSONArray ingredientList = response.getJSONArray("ingredientLines");
            Log.i("YTD", "6" + " Ingredient List Length:" + ingredientList.length());
            for (int x = 0; x < ingredientList.length(); x++) {

                String ing = ingredientList.getString(x);

                detail.ingredients.add(ing);
            }

            // Get nutritional info
            JSONArray ingredients = response.getJSONArray("nutritionEstimates");

            for (int x = 0; x < ingredients.length(); x++) {

                JSONObject element = ingredients.getJSONObject(x);

                // Floor value
                String elementValue = element.getString("value");
                int elementDecimalLocation = elementValue.indexOf(".");

                // We only care about some of the metrics in the array
                switch (element.getString("attribute")) {

                    case "ENERC_KCAL":
                        detail.nutrition.put("calories", elementValue.substring(0, elementDecimalLocation) + " calories");
                        break;

                    case "FAT":
                        detail.nutrition.put("fat", elementValue.substring(0, elementDecimalLocation) + " grams");
                        break;

                    case "PROCNT":
                        detail.nutrition.put("protein", elementValue.substring(0, elementDecimalLocation) + " grams");
                        break;

                    case "FIBTG":
                        detail.nutrition.put("fiber", elementValue.substring(0, elementDecimalLocation) + " grams");
                        break;

                    case "SUGAR":
                        detail.nutrition.put("sugar", elementValue.substring(0, elementDecimalLocation) + " grams");
                        break;

                    default:
                        break;

                }

            }

            return detail;

        } catch (JSONException e) {
            Log.i("yummlyToDetail()", "Error.");
        }

        return detail;
    }

}
