package sfsu.csc413.foodcraft;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraInstance;

import java.util.List;

import static sfsu.csc413.foodcraft.R.layout.fragment_test_photo;


public class TestPhotoFragment extends Fragment {

    private String toast;
    private CompoundBarcodeView barcodeView;



        BarcodeCallback callback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    barcodeView.setStatusText(result.getText());
                }
                //Added preview of scanned barcode
                ImageView imageView = (ImageView) getView().findViewById(R.id.barcode_scanner_image);
                imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        };



    public TestPhotoFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        displayToast();
        scanFromFragment(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = (View) inflater.inflate(fragment_test_photo, container, false);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void scanFromFragment(View view) {
        barcodeView = (CompoundBarcodeView) view.findViewById(R.id.barcode_scanner);
        barcodeView.initializeFromIntent(IntentIntegrator.forFragment(this).createScanIntent());
        barcodeView.resume();
        barcodeView.decodeSingle(callback);
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
