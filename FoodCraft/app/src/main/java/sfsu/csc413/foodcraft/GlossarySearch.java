package sfsu.csc413.foodcraft;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xml.sax.*;
import javax.xml.parsers.*;
import org.xml.sax.helpers.*;

import java.io.IOException;
import java.io.StringReader;

/**
 *
 */
public class GlossarySearch {
    private static final String BIGOVEN_GLOSSARY_ENDPOINT_SEARCH = "http://api.bigoven.com/glossary/byterm/";
    private static final String BIGOVEN_KEY = "vVK4HI1I9NublKJqy5QAEV00J861jtbS";
    private Context context;
    private RecipeDetail detail;
    private GlossarySearch mGlossarySearch;
    String parsedEntry = "";

    GlossarySearch(Context context, RecipeDetail detail){
        this.context = context;
        this.detail = detail;
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

                        Log.i("GLOSSARY_SEARCH", "Glossary Search Request successful");

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

    public String processResponse(String response) throws SAXException, ParserConfigurationException, IOException {

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

        return parsedEntry;
    }

}
