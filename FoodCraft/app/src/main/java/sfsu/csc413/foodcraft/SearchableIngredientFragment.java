package sfsu.csc413.foodcraft;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sfsu.csc413.foodcraft.dummy.DummyContent;

import static sfsu.csc413.foodcraft.R.id.searchable_ingredient_item;
import static sfsu.csc413.foodcraft.R.id.searchview_container;
import static sfsu.csc413.foodcraft.R.layout.abc_search_view;
import static sfsu.csc413.foodcraft.R.layout.list_item_searchable_ingredients;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SearchableIngredientFragment extends Fragment implements AbsListView.OnItemClickListener {

    ArrayList<String> searchableIngredients = new ArrayList<>();
    ArrayAdapter<String> lvIngredientSearchAdapter;
    ListView lvIngredientSearch;
    Boolean queryIsUnique = true;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */


    // TODO: Rename and change types of parameters
    public static SearchableIngredientFragment newInstance(String param1, String param2) {
        SearchableIngredientFragment fragment = new SearchableIngredientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchableIngredientFragment() {
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public ArrayList<String> getSearchableIngredients() {
        return searchableIngredients;
    }

    public void setSearchableIngredients(ArrayList<String> arrayList) {
        this.searchableIngredients = arrayList;
        lvIngredientSearchAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_searchable_ingredients, R.id.searchable_ingredient_item, searchableIngredients);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);
        lvIngredientSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                if (!isInArray(value, IngredientSearch.selectedFoods)) {
                    IngredientSearch.selectedFoods.add(0, value);
                    IngredientSearch.lvSelectedIngredients.setAdapter(IngredientSearch.lvSelectedIngredientsAdapter);
                }
                searchableIngredients.remove(position);
                lvIngredientSearchAdapter.notifyDataSetChanged();
                IngredientSearch.searchView.clearFocus();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        populateArray();
        searchableIngredients.addAll(searchableIngredients);
        final ArrayAdapter lvIngredientSearchAdapter = new ArrayAdapter<>(getActivity(), list_item_searchable_ingredients, searchable_ingredient_item, searchableIngredients);
        lvIngredientSearch = (ListView) view.findViewById(R.id.listView1);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);
        lvIngredientSearch.setTextFilterEnabled(true);

        //on click of searchable list item, add item to selected list
        lvIngredientSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                if (!isInArray(value, IngredientSearch.selectedFoods)) {
                    IngredientSearch.selectedFoods.add(0, value);
                    IngredientSearch.lvSelectedIngredients.setAdapter(IngredientSearch.lvSelectedIngredientsAdapter);
                    searchableIngredients.remove(position);
                    lvIngredientSearchAdapter.notifyDataSetChanged();

                }
            }
        });

        IngredientSearch.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //on text submit, filter list to show search results as user types
                filter(searchableIngredients, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //on text change, filter list to show search results as user types
                filter(searchableIngredients, newText);
                return true;
            }
        });//listen for text change

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean isInArray(String string, ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (string.equals(arrayList.get(i)))
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = lvIngredientSearch.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    private void filter(ArrayList<String> searchableListArray, CharSequence searchQuery) {
        //filter results by matching the query string and setting a new array to the ArrayAdapter on each text change
        searchQuery = searchQuery.toString().toLowerCase();

        final ArrayList<String> newFilterResults;

        if (searchQuery != null && searchQuery.length() > 0) {

            queryIsUnique = true;

            ArrayList<String> auxData = new ArrayList<>();

            for (int i = 0; i < searchableListArray.size(); i++) {
                if (searchableListArray.get(i).toLowerCase().contains(searchQuery))
                    auxData.add(searchableListArray.get(i));
            }

            newFilterResults = auxData;

            for (int j = 0; j < newFilterResults.size(); j++) {
               if (searchQuery == newFilterResults.get(j)) queryIsUnique = false;
            }

            if (queryIsUnique) newFilterResults.add(0, (String) searchQuery);

        } else {
            queryIsUnique = true;
            newFilterResults = searchableIngredients;
        }
        lvIngredientSearchAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_searchable_ingredients, R.id.searchable_ingredient_item, newFilterResults);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);
        lvIngredientSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                if (!isInArray(value, IngredientSearch.selectedFoods)) {
                    IngredientSearch.selectedFoods.add(0, value);
                    IngredientSearch.lvSelectedIngredients.setAdapter(IngredientSearch.lvSelectedIngredientsAdapter);
                    newFilterResults.remove(position);
                    if (queryIsUnique && position != 0)
                        searchableIngredients.remove(position - 1);
                    else if (!queryIsUnique) searchableIngredients.remove(position);
                    lvIngredientSearchAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String id);
    }

    private void populateArray () {
        IngredientList list = new IngredientList();
        searchableIngredients = list.ingredients;
    }

    public void refreshArray () {
        populateArray();
        if (lvIngredientSearchAdapter != null) {
            lvIngredientSearchAdapter.notifyDataSetChanged();
        }
    }

}
