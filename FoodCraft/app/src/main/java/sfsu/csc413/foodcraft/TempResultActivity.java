package sfsu.csc413.foodcraft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TempResultActivity extends AppCompatActivity {

    public final static String RECIPE_SEARCH_RESULTS = "sfsu.csc413.foodcraft.RECIPE_SEARCH_RESULTS";

    private List<Recipe> recipeList;
    private TextView resultsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_result);

        resultsDisplay = (TextView) findViewById(R.id.resultsDisplay);

        recipeList = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        recipeList =  (List<Recipe>) bundle.getSerializable(RECIPE_SEARCH_RESULTS);

        resultsDisplay.setText(recipeList.get(0).id);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temp_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
