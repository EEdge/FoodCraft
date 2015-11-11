package sfsu.csc413.foodcraft;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
        implements SearchView.OnQueryTextListener {

    //ArrayLists for selectable foods and selected foods
    ArrayList<String> foods = new ArrayList<>();
    static ArrayList<String> selectedFoods = new ArrayList<>();

    // Activity self reference
    IngredientSearch selfReference;

    //ListViews to display ArrayLists
    ListView lvIngredientSearch;
    static ListView lvSelectedIngredients;

    //ArrayAdapters for both ListViews
    ArrayAdapter<String> lvIngredientSearchAdapter;
    CustomAdapter<String> lvSelectedIngredientsAdapter;

    // Buttons
    Button searchButton;

    int listIndex = 0;

    TestPhotoFragment testPhotoFrag = new TestPhotoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredient_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchButton = (Button) findViewById(R.id.foodcraft_search_button);
        setSupportActionBar(toolbar);
        populateIngredientSearchArray();
        selfReference = this;

        //initialize searchable ingredient ListView
        lvIngredientSearchAdapter = new ArrayAdapter<>(this, R.layout.list_item_searchable_ingredients, R.id.searchable_ingredient_item, foods);
        lvIngredientSearch = (ListView) findViewById(R.id.listView1);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);
        lvIngredientSearch.setTextFilterEnabled(true);

        //initialize selected ingredient list, non-searchable
        lvSelectedIngredientsAdapter = new CustomAdapter<>(this, R.layout.list_item_selected_ingredients, R.id.selected_item, selectedFoods);
        lvSelectedIngredients = (ListView) findViewById(R.id.selectedIngredientsListView);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);

        //on click of searchable list item, add item to selected list
        lvIngredientSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                if (!isInArray(value, selectedFoods)) {
                    selectedFoods.add(0, value);
                    lvSelectedIngredients.setAdapter(lvSelectedIngredientsAdapter);
                }
            }
        });

        //on click of selected item list, delete item from list
        lvSelectedIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                listIndex = position;
                selectedFoods.remove(position);
                lvSelectedIngredients.deferNotifyDataSetChanged();

                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });

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
        SearchView searchView = (SearchView) findViewById(R.id.menu_item_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);//listen for text change


    }

    /*public void onSearchButtonClick () {


    }*/

    protected void launchSearchResultsActivity (ArrayList<Recipe> recipes) {

        Intent intent = new Intent(this, ResultsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ResultsListActivity.RECIPE_SEARCH_RESULTS, recipes);
        intent.putExtras(bundle);
        
        startActivity(intent);

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


    public boolean onQueryTextChange(String newText) {
        //on text change, filter list to show search results as user types
        filter(foods, newText);
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        //on text submit, filter list to show search results as user types
        filter(foods, query);
        return false;
    }

    private void filter(ArrayList<String> searchableListArray, CharSequence searchQuery) {
        //filter results by matching the query string and setting a new array to the ArrayAdapter on each text change
        searchQuery = searchQuery.toString().toLowerCase();

        ArrayList<String> newFilterResults;

        if (searchQuery != null && searchQuery.length() > 0) {


            ArrayList<String> auxData = new ArrayList<>();

            for (int i = 0; i < searchableListArray.size(); i++) {
                if (searchableListArray.get(i).toLowerCase().contains(searchQuery))
                    auxData.add(searchableListArray.get(i));
            }

            newFilterResults = auxData;
        } else {

            newFilterResults = searchableListArray;
        }
        lvIngredientSearchAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_searchable_ingredients, R.id.searchable_ingredient_item, newFilterResults);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);
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

