package sfsu.csc413.foodcraft;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GlossaryActivity extends AppCompatActivity {

    public static final String RECIPE_DETAILS = "sfsu.csc413.foodcraft.RECIPE_DETAILS";
    TextView txt_glossary;
    private RecipeDetail mRecipeDetail;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);
        txt_glossary = (TextView)findViewById(R.id.glossary_entry);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mRecipeDetail = (RecipeDetail) bundle.getSerializable(RECIPE_DETAILS);

        GlossarySearch mGlossarySearch = new GlossarySearch(context,mRecipeDetail);
        String search = GlossarySearch.ingredientGlossarySearchURL("banana");
        mGlossarySearch.requestGlossaryResponse(search);
        txt_glossary.setText((mGlossarySearch.requestGlossaryResponse(search)).toString());

        //txt_glossary.setText(Html.fromHtml(mGlossarySearch));
    }
}
