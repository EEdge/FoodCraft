package sfsu.csc413.foodcraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;


public class CardviewActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Spinner spinner_nav;


    private Activity myActivity;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private CardviewAdapter mAdapter;
    private boolean isListView;
    private List<Recipe> recipeList;
    private List<Recipe> modifyRecipe;
    private List<String> templist;
    private ArrayList<String> selectedFoods;
    private Menu menu;
    private CardviewActivity selfReference;

    public static final String RECIPE_SEARCH_RESULTS = "sfsu.csc413foodcraft.RECIPE_SEARCH_RESULTS";
    public static final String SELECTED_FOODS_ARRAY = "sfsu.csc413foodcraft.SELECTED_FOODS_ARRAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview_toolbar);

        recipeList = new ArrayList<>();
        selectedFoods = new ArrayList<>();

        selfReference = this;

        // Unbundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeList = ((List<Recipe>) bundle.getSerializable(RECIPE_SEARCH_RESULTS));
        selectedFoods = (bundle.getStringArrayList(SELECTED_FOODS_ARRAY));

        //The following block sorts through the list to find the amount of matched ingredients in the recipes. It then sorts
        //the list based on the result of (TotalIngredients - MatchedIngredients) least to greatest.
        ArrayList<Integer> zeromatches = new ArrayList<Integer>(){};
        for (int i = 0; i < recipeList.size(); i++){
            Recipe aRecipe = recipeList.get(i);
            if (aRecipe.matchedingredients == 0) zeromatches.add(i);
        }

        //This removes entries with no matches
        for (int i = 0; i< zeromatches.size(); i++){
            recipeList.remove(zeromatches.get(i)-i);
        }

        Collections.sort(recipeList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        setUpActionBar();
        addItemsToSpinner();


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        mRecyclerView.setHasFixedSize(true); //Data size is fixed - improves performance
        mAdapter = new CardviewAdapter(this, recipeList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);

        isListView = true;
    }

    CardviewAdapter.OnItemClickListener onItemClickListener = new CardviewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Recipe selected_recipe = recipeList.get(position);
            //TODO MAKE CALL TO API WITH NEW SELECTED RECIPE
            RecipeDetailRequest request = new RecipeDetailRequest(getApplicationContext(), selfReference);
            request.run(selected_recipe);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu=menu;
        return true;
    }

    private void setUpActionBar() {
        if (toolbar != null) {
            setActionBar(toolbar);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setElevation(7);
        }
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

       if (id == R.id.action_settings) {
           Intent intent = new Intent(this,SharedPreferences.class);
           startActivity(intent);
           return true;
       }
        return super.onOptionsItemSelected(item);
    }


    public void sortCourse(String course) {

        modifyRecipe = new ArrayList<>(recipeList);

        for (int i = recipeList.size() - 1; i >= 0; i--){
            Recipe aRecipe = recipeList.get(i);

            if (aRecipe.course != null && !aRecipe.course.equals(course) && !course.equals("All")) {
                modifyRecipe.remove(i);

            }
            else if (aRecipe.course==null) {
                modifyRecipe.remove(i);
            }
        }
        if (modifyRecipe.size() != 0) {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

            mRecyclerView = (RecyclerView) findViewById(R.id.list);
            mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

            mRecyclerView.setHasFixedSize(true); //Data size is fixed - improves performance
            mAdapter = new CardviewAdapter(this, modifyRecipe);
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(onItemClickListener);

            isListView = true;
        }

    }

    protected void launchDetailActivity(RecipeDetail recipeDetail) {
        Log.i("LDA", "1");
        Intent intent = new Intent(selfReference, RecipeDetailActivity.class);
        Log.i("LDA", "2");
        Bundle bundle = new Bundle();
        Log.i("LDA", "3");
        bundle.putSerializable(RecipeDetailActivity.RECIPE_DETAILS, recipeDetail);
        Log.i("LDA", "4");
        intent.putExtras(bundle);
        Log.i("LDA", "5");
        startActivity(intent);
    }

    // add items into spinner dynamically
    public void addItemsToSpinner() {

        templist = new ArrayList<>();

        for (int i = recipeList.size() - 1; i >= 0; i--) {
            Recipe aRecipe = recipeList.get(i);
            if (aRecipe.course != null) {
                if (!templist.contains(aRecipe.course)){
                    templist.add(aRecipe.course);
                }
            }
        }

            ArrayList<String> list = new ArrayList<>();
            list.add("Filter By");
            list.add("All");
        if (templist.contains("Breakfast and Brunch")) {
            list.add("Breakfast");
        }
        if (templist.contains("Lunch and Snacks")) {
            list.add("Lunch");
        }
        if (templist.contains("Appetizers")) {
            list.add("Appetizers");
        }
        if (templist.contains("Main Dishes")) {
            list.add("Main Dishes");
        }
        if (templist.contains("Breads")) {
            list.add("Breads");
        }
        if (templist.contains("Salads")) {
            list.add("Salads");
        }
        if (templist.contains("Desserts")) {
            list.add("Desserts");
        }
        if (templist.contains("Beverages")) {
            list.add("Beverages");
        }
            // Custom ArrayAdapter with spinner item layout to set popup background

            CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                    getApplicationContext(), list);

            // Default ArrayAdapter with default spinner item layout, getting some
            // view rendering problem in lollypop device, need to test in other
            // devices

  /*
   * ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,
   * android.R.layout.simple_spinner_item, list);
   * spinAdapter.setDropDownViewResource
   * (android.R.layout.simple_spinner_dropdown_item);
   */

            spinner_nav.setAdapter(spinAdapter);

            spinner_nav.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                    // On selecting a spinner item
                    String item = adapter.getItemAtPosition(position).toString();
                    if (item.equals("All")) {
                        sortCourse("All");
                    }
                    if (item.equals("Breakfast")) {
                        sortCourse("Breakfast and Brunch");
                    }

                    if (item.equals("Lunch")) {
                        sortCourse("Lunch and Snacks");
                    }

                    if (item.equals("Breads")) {
                        sortCourse("Breads");
                    }

                    if (item.equals("Main Dishes")) {
                        sortCourse("Main Dishes");
                    }

                    if (item.equals("Salads")) {
                        sortCourse("Salads");
                    }

                    if (item.equals("Desserts")) {
                        sortCourse("Desserts");
                    }

                    if (item.equals("Beverages")) {
                        sortCourse("Beverages");
                    }

                    if (item.equals("Appetizers")) {
                        sortCourse("Appetizers");
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

        }
    }
