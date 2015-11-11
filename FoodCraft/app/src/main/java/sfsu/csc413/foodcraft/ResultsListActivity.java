package sfsu.csc413.foodcraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

public class ResultsListActivity extends Activity {

  private Toolbar toolbar;

  private RecyclerView mRecyclerView;
  private StaggeredGridLayoutManager mStaggeredLayoutManager;
  private RecipeAdapter mAdapter;
  private boolean isListView;
  private Menu menu;

    public static final String RECIPE_SEARCH_RESULTS = "sfsu.csc413foodcraft.RECIPE_SEARCH_RESULTS";

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_searchresults);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setUpActionBar();

    mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

    mRecyclerView = (RecyclerView) findViewById(R.id.list);
    mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

    mRecyclerView.setHasFixedSize(true); //Data size is fixed - improves performance
    mAdapter = new RecipeAdapter(this);
    mRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemClickListener(onItemClickListener);

    isListView = true;
  }

    RecipeAdapter.OnItemClickListener onItemClickListener = new RecipeAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(View v, int position) {
          Intent transitionIntent = new Intent(ResultsListActivity.this, DetailActivity.class);
          transitionIntent.putExtra(DetailActivity.EXTRA_PARAM_ID, position);
          ImageView recipeImage = (ImageView) v.findViewById(R.id.recipeImage);
          LinearLayout recipeNameHolder = (LinearLayout) v.findViewById(R.id.recipeNameHolder);

          View navigationBar = findViewById(android.R.id.navigationBarBackground);
          View statusBar = findViewById(android.R.id.statusBarBackground);

          Pair<View, String> imagePair = Pair.create((View) recipeImage, "tImage");
          Pair<View, String> holderPair = Pair.create((View) recipeNameHolder, "tNameHolder");
          Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
          Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
          Pair<View, String> toolbarPair = Pair.create((View) toolbar, "tActionBar");

          ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ResultsListActivity.this, imagePair, holderPair, navPair, statusPair, toolbarPair);
          ActivityCompat.startActivity(ResultsListActivity.this, transitionIntent, options.toBundle());
      }
  };

  private void setUpActionBar() {
    if (toolbar != null) {
      setActionBar(toolbar);
      getActionBar().setDisplayHomeAsUpEnabled(false);
      getActionBar().setDisplayShowTitleEnabled(true);
      getActionBar().setElevation(7);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    this.menu = menu;
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_toggle) {
      toggle();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void toggle() {
    MenuItem item = menu.findItem(R.id.action_toggle);
    if (isListView) {
      mStaggeredLayoutManager.setSpanCount(2);
      item.setIcon(R.drawable.ic_action_list);
      item.setTitle("Show as list");
      isListView = false;
    } else {
      mStaggeredLayoutManager.setSpanCount(1);
      item.setIcon(R.drawable.ic_action_grid);
      item.setTitle("Show as grid");
      isListView = true;
    }
  }
}