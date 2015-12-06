package sfsu.csc413.foodcraft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brook on 11/10/15.
 */
public class RecipeDetail implements Serializable {

    protected String title;
    protected String totalTime;
    protected int numberServings;
    protected String imageURL;
    protected String recipeURL;
    protected List<String> ingredients;
    protected Map<String,String> nutrition;

    RecipeDetail () {
        ingredients = new ArrayList<>();
        nutrition = new HashMap<>();
    }
}


