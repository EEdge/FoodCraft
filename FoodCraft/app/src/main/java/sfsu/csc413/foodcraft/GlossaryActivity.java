package sfsu.csc413.foodcraft;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class GlossaryActivity extends AppCompatActivity {

    public static final String RECIPE_DETAILS = "sfsu.csc413.foodcraft.RECIPE_DETAILS";
    TextView txt_glossary;
    RecipeDetail mRecipeDetail;
    GlossarySearch mGlossarySearch;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);
        txt_glossary = (TextView)findViewById(R.id.glossary_entry);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mRecipeDetail = (RecipeDetail) bundle.getSerializable(RECIPE_DETAILS);

        mGlossarySearch = new GlossarySearch(context,mRecipeDetail, txt_glossary);

        //Clean ingredient search string
        //String cleanedIngredient = Utilities.cleanString(mRecipeDetail.ingredients.get(0));
        //String search = GlossarySearch.ingredientGlossarySearchURL(cleanedIngredient);
        String search = GlossarySearch.ingredientGlossarySearchURL("avocado");

        mGlossarySearch.requestGlossaryResponse(search, mRecipeDetail);

        //txt_glossary.setText(Html.fromHtml(String.valueOf(mGlossarySearch.parsedEntry)));

    }
}