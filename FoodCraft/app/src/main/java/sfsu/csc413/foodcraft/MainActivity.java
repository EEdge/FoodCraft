package sfsu.csc413.foodcraft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Eula(this).show();
        new LocationServices(this).show();
    }

    /** This is the method called when a user hit the "Scan a UPC Code" button in our main activity.
     *  It initiates the scanning using the ZXing intentIntegrator
     *  Calls onActivityResult when the scan is completed
     * @param view
     *  Written by Paul Klein
     */
    public void scan_barcode(View view){
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.initiateScan();
    }

    /** This method is called when the scan initiated by scan_Barcode completes. It parses the scanned UPC code,
     *      and hands it off to the UPCRequest class to fetch the item from the UPC database.
     *  The ingredient is returned as a toast.
     *  TODO: When a ingredient is returned, add that ingredient to the current ingredient list.
     * @param requestCode
     * @param resultCode
     * @param intent
     * Written by Paul Klein
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        UPCRequest barcode_scanner = new UPCRequest("https://api.outpan.com/v1/products/",
                "name", "459563971cd36022e52e0c936ce2836c");
        if (scanResult != null) {
            barcode_scanner.craftUPCRequest(scanResult.getContents(), this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
