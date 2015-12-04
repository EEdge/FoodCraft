package sfsu.csc413.foodcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< HEAD
import android.widget.ImageView;
=======
import android.view.View;
>>>>>>> glossarySearch
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE_DETAILS = "sfsu.csc413.foodcraft.RECIPE_DETAILS";
    TextView txt_ingredientsList, txt_servingSize, txt_totalTime, txt_title, txt_nutritionKey,txt_nutritionValue;
    NetworkImageView recipe_image;

    private Toolbar toolbar;
    private RecipeDetail mRecipeDetail;
    private ArrayList<String> preferencesIngredients;

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


        txt_ingredientsList = (TextView) findViewById(R.id.ingredient_list);
        txt_servingSize = (TextView) findViewById(R.id.serving_size);
        txt_totalTime = (TextView) findViewById(R.id.total_time);
        txt_title = (TextView) findViewById(R.id.recipe_name);
        txt_nutritionKey = (TextView) findViewById(R.id.nutrition_key);
        txt_nutritionValue = (TextView) findViewById(R.id.nutrition_value);
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
        txt_nutritionKey.setText(builder1.toString());
        txt_nutritionValue.setText(builder2.toString());

        //Parse image URL
        Uri myUri = Uri.parse(mRecipeDetail.imageURL);
        recipe_image.setImageURI(myUri);
        Log.i("IMAGE_DETAIL", mRecipeDetail.imageURL);

        StringBuilder builder3 = new StringBuilder();
        for (String details : mRecipeDetail.ingredients) {
            builder3.append(details + "\n");
        }

        txt_ingredientsList.setText(builder3.toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void glossarySearch(View view){
            Intent intent = new Intent(this, GlossaryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(RecipeDetailActivity.RECIPE_DETAILS, mRecipeDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
}