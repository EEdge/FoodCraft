package sfsu.csc413.foodcraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xml.sax.*;

import javax.xml.parsers.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Glossary Search class gets a glossary entry from a ingredient string. It formats an url for a
 * request to BigOven which is used in a string request. The string is processed and the textview in
 * GlossaryActivity is set
 *
 * @author Maria Lienkaemper
 */
public class GlossarySearch extends Activity{
    private static final String BIGOVEN_GLOSSARY_ENDPOINT_SEARCH = "http://api.bigoven.com/glossary/byterm/";
    private static final String BIGOVEN_KEY = "XXXXXXXXXXXXX";
    public static final String GLOSSARY = "sfsu.csc413.foodcraft.GLOSSARY";
    Context context;
    RecipeDetail detail;
    private String parsedEntry = "";
    TaskCallback taskCallback;

    TextView textView;
    String idGlossary;
    public boolean responseFinished = false;

    /**
     * Constructor for Glossary Search class
     *
     * @param context
     * @param detail
     */
    GlossarySearch(Context context, RecipeDetail detail, TaskCallback taskCallback) {
        this.context = context;
        this.detail = detail;
        this.taskCallback = taskCallback;
    }



    /**
     * Formats a url string to send request to BigOven
     *
     * @param ingredient
     * @return url
     */
    private static String ingredientGlossarySearchURL(String ingredient) {

        String url = BIGOVEN_GLOSSARY_ENDPOINT_SEARCH + ingredient
                + "?api_key=" + BIGOVEN_KEY;

        Log.i("API_CALL", url);

        return url;
    }

    /**
     * Sends string request through Volley. It adds request to Volley queue and gets the text of the
     * xml file response
     *
     * @param url
     */
    private void requestGlossaryResponse(String url) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            String text = processResponse(response);
                            if (idGlossary.equals("0"))
                                Toast.makeText(context, "Ingredient not found", Toast.LENGTH_SHORT).show();
                            else {

                                taskCallback.onTaskCompleted(text);

                            }
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.i("GLOSSARY_SEARCH", "Glossary Search Request Successful");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("GLOSSARY_SEARCH", "Glossary Search Request Unsuccessful");
            }
        });
        // Add the request to the RequestQueue.
        VolleyRequest.getInstance(context).addToRequestQueue(stringRequest);

    }

    /**
     * Gets the xml response from BigOven and parses it
     *
     * @param response
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * @return parsedEntry
     */
    private String processResponse(String response)
            throws SAXException, ParserConfigurationException, IOException {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();

        GlossaryXmlHandler mXMLHandler = new GlossaryXmlHandler();
        xr.setContentHandler(mXMLHandler);
        InputSource inStream = new InputSource();
        inStream.setCharacterStream(new StringReader(response));
        xr.parse(inStream);

        GlossaryData entry = mXMLHandler.entry;
        idGlossary = entry.getGlossaryEntryID();
        parsedEntry = "<h3>" + entry.getTerm() + "</h3>";
        parsedEntry += entry.getDefinition();
        responseFinished = true;

        return parsedEntry;
    }

    /**
     * Cleans ingredient string of unnecessary phrases before using it in a search
     * Uses Utilities class to format string
     *
     * @param mRecipeDetail
     * @param position
     * @return search
     */
    private static String searchIngredient(RecipeDetail mRecipeDetail, int position) {

        String cleanedIngredient = Utilities.cleanString(mRecipeDetail.ingredients.get(position));
        cleanedIngredient = cleanedIngredient.replace(" ", "%20");
        String search = GlossarySearch.ingredientGlossarySearchURL(cleanedIngredient);

        return search;
    }

    /*
    private void parseIngredient(){

        String ingredient = mRecipeDetail.ingredients.get(position);
        String[] ingredientSplit = ingredient.split(" ");
        List<String> results = new ArrayList<>();
        for(int i = 0; i < ingredientSplit.length-2; i++ ){
            String previous = ingredientSplit[i-1] + " " + ingredientSplit[i];
            String next =  ingredientSplit[i] + " " + ingredientSplit[i+1];
            if(ingredient.contains(previous.toLowerCase()))
                results.add(previous.toLowerCase());
            if(ingredient.contains(next.toLowerCase()))
                results.add(next.toLowerCase());
            if(results.size() == 0)
                results.add(null);
        }
    }
    */

    /**
     * This method is used to start a glossary search in another activity
     * Uses search ingredient method and request glossary method
     *
     * @param recipeDetail
     * @param position
     */
    public void startGlossarySearch(RecipeDetail recipeDetail, int position) {

        GlossarySearch glossarySearch = new GlossarySearch(context, recipeDetail, taskCallback);
        String search = GlossarySearch.searchIngredient(recipeDetail, position);
        glossarySearch.requestGlossaryResponse(search);
    }


}
