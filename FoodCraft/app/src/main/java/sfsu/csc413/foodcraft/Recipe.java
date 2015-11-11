package sfsu.csc413.foodcraft;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Recipe class is a container class for transmitting essential information about
 * a recipe between activities.
 *
 * @author: Brook Thomas, Maria Lienkaemper
 * @version: 0.1
 */
public class Recipe implements Serializable {

    protected int api;
    protected String id;
    protected List<String> ingredients;

    Recipe () {
        ingredients = new ArrayList<>();
    }

}
