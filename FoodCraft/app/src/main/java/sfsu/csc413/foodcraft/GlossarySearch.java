package sfsu.csc413.foodcraft;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

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
    Context context;
    RecipeDetail detail;
    GlossarySearch mGlossarySearch;
    String parsedEntry = "";
    TextView textView;
    public GlossaryActivity activity;


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

    public void requestGlossaryResponse(String url, final RecipeDetail detail){

        // String result;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mGlossarySearch = new GlossarySearch(context, detail, textView);

                        try {
                            processResponse(response, mGlossarySearch);
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

    public String processResponse(String response, GlossarySearch mGlossarySearch)
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
        parsedEntry += entry.getTerm() + '\n';
        parsedEntry += entry.getDefinition();


        //TextView textView = (TextView) this.activity.findViewById(R.id.glossary_entry);
        textView.setText(this.parsedEntry);

        return parsedEntry;
    }

    public GlossarySearch getGlossarySearch(){
        return mGlossarySearch;
    }

}
