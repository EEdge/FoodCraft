package sfsu.csc413.foodcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.String;
import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE_DETAILS = "sfsu.csc413.foodcraft.RECIPE_DETAILS";
    TextView txt_ingredientsList, txt_servingSize, txt_totalTime, txt_title;

    private RecipeDetail mRecipeDetail;
    private ArrayList<String> preferencesIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        txt_ingredientsList = (TextView)findViewById(R.id.ingredient_list);
        txt_servingSize = (TextView)findViewById(R.id.serving_size);
        txt_totalTime = (TextView)findViewById(R.id.total_time);
        txt_title = (TextView)findViewById(R.id.recipe_name);
        //preferencesIngredients = SharedPreferences.getSharedPreferences();

        /* Unpack */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mRecipeDetail = (RecipeDetail) bundle.getSerializable(RECIPE_DETAILS);

        txt_servingSize.setText(Integer.toString(5667));

        txt_totalTime.setText(mRecipeDetail.totalTime);


        StringBuilder builder = new StringBuilder();
        for (String details : mRecipeDetail.ingredients) {
            builder.append(details + "\n");
        }

        txt_ingredientsList.setText(builder.toString());
        }


    public void glossarySearch(View view){
            Intent intent = new Intent(this, GlossaryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(RecipeDetailActivity.RECIPE_DETAILS, mRecipeDetail);
            intent.putExtras(bundle);
            startActivity(intent);
        }
}
