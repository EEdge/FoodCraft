package sfsu.csc413.foodcraft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static sfsu.csc413.foodcraft.YummlyHandler.formatYummlySearchURL;


/**
 * Search activity to test yummly api handler and controller
 */
public class YummlySearchActivity extends AppCompatActivity {

    private Button mSearchButton;
    private RecipeSearch searchRequest;
    private List<String> ingredients;
    private YummlySearchActivity selfReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yummly_search);

        mSearchButton = (Button) findViewById(R.id.searchButton);
        ingredients = new ArrayList<String>();
        selfReference = this;

        ingredients.add("bacon");
        ingredients.add("spinach");
        ingredients.add("black pepper");
        ingredients.add("olive oil");

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchRequest = new RecipeSearch(ingredients, getApplicationContext(), selfReference);

                searchRequest.run();

            }
        });
    }

    protected void launchSearchResultsActivity (ArrayList<Recipe> recipes) {

        Intent intent = new Intent(selfReference, TempResultActivity.class);

        Bundle bundle = new Bundle();

        bundle.putSerializable(TempResultActivity.RECIPE_SEARCH_RESULTS, recipes);

        intent.putExtras(bundle);

        startActivity(intent);

    }


}

