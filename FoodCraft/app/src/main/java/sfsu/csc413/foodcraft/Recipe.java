package sfsu.csc413.foodcraft;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Recipe implements Serializable {

    protected int api;
    protected String id;
    protected List<String> ingredients;

    Recipe () {
        ingredients = new ArrayList<>();
    }



}
