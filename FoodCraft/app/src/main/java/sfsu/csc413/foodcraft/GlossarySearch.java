package sfsu.csc413.foodcraft;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 *
 */
public class GlossarySearch {
    private static final String BIGOVEN_GLOSSARY_ENDPOINT_SEARCH = "http://api.bigoven.com/glossary/byterm/";
    private static final String BIGOVEN_KEY = "vVK4HI1I9NublKJqy5QAEV00J861jtbS";
    private Context context;
    private RecipeDetail detail;

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

    public StringRequest requestGlossaryResponse(String url){
        // String result;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sendDefinition(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("GLOSSARY_SEARCH", "Glossary Search Request Unsuccessful");
            }
        });
        // Add the request to the RequestQueue.
        VolleyRequest.getInstance(context).addToRequestQueue(stringRequest);

        return stringRequest;

    }

    public String sendDefinition(String response){
        return response;
    }

}
