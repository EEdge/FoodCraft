package sfsu.csc413.foodcraft;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import java.util.ArrayList;

public class IngredientSearch extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    //ArrayLists for selectable foods and selected foods
    ArrayList<String> foods = new ArrayList<>();
    ArrayList<String> selectedFoods = new ArrayList<>();

    //ListViews to display ArrayLists
    ListView lvIngredientSearch;
    ListView lvSelectedIngredients;

    //ArrayAdapters for both ListViews
    ArrayAdapter<String> lvIngredientSearchAdapter;
    ArrayAdapter<String> lvSelectedIngredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredient_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        populateIngredientSearchArray();

        //initialize searchable ingredient ListView
        lvIngredientSearchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foods);
        lvIngredientSearch = (ListView) findViewById(R.id.listView1);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);
        lvIngredientSearch.setTextFilterEnabled(true);

        //initialize selected ingredient list, non-searchable
        lvSelectedIngredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedFoods);
        lvSelectedIngredients = (ListView) findViewById(R.id.selectedIngredientsListView);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);

        //on click of searchable list item, add item to selected list
        lvIngredientSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                selectedFoods.add(value);
                lvSelectedIngredients.setAdapter(lvSelectedIngredientsAdapter);
            }
        });

        //on click of selected item list, delete item from list
        lvSelectedIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                selectedFoods.remove(position);
                lvSelectedIngredients.setAdapter(lvSelectedIngredientsAdapter);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });

        //initialize SearchView for searchable ingredient list
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.menu_item_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);//listen for text change


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
        lvIngredientSearchAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, newFilterResults);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);
    }

}

