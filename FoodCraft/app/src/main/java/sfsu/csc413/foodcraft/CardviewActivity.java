package sfsu.csc413.foodcraft;

/**
 * Activity responsible for creating the card view using the data taken from the search ingredients activity. The card view is created by
 * the card view adapter. This activity also contains the dynamically created activity_cardview_toolbar which is responsible for filtering the recipes by
 * the type of course chosen by the user.
 *
 * @author Robert Chung and Paul Klein
 * @version 1.0 November 15, 2015.
 */

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

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;


public class CardviewActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Spinner spinner_nav;

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
        setContentView(R.layout.activity_cardview_searchresults);
        recipeList = new ArrayList<>();
        selectedFoods = new ArrayList<>();
        selfReference = this;

        // Unbundle the recipe list being sent into the class.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeList = ((List<Recipe>) bundle.getSerializable(RECIPE_SEARCH_RESULTS));

        // Before modifyList becomes a new array with filtered elements, it will point to the full unaltered recipe list.
        modifyRecipe = recipeList;
        selectedFoods = (bundle.getStringArrayList(SELECTED_FOODS_ARRAY));
        //This is where we sort the actual recipeList from least to greatest using the following algorithm:
        //      (Total Ingredients in Recipe) - (Matched Ingredients) = Ingredients to completion
        Collections.sort(recipeList);

        // Toolbar and spinner are set up for this activity.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        setUpActionBar();
        addItemsToSpinner();

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mRecyclerView.setHasFixedSize(true); //Data size is fixed to improve the performance as the adapter creates the individual cards of the cardview.

        //The adapter is used to create the cardview using the recipeList.
        mAdapter = new CardviewAdapter(this, recipeList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListener);
        isListView = true;
    }

    CardviewAdapter.OnItemClickListener onItemClickListener = new CardviewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Recipe selected_recipe = modifyRecipe.get(position);
            //TODO MAKE CALL TO API WITH NEW SELECTED RECIPE
            RecipeDetailRequest request = new RecipeDetailRequest(getApplicationContext(), selfReference);
            request.run(selected_recipe);
        }
    };

    /**
     * Method inflates the menu xml file and adds items to the toolbar if it is present.
     *
     * @param menu interface being added to the toolbar for the user to interact with.
     * @return boolean true or false value.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_recipe, menu);
        this.menu = menu;
        return true;
    }

    /**
     * Method which creates the toolbar for the activity by using the setActionBar and getActionBar functions provided by the android support.
     */
    private void setUpActionBar() {
        if (toolbar != null) {
            setActionBar(toolbar);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setElevation(7);
        }
    }

    /**
     * Method that is responsible for sorting the courses. This algorithm uses a temporary recipe list which is an identical to the original recipe list, sorts the new list
     * and uses it to create a new list of cards in a cardview based on which course it was sorted by.
     *
     * @param course the course property of the recipe object.
     */
    public void sortCourse(String course) {
        modifyRecipe = new ArrayList<>(recipeList);

        for (int i = recipeList.size() - 1; i >= 0; i--) {
            Recipe aRecipe = recipeList.get(i);

            if (aRecipe.course != null && !aRecipe.course.equals(course) && !course.equals("All")) {
                modifyRecipe.remove(i);
            } else if (aRecipe.course == null) {
                modifyRecipe.remove(i);
            }
        }

        if (modifyRecipe.size() != 0) {
            mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView = (RecyclerView) findViewById(R.id.list);
            mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mAdapter = new CardviewAdapter(this, modifyRecipe);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(onItemClickListener);
            isListView = true;
        }
    }

    /**
     * Method which is responsible for passing the information of the recipe to the recipedetail class.
     *
     * @param recipeDetail object which contains all of the information from the recipe.
     */
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

    /**
     * Method which is needed to add items to the spinner. This method will determine all of the available courses from the list of 50 recipes and dynamically generate
     * a drop down spinner tabs which contain all of the available courses. For example, if there are no dessert recipes out of the 50 recipes, a desserts tab will not
     * be added to the spinner. After deciding what is to be added to the spinner, the spinner adapters are used to actually create it.
     */
    public void addItemsToSpinner() {
        templist = new ArrayList<>();

        for (int i = recipeList.size() - 1; i >= 0; i--) {
            Recipe aRecipe = recipeList.get(i);
            if (aRecipe.course != null) {
                if (!templist.contains(aRecipe.course)) {
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
        // This is a custom ArrayAdapter with a spinner item layout to set popup background.
        SpinnerAdapter spinAdapter = new SpinnerAdapter(getApplicationContext(), list);
        spinner_nav.setAdapter(spinAdapter);
        spinner_nav.setOnItemSelectedListener(new OnItemSelectedListener() {

            /**
             * Method responsible for handling what is done when the user clicks on an item from the drop down spinner. The recipe list will be sorted based on
             * what the user decides to filter the course by.
             * @param adapter the adapter used to create the spinner.
             * @param v the view for which the activity the spinner is in.
             * @param position the position of the item selected on the spinner list.
             * @param id the id for the item being selected.
             */
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String item = adapter.getItemAtPosition(position).toString();
                if (item.equals("All")) {
                    sortCourse("All");
                } else if (item.equals("Breakfast")) {
                    sortCourse("Breakfast and Brunch");
                } else if (item.equals("Lunch")) {
                    sortCourse("Lunch and Snacks");
                } else if (item.equals("Breads")) {
                    sortCourse("Breads");
                } else if (item.equals("Main Dishes")) {
                    sortCourse("Main Dishes");
                } else if (item.equals("Salads")) {
                    sortCourse("Salads");
                } else if (item.equals("Desserts")) {
                    sortCourse("Desserts");
                } else if (item.equals("Beverages")) {
                    sortCourse("Beverages");
                } else if (item.equals("Appetizers")) {
                    sortCourse("Appetizers");
                }

            }

            /**
             * Method that is called when the user does not select anything from the spinner.
             * @param arg0
             */
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
}
