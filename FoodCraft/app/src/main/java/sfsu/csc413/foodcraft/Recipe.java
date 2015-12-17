package sfsu.csc413.foodcraft;


import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Recipe class is a container class for storing and transmitting recipe meta-data between activities.
 *
 * @author: Brook Thomas, Maria Lienkaemper
 * @version: 1.0
 */

public class Recipe implements Serializable, Comparable<Recipe>  {

    protected int api;
    protected String id;
    protected String name;
    protected String imageURL;
    protected List<String> ingredients;
    protected String course;
    protected int  matchedingredients = 0;
    Recipe () {
        ingredients = new ArrayList<>();
    }

    /**
     * Placeholder image loader for slow internet connections.
     *
     * @param context The application context.
     * @return The appropriate image resource ID.
     */
    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier("background_plate", "drawable", context.getPackageName());
    }

    /**
     * Enable comparisons for Recipe objects. Comparisons are based on the relative number of matched ingredients.
     *
     * @param other Another Recipe object.
     * @return  The result of the comparison.
     */
    public int compareTo(Recipe other) {
        return Integer.compare((this.ingredients.size() - this.matchedingredients), (other.ingredients.size() - other.matchedingredients));
    }
}

