package sfsu.csc413.foodcraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ResultsListActivity extends Activity {

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private RecipeAdapter mAdapter;
    private boolean isListView;
    private List<Recipe> recipeList;
    private ArrayList<String> selectedFoods;
    private Menu menu;
    private ResultsListActivity selfReference;

    public static final String RECIPE_SEARCH_RESULTS = "sfsu.csc413foodcraft.RECIPE_SEARCH_RESULTS";
    public static final String SELECTED_FOODS_ARRAY = "sfsu.csc413foodcraft.SELECTED_FOODS_ARRAY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);

        recipeList = new ArrayList<>();
        selectedFoods = new ArrayList<>();

        selfReference = this;

        // Unbundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeList = ((List<Recipe>) bundle.getSerializable(RECIPE_SEARCH_RESULTS));
        selectedFoods = (bundle.getStringArrayList(SELECTED_FOODS_ARRAY));
        //The following block sorts through the list to find the amount of matched ingredients in the recipes. It then sorts
        //the list based on the result of (TotalIngredients - MatchedIngredients) least to greatests
        ArrayList<Integer> zeromatches = new ArrayList<Integer>(){};
        for (int i = 0; i < recipeList.size(); i++){
            Recipe aRecipe = recipeList.get(i);
            for (String ingredient : selectedFoods) {
                if (aRecipe.ingredients.contains(ingredient)) {
                    aRecipe.matchedingredients++;
                } else if (aRecipe.ingredients.contains(ingredient + "s")) {
                    aRecipe.matchedingredients++;
                } else if (aRecipe.ingredients.contains(ingredient + "es")) {
                    aRecipe.matchedingredients++;
                } else if (aRecipe.ingredients.contains(ingredient.substring(0,ingredient.length()-2) + "ies")) {
                    aRecipe.matchedingredients++;
                }
            }
            if (aRecipe.matchedingredients == 0) zeromatches.add(i);
        }
        //This removes entries with no matches
        for (int i = 0; i< zeromatches.size(); i++){
            recipeList.remove(zeromatches.get(i)-i);
        }

        Collections.sort(recipeList);
        //Collections.reverse(recipeList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        mRecyclerView.setHasFixedSize(true); //Data size is fixed - improves performance
        mAdapter = new RecipeAdapter(this, recipeList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);

        isListView = true;
    }

    RecipeAdapter.OnItemClickListener onItemClickListener = new RecipeAdapter.OnItemClickListener() {
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
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
}
