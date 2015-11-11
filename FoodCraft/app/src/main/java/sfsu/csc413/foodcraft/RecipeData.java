package sfsu.csc413.foodcraft;

import java.util.ArrayList;
import java.util.List;

public class RecipeData {


  //public static String[] ResultsArray = {"Chicken Satay", "Peanut Butter Brownies", "Peanut Butter Cookies", "Peanut Sweet Potato Stew", "Peanut Butter Oat Bites", "Ginger Peanut Chicken Wrap", "Peanut Butter Ice Cream Cake", "Asian Sesame Noodles with Chicken" };

  public static ArrayList<Recipe> recipeList() {

      ArrayList<Recipe> list = new ArrayList<>();

      Recipe r1 = new Recipe();
      Recipe r2 = new Recipe();
      Recipe r3 = new Recipe();

      r1.id = "Recipe One";
      r2.id = "Recipe Two";
      r3.id = "Recipe Three";

      List<Recipe> recipeList2 = new ArrayList<>();
      recipeList2.add(r1);
      recipeList2.add(r2);
      recipeList2.add(r3);
      int i = 0;
      for (Recipe recipeobj : recipeList2) {
          recipeobj.name = recipeobj.id;
          recipeobj.imageURL = "generic";
          list.add(recipeobj);
          i++;
      }
      return (list);
        }

      /*for (int i = 0; i < ResultsArray.length; i++) {
      Recipe recipe = new Recipe();
      recipe.name = ResultsArray[i];
      recipe.imageName = "generic";

      list.add(recipe);
      }

      return (list);
      }*/

  public static Recipe getItem(String _id) {
    for (Recipe recipe : recipeList()) {
      if (recipe.id.equals(_id)) {
        return recipe;
      }
    }
    return null;
  }
}
