package sfsu.csc413.foodcraft;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class GlossaryActivity extends AppCompatActivity {

    public static final String RECIPE_DETAILS = "sfsu.csc413.foodcraft.RECIPE_DETAILS";
    TextView txt_glossary;
    RecipeDetail mRecipeDetail;
    GlossarySearch mGlossarySearch;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);
        txt_glossary = (TextView)findViewById(R.id.glossary_entry);
        txt_glossary.setMovementMethod(new ScrollingMovementMethod());

        //unpack
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mRecipeDetail = (RecipeDetail) bundle.getSerializable(RECIPE_DETAILS);
        String search = intent.getExtras().getString("search");

        mGlossarySearch = new GlossarySearch(context,mRecipeDetail, txt_glossary);

        mGlossarySearch.requestGlossaryResponse(search);

    }

}