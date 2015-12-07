package sfsu.csc413.foodcraft;

import android.content.Context;
import android.content.Intent;
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

/**
 *
 */
public class GlossarySearch{
    private static final String BIGOVEN_GLOSSARY_ENDPOINT_SEARCH = "http://api.bigoven.com/glossary/byterm/";
    private static final String BIGOVEN_KEY = "vVK4HI1I9NublKJqy5QAEV00J861jtbS";
    Context context ;
    RecipeDetail detail;
    GlossaryActivity glossaryActivity;
    String parsedEntry = "";
    TextView textView;

    GlossarySearch(Context context, RecipeDetail detail, TextView textView){
        this.context = context;
        this.detail = detail;
        this.textView = textView;
    }

    public static String ingredientGlossarySearchURL(String ingredient) {

        String url = BIGOVEN_GLOSSARY_ENDPOINT_SEARCH + ingredient
                + "?api_key=" + BIGOVEN_KEY;

        Log.i("API_CALL", url);

        return url;
    }

    public void requestGlossaryResponse(String url){

        // String result;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            try {
                                processResponse(response);
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

    public void processResponse(String response)
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
        String id = entry.getGlossaryEntryID();
        parsedEntry = "<h3>" + entry.getTerm() + "</h3>";
        parsedEntry += entry.getDefinition();

        if(id.equals("0"))
            textView.setText("Ingredient not found");
        else
            textView.setText(Html.fromHtml(String.valueOf(this.parsedEntry)));

    }

    public static String searchIngredient(RecipeDetail mRecipeDetail, int position){
        String cleanedIngredient = Utilities.cleanString(mRecipeDetail.ingredients.get(position));
        cleanedIngredient = cleanedIngredient.replace(" ", "%20");
        String search = GlossarySearch.ingredientGlossarySearchURL(cleanedIngredient);

        return search;
    }

}
