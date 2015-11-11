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
import java.util.List;

public class ResultsListActivity extends Activity {

  private Toolbar toolbar;

  private RecyclerView mRecyclerView;
  private StaggeredGridLayoutManager mStaggeredLayoutManager;
  private RecipeAdapter mAdapter;
  private boolean isListView;
    private List<Recipe> recipeList;
  private Menu menu;
    private ResultsListActivity selfReference;

    public static final String RECIPE_SEARCH_RESULTS = "sfsu.csc413foodcraft.RECIPE_SEARCH_RESULTS";

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_searchresults);

    recipeList = new ArrayList<>();

    // Unbundle
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    recipeList = ((List<Recipe>) bundle.getSerializable(RECIPE_SEARCH_RESULTS));

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
            RecipeDetailRequest request = new RecipeDetailRequest(getApplicationContext(),selfReference);
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

    protected void launchDetailActivity (RecipeDetail recipeDetail) {

        Intent intent = new Intent(selfReference, RecipeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RecipeDetailActivity.RECIPE_DETAILS, recipeDetail);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}