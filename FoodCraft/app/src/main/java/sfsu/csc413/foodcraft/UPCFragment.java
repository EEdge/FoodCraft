package sfsu.csc413.foodcraft;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.ArrayList;
import java.util.List;

import static sfsu.csc413.foodcraft.R.layout.fragment_scan;

/**
 * This fragment will hold the BarcodeScanner (Camera) preview. It took a lot of effort to reduce
 * the fullscreen preview into a tiny fragment that we can swap in and out.
 * @file:UPCFragment.java
 * @author: Paul Klein
 * @version: 1.0
 */
public class UPCFragment extends Fragment {

    private CompoundBarcodeView barcodeView;
    UPCRequest barcode_scanner = new UPCRequest(getActivity());

    //This is the barcode callback that is called when a barcode is scanned and decoded
    BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            UPCRequest barcode_scanner = new UPCRequest(getActivity());
            if (result != null) {
                //This logic check is looking to see if we already have cached the UPC code.
                //We will always give priority to the cached data.
                if (barcode_scanner.getCachedCode(result.toString()).product_title != null) {
                    List<UPCObject> objlist = new ArrayList<UPCObject>() {
                    };
                    objlist.add(barcode_scanner.getCachedCode(result.toString()));
                    ((IngredientSearch) getActivity()).addselectedFoods(objlist, true);
                } else {
                    barcode_scanner.craftUPCRequest(result.toString(), getActivity(), taskCallback);
                }
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    //This callback passes the completed scan data from the UPC fragment into the IngredientSearch.
    TaskCallback taskCallback = new TaskCallback() {
        @Override
        public void onTaskCompleted(List<UPCObject> result) {
            ((IngredientSearch) getActivity()).addselectedFoods(result, false);
            barcodeView.pause();
        }

        @Override
        void onTaskCompleted(ArrayList<Place> result) {

        }

        public void onTaskCompleted(UPCObject result, boolean cached) {
            List<UPCObject> resultlist = new ArrayList<UPCObject>();
            resultlist.add(result);
            ((IngredientSearch) getActivity()).addselectedFoods(resultlist, false);
            barcodeView.pause();
        }
    };

    public UPCFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        scanFromFragment(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        scanFromFragment(getView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(fragment_scan, container, false);
        return view;
    }

    /**
     * This function begins the scanning of barcodes using the scanner. It uses the callback to launch
     * the API call once a barcode is captured and decoded.
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void scanFromFragment(View view) {
        barcodeView = (CompoundBarcodeView) view.findViewById(R.id.barcode_scanner);
        barcodeView.initializeFromIntent(IntentIntegrator.forFragment(this).createScanIntent());
        barcodeView.resume();
        barcodeView.decodeSingle(callback);
    }

    /** This function is called from within the IngredientSearch class. It is used to add new
     *  code - product title combinations to our database, for later quick access.
     * @param code    UPC code
     * @param title   User entered title
     * @param context
     */
    public void addtoDatabase(String code, String title, Context context) {
        barcode_scanner.insertCachedCode(code, title, context);
    }
}
