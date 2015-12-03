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

import static sfsu.csc413.foodcraft.R.layout.upc_scan;


public class UPCFragment extends Fragment {

    private CompoundBarcodeView barcodeView;
    UPCRequest barcode_scanner = new UPCRequest(getActivity());

    BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            UPCRequest barcode_scanner = new UPCRequest(getActivity());
            //TODO HANDLE NULL RESPONSE
            if (result != null) {
                if (barcode_scanner.getCachedCode(result.toString()).product_title != null){
                    List<UPCObject> objlist = new ArrayList<UPCObject>(){};
                    objlist.add(barcode_scanner.getCachedCode(result.toString()));
                    ((IngredientSearch) getActivity()).addselectedFoods(objlist, true);
                }
                else {
                    barcode_scanner.craftUPCRequest(result.toString(), getActivity(), taskCallback);
                }
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    TaskCallback taskCallback = new TaskCallback() {
        @Override
        public void onTaskCompleted(List<UPCObject> result) {
            ((IngredientSearch) getActivity()).addselectedFoods(result, false);
            barcodeView.pause();
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
    public void addtoDatabase(String code, String title, Context context){
        barcode_scanner.insertCachedCode(code, title, context);
    }
}
