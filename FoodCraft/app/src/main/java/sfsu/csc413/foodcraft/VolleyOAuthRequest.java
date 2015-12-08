package sfsu.csc413.foodcraft;

/**
 * Created by pklein on 12/6/15.
 */
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VolleyOAuthRequest<T> extends Request<T> {

    private HashMap<String, String> params;
    private OAuthRequest oAuthRequest;
    private Token accessToken;
    private OAuthService service;

    public VolleyOAuthRequest(int method, String path, Response.ErrorListener errorListener, String consumerKey, String consumerSecret,
                              String token, String tokenSecret) {
        super(method, path, errorListener);
        params = new HashMap<String, String>();
        this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
                .apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);

    }

    public void addParameter(String key, String value) {
        params.put(key, value);
    }

    private void buildOAuthRequest() {
        oAuthRequest = new OAuthRequest(getVerb(), super.getUrl());
        for(Map.Entry<String, String> entry : getParams().entrySet()) {
            oAuthRequest.addQuerystringParameter(entry.getKey(), entry.getValue());
        }
        service.signRequest(accessToken, oAuthRequest);
    }

    private Verb getVerb() {
        switch (getMethod()) {
            case Method.GET:
                return Verb.GET;
            case Method.DELETE:
                return Verb.DELETE;
            case Method.POST:
                return Verb.POST;
            case Method.PUT:
                return Verb.PUT;
            default:
                return Verb.GET;
        }
    }

    private String getParameterString() {
        StringBuilder sb = new StringBuilder("?");
        Iterator<String> keys = params.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            sb.append(String.format("&%s=%s", key, params.get(key)));
        }
        return sb.toString();
    }
    @Override
    protected Map<String, String> getParams() {
        return params;
    }

    @Override
    public String getUrl() {
        if(oAuthRequest == null) {
            buildOAuthRequest();

            for(Map.Entry<String, String> entry : oAuthRequest.getOauthParameters().entrySet()) {
                addParameter(entry.getKey(), entry.getValue());
            }
        }
        return super.getUrl() + getParameterString();
    }

    @Override
    protected void deliverResponse(T response) {

    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        return null;
    }
}