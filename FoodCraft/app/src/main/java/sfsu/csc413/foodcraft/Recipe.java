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

    Recipe () {}

    Recipe(int api, String id) {
        this.api = api;
        this.id = id;
        this.ingredients = new ArrayList<>();
    }

    /**
     * Adds a new ingredients to the Recipe. Note: no duplicate protection.
     * @param ingredient An ingredient.
     */
    protected void addIngredient (String ingredient) {
        ingredients.add(ingredient);
    }

}
