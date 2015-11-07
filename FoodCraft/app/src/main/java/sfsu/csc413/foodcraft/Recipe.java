package sfsu.csc413.foodcraft;


import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Recipe {

    protected int api;
    protected String id;
    protected List<String> ingredients;

    Recipe(int api, String id) {
        this.api = api;
        this.id = id;
        this.ingredients = new ArrayList<>();
    }

    protected void addIngredient (String ingredient) {
        ingredients.add(ingredient);
    }

}
