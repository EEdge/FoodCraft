package sfsu.csc413.foodcraft;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.ImageButton;
import android.widget.LinearLayout;
=======
import android.widget.EditText;
>>>>>>> master
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    UPCFragment upcfrag = new UPCFragment();
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

    public void addselectedFoods(List<UPCObject> item, boolean cached) {
        togglePhotoFragment(upcfrag.getView());
        if (selectedFoods.size() == 0) {
            lvSelectedIngredients.setAdapter(lvSelectedIngredientsAdapter);
        }
        if (item.size() == 1 && cached && item.get(0).product_title != null) {
            selectedFoods.add(item.get(0).product_title);
            lvSelectedIngredientsAdapter.notifyDataSetChanged();
        } else if (item.size() == 1 && !cached && item.get(0).product_title != null) {
            //Prompt edit string of the title, add to cache
            //Confirmation of title
            SingleIngredientAlert(item.get(0));
        } else if (item.size() > 1 && !cached) {
            //// TODO: 12/1/15  fix radio button selection
            //Radio button selection of each item, and 'Other' selection that adds custom title to database
            MultipleIngredientAlert(item);
        } else if (item.size() == 1 && item.get(0).product_title == null) {
            SingleIngredientAlert(item.get(0));
        }
    }

    private void SingleIngredientAlert(final UPCObject product) {
        LayoutInflater li = getLayoutInflater();
        final View promptsView = li.inflate(R.layout.single_ingredient_alert, null);
        EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setText(product.original_title,TextView.BufferType.EDITABLE);

        new AlertDialog.Builder(this.selfReference)
                .setView(promptsView)
                .setTitle("Unable to detect ingredient!")
                .setMessage("Please confirm the ingredient that you scanned. This will be saved for the next time you scan this item.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                        selectedFoods.add(userInput.getText().toString());
                        lvSelectedIngredientsAdapter.notifyDataSetChanged();
                        upcfrag.addtoDatabase(product.code, userInput.getText().toString(), selfReference.getApplicationContext());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void MultipleIngredientAlert(List<UPCObject> matches) {
        LayoutInflater li = getLayoutInflater();
        View promptsView = li.inflate(R.layout.multiple_ingredient_alert, null);
        RelativeLayout layout_root = (RelativeLayout) promptsView.findViewById(R.id.multiple_layout_root);
        RadioGroup radioGroup = new RadioGroup(this.selfReference);
        layout_root.addView(radioGroup);
        int counter = 0;
        for (UPCObject item : matches){
            RadioButton radioButtonView = new RadioButton(this.selfReference);
            radioButtonView.setText(item.product_title);
            radioGroup.addView(radioButtonView, counter);
            //((ViewGroup)layout_root.getParent()).removeView(layout_root); //this line causes it to crash
            counter++;
        }
        //RadioButton radioButtonView = new RadioButton(this.selfReference);
        //radioButtonView.setText("Not listed");
        //radioGroup.addView(radioButtonView, counter);
        new AlertDialog.Builder(this.selfReference)
                .setView(promptsView)
                .setTitle("Unable to detect ingredient!")
                .setMessage("Please select the ingredient that you scanned. This will be saved for the next time you scan this item.")

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    protected void launchSearchResultsActivity(ArrayList<Recipe> recipes) {

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
        //Paul changed this from a static selection to the IngredientList class
        IngredientList list = new IngredientList();
        foods = list.ingredients;
    }


    public void togglePhotoFragment(View view) {
        if (!upcfrag.isAdded()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_holder, upcfrag);
            transaction.show(upcfrag);
            transaction.hide(searchableFrag);
            transaction.commit();
        } else if (upcfrag.isHidden()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            upcfrag.onResume();
            transaction.show(upcfrag);
            transaction.hide(searchableFrag);
            transaction.commit();
        } else if (upcfrag.isVisible()) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(searchableFrag);
            transaction.hide(upcfrag);
            upcfrag.onPause();
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

        Intent intent = new Intent(this, SharedPreferences.class);
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

            ImageButton button = (ImageButton) v.findViewById(R.id.delete);
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

