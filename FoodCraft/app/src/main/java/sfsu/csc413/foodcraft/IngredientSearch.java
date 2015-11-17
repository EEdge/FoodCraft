package sfsu.csc413.foodcraft;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class IngredientSearch extends AppCompatActivity
        implements SearchView.OnQueryTextListener, SearchableIngredientFragment.OnFragmentInteractionListener {

    //ArrayLists for selectable foods and selected foods
    ArrayList<String> foods = new ArrayList<>();
    static ArrayList<String> selectedFoods = new ArrayList<>();

    // Activity self reference
    IngredientSearch selfReference;

    //ListViews to display ArrayLists
    static ListView lvSelectedIngredients;

    //ArrayAdapters for both ListViews
    //ArrayAdapter<String> lvIngredientSearchAdapter;
    static CustomAdapter<String> lvSelectedIngredientsAdapter;

    // Buttons
    Button searchButton;

    //SearchView
    static SearchView searchView;

    int listIndex = 0;

    TestPhotoFragment testPhotoFrag = new TestPhotoFragment();
    SearchableIngredientFragment searchableFrag = new SearchableIngredientFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_holder, searchableFrag);
        transaction.show(searchableFrag);
        transaction.commit();

        setContentView(R.layout.activity_ingredient_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchButton = (Button) findViewById(R.id.foodcraft_search_button);
        setSupportActionBar(toolbar);
        populateIngredientSearchArray();
        selfReference = this;

        //initialize selected ingredient list, non-searchable
        lvSelectedIngredientsAdapter = new CustomAdapter<>(this, R.layout.list_item_selected_ingredients, R.id.selected_item, selectedFoods);
        lvSelectedIngredients = (ListView) findViewById(R.id.selectedIngredientsListView);

        // on click searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeSearchRequest searchRequest = new RecipeSearchRequest(getApplicationContext(), selfReference);
                searchRequest.run(selectedFoods);
            }
        });

        //initialize SearchView for searchable ingredient list
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.menu_item_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);



    }


    protected void launchSearchResultsActivity (ArrayList<Recipe> recipes) {

        Log.i("LAUNCH_RESULTS", "Started with recipes list of length " + recipes.size());

        Intent intent = new Intent(this, ResultsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ResultsListActivity.RECIPE_SEARCH_RESULTS, recipes);
        bundle.putStringArrayList(ResultsListActivity.SELECTED_FOODS_ARRAY, selectedFoods);
        intent.putExtras(bundle);
        Log.i("LAUNCH_RESULTS", "Starting new activity");
        selfReference.startActivity(intent);

        }

    private void populateIngredientSearchArray() {
        //method to populate array of searchable/selectable ingredient items
        foods.add("banana");
        foods.add("apple");
        foods.add("grape");
        foods.add("lemon");
        foods.add("orange");
        foods.add("bread");
        foods.add("lime");
        foods.add("chicken");
        foods.add("ground beef");
        foods.add("beef");
        foods.add("ground chicken");
        foods.add("potato");
        foods.add("sweet potato");
        foods.add("pear");
    }


    public void togglePhotoFragment(View view) {
        if (!testPhotoFrag.isAdded()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_holder, testPhotoFrag);
            transaction.show(testPhotoFrag);
            transaction.commit();
        } else if (testPhotoFrag.isHidden()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(testPhotoFrag);
            transaction.commit();
        } else if (testPhotoFrag.isVisible()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(testPhotoFrag);
            transaction.commit();
        }
    }

    private boolean isInArray(String string, ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (string == arrayList.get(i))
                return true;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        UPCRequest barcode_scanner = new UPCRequest("https://api.outpan.com/v1/products/",
                "name", "459563971cd36022e52e0c936ce2836c");
        if (scanResult != null) {
            barcode_scanner.craftUPCRequest(scanResult.getContents(), this);
        }
    }

    public static void deleteIngredient(int position, ArrayAdapter adapter) {
        selectedFoods.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(this,SharedPreferences.class);
        startActivity(intent);
        return true;
    }


    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

    class CustomAdapter<T> extends ArrayAdapter {

        ArrayList<String> list;

        public CustomAdapter(Context context, int layoutContainerId, int layoutResourceId, ArrayList<String> list) {
            super(context, layoutContainerId, layoutResourceId, list);

            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_item_selected_ingredients, null);
            }

            String string = (String) getItem(position);

            TextView t = (TextView) v.findViewById(R.id.selected_item);

            t.setText(string);

            Button button = (Button) v.findViewById(R.id.delete);
            button.setTag(position);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    list.remove(pos);
                    CustomAdapter.this.notifyDataSetChanged();
                }
            });


            return v;
        }


    }

