package sfsu.csc413.foodcraft;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Code taken from Yelp at https://github.com/Yelp/yelp-api/tree/master/v2/java
 * edited by Evan Edge
 *
 * Generic service provider for two-step OAuth10a.
 */
public class TwoStepOAuth extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token arg0) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}