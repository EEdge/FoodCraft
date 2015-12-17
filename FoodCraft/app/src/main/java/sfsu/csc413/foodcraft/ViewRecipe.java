package sfsu.csc413.foodcraft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toolbar;

/**
 * The ViewRecipe class displays the Recipe instructions in a WebView.
 * When the recipe instruction URL is passed by the RecipeDetailActivity class, the ViewRecipe class opens the URL in a WebView.
 *
 * @author: Sapan Tiwari
 * @version: 1.0
 */
public class ViewRecipe extends AppCompatActivity {
    public static final String RECIPE_URL = "sfsu.csc413.foodcraft.RECIPE_URL";

    private Toolbar toolbar;

    WebView wb;
    String recipeURL;

    /**
     * This method is called when the activity is first created.
     *
     * @param savedInstanceState Information about the current state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        wb = (WebView) findViewById(R.id.webview);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setBuiltInZoomControls(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setActionBar(toolbar);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(true);

            // https://stackoverflow.com/questions/16240605/change-action-bar-title-color -- Brilliant!
            getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Viewing Recipe...</font>"));

            getActionBar().setElevation(7);
        }

        //Unpack the bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeURL = bundle.getString(RECIPE_URL);

        wb.loadUrl(recipeURL);
        Log.i("VIEWRECIPEURL", recipeURL);

        wb.setWebViewClient(new HelloWebViewClient());
    }


    //This method provide more control over where a clicked link loads.
    private class HelloWebViewClient extends WebViewClient {
        //Give the host application a chance to take over the control when a new url is about to be loaded in the current WebView.
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /**
     * This method allows the user to go back to the previous page in the WebView.
     *
     * @param keyCode The value in event.getKeyCode().
     * @param event   Description of the key event.
     * @return If the event is handled, return true. If you want to allow the event to be handled by the next receiver, return false.
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wb.canGoBack()) {
            wb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_recipe, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
