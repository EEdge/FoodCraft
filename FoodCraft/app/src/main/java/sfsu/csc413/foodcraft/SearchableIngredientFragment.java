package sfsu.csc413.foodcraft;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sfsu.csc413.foodcraft.dummy.DummyContent;

import static sfsu.csc413.foodcraft.R.id.searchable_ingredient_item;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        populateArray();
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
            if (string == arrayList.get(i))
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
        lvIngredientSearchAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_searchable_ingredients, R.id.searchable_ingredient_item, newFilterResults);
        lvIngredientSearch.setAdapter(lvIngredientSearchAdapter);
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
        public void onFragmentInteraction(String id);
    }

    private void populateArray () {
        searchableIngredients.add("apple");
        searchableIngredients.add("apricot");
        searchableIngredients.add("avocado");
        searchableIngredients.add("banana");
        searchableIngredients.add("bilberry");
        searchableIngredients.add("blackberry");
        searchableIngredients.add("blackcurrant");
        searchableIngredients.add("blueberry");
        searchableIngredients.add("boysenberry");
        searchableIngredients.add("cantaloupe");
        searchableIngredients.add("currant");
        searchableIngredients.add("cherry");
        searchableIngredients.add("cherimoya");
        searchableIngredients.add("cloudberry");
        searchableIngredients.add("coconut");
        searchableIngredients.add("cranberry");
        searchableIngredients.add("damson");
        searchableIngredients.add("date");
        searchableIngredients.add("dragonfruit");
        searchableIngredients.add("durian");
        searchableIngredients.add("elderberry");
        searchableIngredients.add("feijoa");
        searchableIngredients.add("fig");
        searchableIngredients.add("goji berry");
        searchableIngredients.add("gooseberry");
        searchableIngredients.add("grape");
        searchableIngredients.add("raisin");
        searchableIngredients.add("grapefruit");
        searchableIngredients.add("guava");
        searchableIngredients.add("huckleberry");
        searchableIngredients.add("jabouticaba");
        searchableIngredients.add("jackfruit");
        searchableIngredients.add("jambul");
        searchableIngredients.add("jujube");
        searchableIngredients.add("juniper berry");
        searchableIngredients.add("kiwi fruit");
        searchableIngredients.add("kumquat");
        searchableIngredients.add("kemon");
        searchableIngredients.add("lime");
        searchableIngredients.add("loquat");
        searchableIngredients.add("lychee");
        searchableIngredients.add("mango");
        searchableIngredients.add("marion berry");
        searchableIngredients.add("melon");
        searchableIngredients.add("honeydew");
        searchableIngredients.add("watermelon");
        searchableIngredients.add("miracle fruit");
        searchableIngredients.add("mulberry");
        searchableIngredients.add("nectarine");
        searchableIngredients.add("olive");
        searchableIngredients.add("orange");
        searchableIngredients.add("blood orange");
        searchableIngredients.add("clementine");
        searchableIngredients.add("mandarine");
        searchableIngredients.add("tangerine");
        searchableIngredients.add("pear");
        searchableIngredients.add("persimmon");
        searchableIngredients.add("physalis");
        searchableIngredients.add("plum");
        searchableIngredients.add("prune");
        searchableIngredients.add("pineapple");
        searchableIngredients.add("pumpkin");
        searchableIngredients.add("pomegranate");
        searchableIngredients.add("pomelo");
        searchableIngredients.add("purple mangosteen");
        searchableIngredients.add("quince");
        searchableIngredients.add("raspberry");
        searchableIngredients.add("salmonberry");
        searchableIngredients.add("rambutan");
        searchableIngredients.add("redcurrant");
        searchableIngredients.add("salal berry");
        searchableIngredients.add("satsuma");
        searchableIngredients.add("star fruit");
        searchableIngredients.add("strawberry");
        searchableIngredients.add("squash");
        searchableIngredients.add("tamarillo");
        searchableIngredients.add("tamarind");
        searchableIngredients.add("tomato");
        searchableIngredients.add("bok choy");
        searchableIngredients.add("arugula");
        searchableIngredients.add("beet");
        searchableIngredients.add("brussel sprout");
        searchableIngredients.add("cabbage");
        searchableIngredients.add("celery");
        searchableIngredients.add("chicory");
        searchableIngredients.add("collard greens");
        searchableIngredients.add("dill");
        searchableIngredients.add("endive");
        searchableIngredients.add("grape leaves");
        searchableIngredients.add("kale");
        searchableIngredients.add("lettuce");
        searchableIngredients.add("mustard");
        searchableIngredients.add("napa cabbage");
        searchableIngredients.add("spinach");
        searchableIngredients.add("turnip");
        searchableIngredients.add("watercress");
        searchableIngredients.add("wheatgrass");
        searchableIngredients.add("bell pepper");
        searchableIngredients.add("gourd");
        searchableIngredients.add("chayote");
        searchableIngredients.add("cucumber");
        searchableIngredients.add("sweet pepper");
        searchableIngredients.add("tomatillo");
        searchableIngredients.add("vanilla");
        searchableIngredients.add("zucchini");
        searchableIngredients.add("artichoke");
        searchableIngredients.add("broccoli");
        searchableIngredients.add("caper");
        searchableIngredients.add("cauliflower");
        searchableIngredients.add("black bean");
        searchableIngredients.add("chickpea");
        searchableIngredients.add("fava bean");
        searchableIngredients.add("garbanzo");
        searchableIngredients.add("green bean");
        searchableIngredients.add("lentil");
        searchableIngredients.add("lima bean");
        searchableIngredients.add("moth bean");
        searchableIngredients.add("mung bean");
        searchableIngredients.add("okra");
        searchableIngredients.add("pea");
        searchableIngredients.add("peanut");
        searchableIngredients.add("snap pea");
        searchableIngredients.add("snow pea");
        searchableIngredients.add("soybean");
        searchableIngredients.add("asparagus");
        searchableIngredients.add("chives");
        searchableIngredients.add("garlic");
        searchableIngredients.add("lemongrass");
        searchableIngredients.add("leek");
        searchableIngredients.add("white onion");
        searchableIngredients.add("yellow onion");
        searchableIngredients.add("red onion");
        searchableIngredients.add("sweet onion");
        searchableIngredients.add("shallot");
        searchableIngredients.add("cassava");
        searchableIngredients.add("ginger");
        searchableIngredients.add("horseradish");
        searchableIngredients.add("jicama");
        searchableIngredients.add("potato");
        searchableIngredients.add("radish");
        searchableIngredients.add("rutabaga");
        searchableIngredients.add("sweet potato");
        searchableIngredients.add("taro");
        searchableIngredients.add("turmeric");
        searchableIngredients.add("wasabi");
        searchableIngredients.add("yam");
        searchableIngredients.add("quinoa");
        searchableIngredients.add("cilantro");
        searchableIngredients.add("coriander");
        searchableIngredients.add("whole milk");
        searchableIngredients.add("skim milk");
        searchableIngredients.add("fat-free milk");
        searchableIngredients.add("egg");
        searchableIngredients.add("plain yogurt");
        searchableIngredients.add("greek yogurt");
        searchableIngredients.add("cottage cheese");
        searchableIngredients.add("cream cheese");
        searchableIngredients.add("pepperoni");
        searchableIngredients.add("mozarella");
        searchableIngredients.add("parmesan cheese");
        searchableIngredients.add("cheddar cheese");
        searchableIngredients.add("sharp cheddar");
        searchableIngredients.add("allspice");
        searchableIngredients.add("anise");
        searchableIngredients.add("basil");
        searchableIngredients.add("bay leaf");
        searchableIngredients.add("caraway");
        searchableIngredients.add("cardamom");
        searchableIngredients.add("catnip");
        searchableIngredients.add("cayenne pepper");
        searchableIngredients.add("celery seed");
        searchableIngredients.add("chili pepper");
        searchableIngredients.add("cumin");
        searchableIngredients.add("curry leaf");
        searchableIngredients.add("fennel");
        searchableIngredients.add("jasmine");
        searchableIngredients.add("lavender");
        searchableIngredients.add("licorice");
        searchableIngredients.add("mace");
        searchableIngredients.add("marjoram");
        searchableIngredients.add("mint");
        searchableIngredients.add("mustard seed");
        searchableIngredients.add("nutmeg");
        searchableIngredients.add("oregano");
        searchableIngredients.add("paprika");
        searchableIngredients.add("parsley");
        searchableIngredients.add("black pepper");
        searchableIngredients.add("white pepper");
        searchableIngredients.add("saffron");
        searchableIngredients.add("sage");
        searchableIngredients.add("sassafras");
        searchableIngredients.add("savory");
        searchableIngredients.add("tarragon");
        searchableIngredients.add("thyme");
        searchableIngredients.add("wintergreen");
        searchableIngredients.add("white rice");
        searchableIngredients.add("brown rice");
        searchableIngredients.add("wild rice");
        searchableIngredients.add("white flour");
        searchableIngredients.add("wheat flour");
        searchableIngredients.add("barley");
        searchableIngredients.add("sorghum");
        searchableIngredients.add("millet");
        searchableIngredients.add("oat");
        searchableIngredients.add("rye");
        searchableIngredients.add("chia seed");
        searchableIngredients.add("ground beef");
        searchableIngredients.add("ground chicken");
        searchableIngredients.add("chicken");
        searchableIngredients.add("pot roast");
        searchableIngredients.add("salmon");
        searchableIngredients.add("tilapia");
        searchableIngredients.add("catfish");
        searchableIngredients.add("bacon");
        searchableIngredients.add("low-fat cream cheese");
        searchableIngredients.add("sharp cheddar cheese");
        searchableIngredients.add("plain greek yogurt");
        searchableIngredients.add("green onion");
        searchableIngredients.add("ranch-style seasoning");
        searchableIngredients.add("garlic paste");
        searchableIngredients.add("corn tortilla");
        searchableIngredients.add("flour tortilla");
        searchableIngredients.add("butter");
        searchableIngredients.add("cheese");
        searchableIngredients.add("ketchup");
        searchableIngredients.add("pickle");
        searchableIngredients.add("russet potato");
        searchableIngredients.add("green pepper");
        searchableIngredients.add("ham");
        searchableIngredients.add("carrot");
        searchableIngredients.add("low sodium chicken");
        searchableIngredients.add("all-purpose flour");
        searchableIngredients.add("milk");
        searchableIngredients.add("heavy cream");
        searchableIngredients.add("salt");
        searchableIngredients.add("cajun spice mix");
        searchableIngredients.add("boneless chicken breast");
        searchableIngredients.add("red potato");
        searchableIngredients.add("olive oil");
        searchableIngredients.add("garlic powder");
        searchableIngredients.add("seasoning salt");
        searchableIngredients.add("ground black pepper");
        searchableIngredients.add("brown sugar");
        searchableIngredients.add("dry mustard");
        searchableIngredients.add("kosher salt");
        searchableIngredients.add("worcestershire sauce");
        searchableIngredients.add("canadian bacon");
        searchableIngredients.add("pepper");
        searchableIngredients.add("onion");
        searchableIngredients.add("water");
        searchableIngredients.add("rosemary");
        searchableIngredients.add("whipping cream");
        searchableIngredients.add("sour cream");
        searchableIngredients.add("chicken breast");
        searchableIngredients.add("chili powder");
        searchableIngredients.add("powdered sugar");
        searchableIngredients.add("double cream");
        searchableIngredients.add("toast");
        searchableIngredients.add("lemon juice");
        searchableIngredients.add("ice cube");
        searchableIngredients.add("turkey sausage");
        searchableIngredients.add("chicken broth");
        searchableIngredients.add("red wine");
        searchableIngredients.add("arborio rice");
        searchableIngredients.add("garbanzo beans");
        searchableIngredients.add("tomatoes");
        searchableIngredients.add("carrots");
        searchableIngredients.add("scallions");
        searchableIngredients.add("chutney");
        searchableIngredients.add("baby spinach");
        searchableIngredients.add("coconut water");
        searchableIngredients.add("unsweetened coconut water");
        searchableIngredients.add("coconut oil");
        searchableIngredients.add("sea salt");
        searchableIngredients.add("unsalted butter");
        searchableIngredients.add("lime juice");
        searchableIngredients.add("extra-virgin olive oil");
        searchableIngredients.add("red cabbage");
        searchableIngredients.add("candied pecan");
        searchableIngredients.add("poppy seed dressing");
        searchableIngredients.add("canola oil");
        searchableIngredients.add("smoked sausage");
        searchableIngredients.add("italian seasoning");
        searchableIngredients.add("parsnip");
        searchableIngredients.add("gold potato");
        searchableIngredients.add("butternut squash");
        searchableIngredients.add("chicken stock");
        searchableIngredients.add("italian sausages");
        searchableIngredients.add("beef broth");
        searchableIngredients.add("tomato sauce");
        searchableIngredients.add("tomato paste");
        searchableIngredients.add("cheese tortellini");
        searchableIngredients.add("garlic clove");
        searchableIngredients.add("red chili pepper");
        searchableIngredients.add("ground cinnamon");
        searchableIngredients.add("tumeric");
        searchableIngredients.add("red lentil");
        searchableIngredients.add("peanut butter");
        searchableIngredients.add("coconut milk");
        searchableIngredients.add("chicken sausage");
        searchableIngredients.add("italian sausage");
        searchableIngredients.add("red pepper flakes");
        searchableIngredients.add("balsamic vinegar");
        searchableIngredients.add("soy sauce");
        searchableIngredients.add("jalapeno chili");
        searchableIngredients.add("lime wedge");
        searchableIngredients.add("vegetable stock");
        searchableIngredients.add("almond");
        searchableIngredients.add("khoa");
        searchableIngredients.add("cane sugar");
        searchableIngredients.add("dry coconut");
        searchableIngredients.add("coconut sugar");
        searchableIngredients.add("cinnamon");
        searchableIngredients.add("buckwheat flour");
        searchableIngredients.add("baking powder");
        searchableIngredients.add("honey");
        searchableIngredients.add("walnut");
        searchableIngredients.add("vanilla bean paste");
        searchableIngredients.add("cinnamon stick");
        searchableIngredients.add("orange peel");
        searchableIngredients.add("pink peppercorn");
        searchableIngredients.add("clove");
        searchableIngredients.add("yeast");
        searchableIngredients.add("baking soda");
        searchableIngredients.add("egg white");
        searchableIngredients.add("old-fashioned oats");
        searchableIngredients.add("cardamon");
        searchableIngredients.add("almonds");
        searchableIngredients.add("pumpkin seed");
        searchableIngredients.add("buttermilk");
        searchableIngredients.add("coriander seed");
        searchableIngredients.add("cumin seed");
        searchableIngredients.add("cloves");
        searchableIngredients.add("coffee");
        searchableIngredients.add("almond milk");
        searchableIngredients.add("bread flour");
        searchableIngredients.add("rum");
        searchableIngredients.add("marzipan");
        searchableIngredients.add("powdered milk");
        searchableIngredients.add("meringue powder");
        searchableIngredients.add("garlic salt");
        searchableIngredients.add("caviar");
        searchableIngredients.add("crisco");
        searchableIngredients.add("bisquick");
        searchableIngredients.add("pork roast");
        searchableIngredients.add("apple cider vinegar");
        searchableIngredients.add("coleslaw");
        searchableIngredients.add("hamburger bun");
        searchableIngredients.add("pork tenderloin");
        searchableIngredients.add("dijon mustard");
        searchableIngredients.add("bourbon whiskey");
        searchableIngredients.add("rice flour");
        searchableIngredients.add("pizza sauce");
        searchableIngredients.add("mozzarella cheese");
        searchableIngredients.add("yellow mustard");
        searchableIngredients.add("sandwich roll");
        searchableIngredients.add("swiss cheese");
        searchableIngredients.add("pork loin");
        searchableIngredients.add("deli ham");
        searchableIngredients.add("extra virgin coconut oil");
        searchableIngredients.add("andouille sausage");
        searchableIngredients.add("green bell pepper");
        searchableIngredients.add("coarse salt");
        searchableIngredients.add("shrimp");
        searchableIngredients.add("crab");
        searchableIngredients.add("abalone");
        searchableIngredients.add("lobster");
        searchableIngredients.add("pasta shells");
        searchableIngredients.add("ricotta cheese");
        searchableIngredients.add("romano cheese");
        searchableIngredients.add("pizza dough");
        searchableIngredients.add("oscar mayer bacon");
        searchableIngredients.add("white bread");
        searchableIngredients.add("ranch dressing");
        searchableIngredients.add("kidney bean");
        searchableIngredients.add("chicken bone");
        searchableIngredients.add("baby carrot");
        searchableIngredients.add("string bean");
        searchableIngredients.add("cream of chicken soup");
        searchableIngredients.add("parsley flakes");
        searchableIngredients.add("brown hash potato");
        searchableIngredients.add("bacon pieces");
        searchableIngredients.add("sweet pickle");
        searchableIngredients.add("dill pickle");
        searchableIngredients.add("mayonnaise");
        searchableIngredients.add("onion powder");
        searchableIngredients.add("margarine");
        searchableIngredients.add("red vinegar");
        searchableIngredients.add("rice vinegar");
        searchableIngredients.add("vinegar");
        searchableIngredients.add("dressing");
        searchableIngredients.add("white wine");
        searchableIngredients.add("chuck roast");
        searchableIngredients.add("beef bouillon");
        searchableIngredients.add("cremini mushroom");
        searchableIngredients.add("white mushroom");
        searchableIngredients.add("vegetable broth");
        searchableIngredients.add("long-grain rice");
        searchableIngredients.add("purple onion");
        searchableIngredients.add("celery rib");
        searchableIngredients.add("crushed tomato");
        searchableIngredients.add("sugar");
        searchableIngredients.add("cocoa powder");
        searchableIngredients.add("dragon fruit");
        searchableIngredients.add("vegetable oil");
        searchableIngredients.add("vanilla extract");
        searchableIngredients.add("white vinegar");
        searchableIngredients.add("corn starch");
        searchableIngredients.add("milk chocolate chip");
        searchableIngredients.add("avocado oil");
        searchableIngredients.add("mini chocolate chip");
        searchableIngredients.add("maple syrup");
        searchableIngredients.add("white sugar");
        searchableIngredients.add("whipped cream");
        searchableIngredients.add("instant coffee");
        searchableIngredients.add("unsweetened cocoa powder");
        searchableIngredients.add("dry milk powder");
        searchableIngredients.add("dark chocolate chip");
        searchableIngredients.add("peppermint extract");
        searchableIngredients.add("dark chocolate");
        searchableIngredients.add("cashew milk");
        searchableIngredients.add("almond butter");
        searchableIngredients.add("mint extract");
        searchableIngredients.add("chocolate chip");
        searchableIngredients.add("caster sugar");
        searchableIngredients.add("apricot jam");
        searchableIngredients.add("agave nectar");
        searchableIngredients.add("vanilla ice cream");
        searchableIngredients.add("chocolate syrup");
        searchableIngredients.add("dark brown sugar");
        searchableIngredients.add("garnet yam");
        searchableIngredients.add("pecan");
        searchableIngredients.add("chevre");
        searchableIngredients.add("mustard green");
        searchableIngredients.add("white miso");
        searchableIngredients.add("sriracha");
        searchableIngredients.add("baby kale");
        searchableIngredients.add("lemon zest");
        searchableIngredients.add("orange zest");
        searchableIngredients.add("yukon gold potato");
        searchableIngredients.add("molasses");
        searchableIngredients.add("golden raisin");
        searchableIngredients.add("sesame seed");
        searchableIngredients.add("chipotle pepper");
        searchableIngredients.add("red bell pepper");
        searchableIngredients.add("shishito chile");
        searchableIngredients.add("grape tomato");
        searchableIngredients.add("kalamata");
        searchableIngredients.add("plum tomato");
        searchableIngredients.add("smoked paprika");
        searchableIngredients.add("brown lentil");
        searchableIngredients.add("green lentil");
        searchableIngredients.add("red wine vinegar");
        searchableIngredients.add("sprouts");
        searchableIngredients.add("peach");
        searchableIngredients.add("rump roast");
        searchableIngredients.add("sirloin steak");
        searchableIngredients.add("porterhouse steak");
        searchableIngredients.add("t-bone steak");
        searchableIngredients.add("club steak");
        searchableIngredients.add("rib steak");
        searchableIngredients.add("rib roast");
        searchableIngredients.add("chuck roast");
        searchableIngredients.add("stew meat");
        searchableIngredients.add("flank steak");
        searchableIngredients.add("plate steak");
        searchableIngredients.add("short ribs");
        searchableIngredients.add("ground beef");
        searchableIngredients.add("brisket steak");
        searchableIngredients.add("arm roast");
        searchableIngredients.add("pork shoulder");
        searchableIngredients.add("spare ribs");
        searchableIngredients.add("rib chop");
        searchableIngredients.add("pork belly");
        searchableIngredients.add("shoulder steak");
        searchableIngredients.add("loin steak");
        searchableIngredients.add("chicken breast");
        searchableIngredients.add("chicken thigh");
        searchableIngredients.add("bucatini");
        searchableIngredients.add("capellini");
        searchableIngredients.add("fusilli");
        searchableIngredients.add("pici");
        searchableIngredients.add("spaghetti");
        searchableIngredients.add("vermicelli");
        searchableIngredients.add("ziti");
        searchableIngredients.add("bavette");
        searchableIngredients.add("ciriole");
        searchableIngredients.add("fettuccine");
        searchableIngredients.add("lasagne");
        searchableIngredients.add("linguine");
        searchableIngredients.add("pappardelle");
        searchableIngredients.add("gemelli");
        searchableIngredients.add("macaroni");
        searchableIngredients.add("manicotti");
        searchableIngredients.add("penne");
        searchableIngredients.add("rigatoni");
        searchableIngredients.add("rotini");
        searchableIngredients.add("campanelle");
        searchableIngredients.add("farfalle");
        searchableIngredients.add("gigli");
        searchableIngredients.add("gnocchi");
        searchableIngredients.add("anelli");
        searchableIngredients.add("farfalline");
        searchableIngredients.add("orzo");
        searchableIngredients.add("cannelloni");
        searchableIngredients.add("fagottini");
        searchableIngredients.add("ravioli");
        searchableIngredients.add("tortellini");
    }

}
