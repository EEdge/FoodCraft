package sfsu.csc413.foodcraft;

import android.annotation.TargetApi;
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

import java.util.List;

import static sfsu.csc413.foodcraft.R.layout.upc_scan;


public class UPCFragment extends Fragment {

    private CompoundBarcodeView barcodeView;

    BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            UPCRequest barcode_scanner = new UPCRequest("https://api.outpan.com/v1/products/",
                    "name", "459563971cd36022e52e0c936ce2836c");
            if (result != null) {
                barcode_scanner.craftUPCRequest(result.toString(), getActivity(), taskCallback);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    TaskCallback taskCallback = new TaskCallback() {
        @Override
        public void onTaskCompleted(List<String> result) {
            if (result.size() == 0){

            }
            else ((IngredientSearch) getActivity()).addselectedFoods(result);
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
        view = inflater.inflate(upc_scan, container, false);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void scanFromFragment(View view) {
        barcodeView = (CompoundBarcodeView) view.findViewById(R.id.barcode_scanner);
        barcodeView.initializeFromIntent(IntentIntegrator.forFragment(this).createScanIntent());
        barcodeView.resume();
        barcodeView.decodeSingle(callback);
    }
}
