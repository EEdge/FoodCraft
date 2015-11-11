package sfsu.csc413.foodcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        setContentView(R.layout.activity_temp_result);

        preferencesIngredients = SharedPreferences.getSharedPreferences();
        txt_title = (TextView)findViewById(R.id.textView);
        txt_ingredientsList = (TextView)findViewById(R.id.textView2);
        txt_servingSize = (TextView)findViewById(R.id.textView3);
        txt_totalTime = (TextView)findViewById(R.id.textView5);


        mRecipeDetail = new RecipeDetail();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mRecipeDetail = (RecipeDetail) bundle.getSerializable(RECIPE_DETAILS);

        txt_servingSize.setText(mRecipeDetail.numberServings);
        txt_totalTime.setText(mRecipeDetail.totalTime);


        StringBuilder builder = new StringBuilder();
        for (String details : mRecipeDetail.ingredients) {
            builder.append(details + "\n");
        }

        txt_ingredientsList.setText(builder.toString());
        }


}
