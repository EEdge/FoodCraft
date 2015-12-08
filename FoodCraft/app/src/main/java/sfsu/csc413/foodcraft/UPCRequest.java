package sfsu.csc413.foodcraft;

import android.content.*;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the class that handles the calling of UPC API (Outpan) and Toasting the returned product name.
 * Written by Paul Klein
 */
public class UPCRequest {
    private static final String UPC_KEYWORD = "name";
    private static final String UPC_KEY = "459563971cd36022e52e0c936ce2836c";
    private static final String UPC_URL = "https://api.outpan.com/v1/products/";
    private TaskCallback callback;
    private Context myActivity;

    /**
     * The constructor for the UPCRequest class that establishes our variables.
     * Written by Paul Klein
     */
    public UPCRequest(Context myActivity){
        this.myActivity = myActivity;
    }

    /**
     * This method crafts the JSONObjectRequest for our UPC API and adds it to the VolleyRequest request queue.
     * Once there is a response to the request, it sends a toast of the product title.
     * @param upc_code
     * @param context
     * Written by Paul Klein
     *  TODO: Parse Item for Ingredients, and return that ingredient as a String instead of a toast.
     */
    public void craftUPCRequest(final String upc_code, final Context context, TaskCallback callback){
        final String requesturl = UPC_URL + upc_code + "/" + UPC_KEYWORD;
        this.callback = callback;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requesturl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.getString(UPC_KEYWORD).equals("null")) {
                                parse_response(response.getString(UPC_KEYWORD), upc_code);
                            }
                            else {
                                parse_response("", upc_code);
                            }

                        } catch (Exception exception) {
                            VolleyLog.e("Error: ", exception.toString());
                            Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:", UPC_KEY);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        VolleyRequest.getInstance(context).addToRequestQueue(jsObjRequest);
    }
    public UPCObject getCachedCode(String upc){
        //get product name from database with matching UPC
        UPCDatabase database = new UPCDatabase(myActivity);
        String result = database.getProductByCode(upc);
        UPCObject obj = new UPCObject(upc, result, result);
        return obj;
    }
    public void insertCachedCode(String upc, String title, Context context){
        UPCDatabase database = new UPCDatabase(context);
        database.insert(upc, title);
    }
    public void parse_response(String scanned_product, String upc_code){
        IngredientList list = new IngredientList();
        String[] split_text = scanned_product.split("\\s+");
        List<UPCObject> results = new ArrayList<UPCObject>();
        if (scanned_product.length() < 1){
            results.add(new UPCObject(upc_code, null, scanned_product));
        }
        else {
            for (String part: split_text) {
                if (list.Contains(part.toLowerCase())){
                    results.add(new UPCObject(upc_code, part.toLowerCase(), scanned_product));
                }
            }
            for (int i = 1; i <= split_text.length - 2; i++){
                String previouscombo = split_text[i - 1] + " " + split_text[i];
                String postcombo = split_text[i] + " " + split_text[i + 1];
                if (list.Contains(previouscombo.toLowerCase())){
                    results.add(new UPCObject(upc_code, previouscombo.toLowerCase(), scanned_product));
                }
                if (list.Contains(postcombo.toLowerCase())){
                    results.add(new UPCObject(upc_code, postcombo.toLowerCase(), scanned_product));
                }
            }
            if (results.size() == 0){
                results.add(new UPCObject(upc_code, null, scanned_product));
            }
        }
        callback.onTaskCompleted(results);
    }
}
