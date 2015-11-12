package sfsu.csc413.foodcraft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brook on 11/10/15.
 */
public class RecipeDetail implements Serializable {

    protected String title;
    protected String totalTime;
    protected int numberServings;
    protected String imageURL;
    protected List<String> ingredients;

    RecipeDetail () {
        ingredients = new ArrayList<>();
    }
}
