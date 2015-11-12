package sfsu.csc413.foodcraft;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button button_datastore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_datastore = (Button)findViewById(R.id.button1);

        new Eula(this).show();
        new LocationServices(this).show();
    }
    public void dataStore (View view) {
        startActivity(new Intent(getApplicationContext(), SharedPreferences.class));
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

    /**
     * This method is called when the "search an ingredient in yummly" button is pressed.
     * It goes to the Yummly search activity
     * @param v
     */
    public void yummlyButtonListener(View v){
        Intent intent = new Intent("sfsu.csc413.foodcraft.YummlySearchActivity");
        startActivity(intent);
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

    //This is Paul Klein's quick and dirty Fragment class
    public static class ScanFragment extends Fragment {
        private String toast;

        public ScanFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            displayToast();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scan, container, false);
            Button scan = (Button) view.findViewById(R.id.scan_from_fragment);
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanFromFragment();
                }
            });
            return view;
        }

        public void scanFromFragment() {
            IntentIntegrator integrator = new IntentIntegrator(this.getActivity());
            integrator.setCaptureActivity(UPCScan.class);
            integrator.initiateScan();
        }

        private void displayToast() {
            if(getActivity() != null && toast != null) {
                Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
                toast = null;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            UPCRequest barcode_scanner = new UPCRequest("https://api.outpan.com/v1/products/",
                    "name", "459563971cd36022e52e0c936ce2836c");
            if (scanResult != null) {
                barcode_scanner.craftUPCRequest(scanResult.getContents(), this.getActivity());
            }
        }
    }

    public void searchActivitySwitch(View view) {
        Intent intent = new Intent(MainActivity.this, IngredientSearch.class);
        startActivity(intent);
    }
}
