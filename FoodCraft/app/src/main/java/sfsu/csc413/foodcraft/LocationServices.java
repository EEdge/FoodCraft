package sfsu.csc413.foodcraft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Main class for the location services which pops up if the user does not have location services enabled.
 *
 * @author Robert Chung
 * @version 1.0
 */
public class LocationServices extends Activity {

    private Activity myActivity;

    public LocationServices(Activity context) {
        myActivity = context;
    }

    /**
     * Method that presents a dialog box asking if the user wants to enable location services if it has not been enabled.
     */
    public void show() {

        // Location manager is the instance of the LocationManager object which allows the current activity to obtain the status of the location services.
        LocationManager lm = (LocationManager) myActivity.getSystemService(Context.LOCATION_SERVICE);

        // Boolean variables for whether the gps and the network are enabled.
        boolean gps_enabled = false;
        boolean network_enabled = false;

        // Try catch block if for checking if the network and gps are fully functional.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {

            // The properties for the locations services are provided into the dialog builder and the alert dialog is created.
            String message = myActivity.getString(R.string.locationMessage);
            AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);

            dialog.setMessage(message);
            dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                /**
                 * Method that starts a new activity which brings the user to the location services settings page.
                 *
                 * @param paramDialogInterface The interface of the location services dialog box.
                 * @param paramInt The integer containing the parameter.
                 */
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    myActivity.startActivity(myIntent);
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                /**
                 * Method that closes the dialog box if the user presses cancel.
                 *
                 * @param paramDialogInterface The interface of the location services dialog box.
                 * @param paramInt The integer containing the parameter.
                 */
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });

            dialog.show();
        }
    }
}
