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
import android.widget.Button;
import android.widget.Toolbar;

public class ViewRecipe extends AppCompatActivity {
    public static final String RECIPE_URL = "sfsu.csc413.foodcraft.RECIPE_URL";

    private Toolbar toolbar;

    WebView wb;
    String recipeURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        wb=(WebView)findViewById(R.id.webview);
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
        }        /* Unpack */
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        recipeURL = (String) bundle.getString(RECIPE_URL);

        wb.loadUrl(recipeURL);
        Log.i("VIEWRECIPEURL", recipeURL);


        wb.setWebViewClient(new HelloWebViewClient());
    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public boolean onKeyDown (int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wb.canGoBack())
        {
            wb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_recipe, menu);
        return true;
    }

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
