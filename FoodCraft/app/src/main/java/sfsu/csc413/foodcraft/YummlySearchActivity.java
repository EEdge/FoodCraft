package sfsu.csc413.foodcraft;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static sfsu.csc413.foodcraft.YummlyHandler.formatYummlySearchURL;


/**
 * Search activity to test yummly api handler and controller
 */
public class YummlySearchActivity extends AppCompatActivity {

    private EditText mSearchText;
    private Button mSearchButton;
    private TextView mResultText;
    ArrayAdapter<String> adapter;
    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yummly_search);
        final Context context;
        context = getApplicationContext();


        mSearchText = (EditText) findViewById(R.id.searchQuery);
        mSearchButton = (Button) findViewById(R.id.searchButton);
        mResultText = (TextView) findViewById(R.id.searchResultDisplay);

        //create listview
        //ListView listView = (ListView) findViewById(R.id.listv);
        //items = new ArrayList<>();
        //adapter = new ArrayAdapter(this, R.layout.item_layout, R.id.title, items);
        //listView.setAdapter(adapter);


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = formatYummlySearchURL(mSearchText.getText().toString());

                //refresh listview
                //adapter.clear();
                //adapter.notifyDataSetChanged();

                //Check if search field is empty
                if (TextUtils.isEmpty(mSearchText.getText())) {
                    return;
                }

                APIRecipeController request = new APIRecipeController();
                request.recipeRequest(url, context);

            }
        });
        }
    }

