package sfsu.csc413.foodcraft;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The RecipeDetailActivity class displays the recipe details received from the RecipeDetail class.
 * The RecipeDetailActivity class receives a bundle of Recipe details from the RecipeDetail class,
 * unpacks the bundle and displays the details.
 *
 * @author: Sapan Tiwari
 * @version: 1.0
 */

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE_DETAILS = "sfsu.csc413.foodcraft.RECIPE_DETAILS";
    TextView txt_servingSize, txt_totalTime, txt_title, txt_nutritionKey, txt_nutritionValue;
    ListView txt_ingredientsList;
    NetworkImageView recipe_image;
    Button btn_viewRecipe;

    private Menu menu;
    private Toolbar toolbar;
    private RecipeDetail mRecipeDetail;
    private ArrayList<String> preferencesIngredients;
    GlossarySearch mGlossarySearch;
    RecipeDetailActivity selfReference;
    String idGlossary;
    Context context = this;

    /**
     * This method is called when the activity is first created.
     *
     * @param savedInstanceState Information about the current state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setActionBar(toolbar);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(true);

            // https://stackoverflow.com/questions/16240605/change-action-bar-title-color -- Brilliant!
            getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Recipe Details</font>"));

            getActionBar().setElevation(7);
        }
        selfReference = this;

        //Cast TextViews and Buttons.
        txt_ingredientsList = (ListView) findViewById(R.id.ingredient_list);
        txt_servingSize = (TextView) findViewById(R.id.serving_size);
        txt_totalTime = (TextView) findViewById(R.id.total_time);
        txt_title = (TextView) findViewById(R.id.recipe_name);
        txt_nutritionKey = (TextView) findViewById(R.id.nutrition_key);
        txt_nutritionValue = (TextView) findViewById(R.id.nutrition_value);
        btn_viewRecipe = (Button) findViewById(R.id.view_recipe);

        //Unpack the bundle received from RecipeDetail class.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mRecipeDetail = (RecipeDetail) bundle.getSerializable(RECIPE_DETAILS);

        //Load the Recipe image from the RecipeDetail bundle.
        recipe_image = (NetworkImageView) findViewById(R.id.recipe_image);
        ImageLoader mImageLoader = VolleyRequest.getInstance(this).getImageLoader();
        recipe_image.setImageUrl(mRecipeDetail.imageURL, mImageLoader);


        //The below are quick catches in case the selected recipe is missing some information.
        String title = mRecipeDetail.title;
        if (title == null){
            title = "A delicious recipe!";
        }
        txt_title.setText(title);

        String servingSize = Integer.toString(mRecipeDetail.numberServings);
        if(servingSize == null){
            servingSize = "Serving size not available.";
        }
        txt_servingSize.setText(servingSize);

        String time = mRecipeDetail.totalTime;
        if(time == null){
            time = "Cook time not available.";
        }
        txt_totalTime.setText(time);

        //Display all the nutrition info from the RecipeDetail bundle.
        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();
        for (Map.Entry<String, String> entry : mRecipeDetail.nutrition.entrySet()) {
            String key = entry.getKey();
            key = Character.toString(key.charAt(0)).toUpperCase() + key.substring(1);
            String value = entry.getValue();
            value = Character.toString(value.charAt(0)).toUpperCase() + value.substring(1);
            builder1.append(key + "\n");
            builder2.append(value + "\n");
        }
        if (builder2.toString() == "") {
            txt_nutritionKey.setText("Nutritional information is not available.");
            txt_nutritionValue.setText("");
        } else {
            txt_nutritionKey.setText(builder1.toString());
            txt_nutritionValue.setText(builder2.toString());
        }

        //create listview and populate with list of ingredients
        List<String> ingredientList = new ArrayList<String>();
        for (int i = 0; i < mRecipeDetail.ingredients.size(); i++) {
            ingredientList.add(mRecipeDetail.ingredients.get(i));
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, ingredientList);
        txt_ingredientsList.setAdapter(adapter);
        //make sure that activity starts at top not at list
        txt_ingredientsList.setFocusable(false);

        //onClickListener for launching the glossary activity
        txt_ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //This method allows the user to see the information about a particular ingredient by clicking on it.
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mGlossarySearch = new GlossarySearch(getApplicationContext(), mRecipeDetail, taskCallback);
                mGlossarySearch.startGlossarySearch(mRecipeDetail, position);
        }
        });

    }

    /**
     * This method launches the ViewRecipe activity when the user clicks on the View Recipe button.
     * @param view Called when a view has been clicked.
     */
    public void viewRecipe(View view) {

        Intent intent = new Intent(this, ViewRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putString(ViewRecipe.RECIPE_URL, mRecipeDetail.recipeURL);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    TaskCallback taskCallback = new TaskCallback() {
        public void onTaskCompleted(UPCObject result, boolean cached) {
        }
        public void onTaskCompleted(String text){
            Intent intent = new Intent(getApplicationContext(), GlossaryActivity.class);
            Bundle bundle = new Bundle();
            bundle.getString(text);
            intent.putExtra("entry", text);
            intent.putExtras(bundle);

            selfReference.startActivity(intent);
        }
        public void onTaskCompleted(Place holder) {
        }
        public void onTaskCompleted(ArrayList<Place> result){
        }
    };

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shared_preferences, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(this, sfsu.csc413.foodcraft.MapsActivity.class);
        startActivity(intent);
        return true;
    }


}
