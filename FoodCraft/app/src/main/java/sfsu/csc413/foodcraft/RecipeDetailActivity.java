package sfsu.csc413.foodcraft;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE_DETAILS = "sfsu.csc413.foodcraft.RECIPE_DETAILS";
    TextView  txt_servingSize, txt_totalTime, txt_title, txt_nutritionKey,txt_nutritionValue;
    ListView txt_ingredientsList;
    NetworkImageView recipe_image;
    Button btn_viewRecipe;

    private Menu menu;
    private Toolbar toolbar;
    private RecipeDetail mRecipeDetail;
    private ArrayList<String> preferencesIngredients;
    GlossarySearch mGlossarySearch;

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


        txt_ingredientsList = (ListView) findViewById(R.id.ingredient_list);
        txt_servingSize = (TextView) findViewById(R.id.serving_size);
        txt_totalTime = (TextView) findViewById(R.id.total_time);
        txt_title = (TextView) findViewById(R.id.recipe_name);
        txt_nutritionKey = (TextView) findViewById(R.id.nutrition_key);
        txt_nutritionValue = (TextView) findViewById(R.id.nutrition_value);
        btn_viewRecipe = (Button) findViewById(R.id.view_recipe);
        //preferencesIngredients = SharedPreferences.getSharedPreferences();

        /* Unpack */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mRecipeDetail = (RecipeDetail) bundle.getSerializable(RECIPE_DETAILS);

        recipe_image = (NetworkImageView) findViewById(R.id.recipe_image);
        ImageLoader mImageLoader = VolleyRequest.getInstance(this).getImageLoader();
        recipe_image.setImageUrl(mRecipeDetail.imageURL, mImageLoader);

        txt_title.setText(mRecipeDetail.title);

        txt_servingSize.setText(Integer.toString(mRecipeDetail.numberServings));

        txt_totalTime.setText(mRecipeDetail.totalTime);

        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();

        for (Map.Entry<String, String> entry : mRecipeDetail.nutrition.entrySet()) {
            String key = entry.getKey();
            key = Character.toString(key.charAt(0)).toUpperCase()+key.substring(1);
            String value = entry.getValue();
            value = Character.toString(value.charAt(0)).toUpperCase()+value.substring(1);
            builder1.append(key + "\n");
            builder2.append(value + "\n");
        }

        if (builder2.toString()==""){
            txt_nutritionKey.setText("Nutritional information is not available.");
            txt_nutritionValue.setText("");
        }
        else {
            txt_nutritionKey.setText(builder1.toString());
            txt_nutritionValue.setText(builder2.toString());
        }

        //Parse image URL
        Uri myUri = Uri.parse(mRecipeDetail.imageURL);
        recipe_image.setImageURI(myUri);
        Log.i("IMAGE_DETAIL", mRecipeDetail.imageURL);


        //create listview and populate with list of ingredients
        List<String> ingredientList = new ArrayList<String>();
        for (int i = 0; i < mRecipeDetail.ingredients.size(); i++ ) {
            ingredientList.add(mRecipeDetail.ingredients.get(i));
        }

         final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, ingredientList);
        txt_ingredientsList.setAdapter(adapter);
        //make sure that activity starts at top not at list
        txt_ingredientsList.setFocusable(false);

        /**
         * onClickListener for
         */
        txt_ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String search = GlossarySearch.searchIngredient(mRecipeDetail, position);

                Intent intent = new Intent(view.getContext(), GlossaryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RecipeDetailActivity.RECIPE_DETAILS, mRecipeDetail);
                intent.putExtras(bundle);
                intent.putExtra("search", search);
                startActivity(intent);

        }
        });

    }


    public void viewRecipe (View view) {

        Intent intent = new Intent(this, ViewRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putString(ViewRecipe.RECIPE_URL, mRecipeDetail.recipeURL);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shared_preferences, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(this, sfsu.csc413.foodcraft.SharedPreferences.class);
        startActivity(intent);
        return true;
    }


}
