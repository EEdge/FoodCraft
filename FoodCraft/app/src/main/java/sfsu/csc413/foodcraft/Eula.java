package sfsu.csc413.foodcraft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

//Reference : http://www.donnfelker.com/android-a-simple-eula-for-your-android-apps/

/**
 * Main Activity for popping up a EULA dialog depending on whether the user has previously accepted the terms of use.
 *
 * @author Robert Chung
 * @version 1.0 October 15, 2015.
 */
public class Eula {

    private String EULA_PREFIX = "eula_";
    private Activity myActivity;

    /**
     * Class that allows this Eula java class to set the current activity to the context of the application.
     * @param context Information about the application.
     */
    public Eula(Activity context) {
        myActivity = context;
    }

    private PackageInfo getPackageInfo() {

        // Set the package information to null begin with.
        PackageInfo pi = null;

        // Try catch block for when the information and contents about the package are obtained.
        try {
            pi = myActivity.getPackageManager().getPackageInfo(myActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * Method that checks if the user has previously accepted the EULA; EULA dialog will appear if the user has not. This is possible using the shared preferences inherent
     * in the android support. The dialog box for the EULA is created and customized here. If the user chooses to accept the EULA, the application will store the decision
     * and will not appear the next time the application is started. If the user decides to reject the EULA, the application will be stopped and closed.
     */
    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        //eulaKey for keeping track of the version number.
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;

        //Shared preference object for recognizing whether the user has accepted the EULA already.
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myActivity);

        // Boolean variable that works with the shared preferences object for deciding whether to display the EULA to the user.
        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);

        // If it is determined that the user has not previously accepted the EULA, a new EULA dialog will be created and presented. It will contain the title, message, and the postive
        // and negative buttons. Once the positive button has been selected, the shared preferences editor will store that data.
        if (hasBeenShown == false) {

            String title = myActivity.getString(R.string.eulaTitle);
            String message = myActivity.getString(R.string.updates) + "\n\n" + myActivity.getString(R.string.eula);

            AlertDialog.Builder builder = new AlertDialog.Builder(myActivity)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Accept", new Dialog.OnClickListener() {

                        /**
                         * Method that changes the SharedPreferences to true if the user accepts the EULA.
                         * @param dialogInterface The design of the positive button.
                         * @param i The integer containing the dialog.
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(eulaKey, true);
                            editor.commit();
                            dialogInterface.dismiss();
                        }
                    })

                    // If the user decides to select the negative button for the EULA dialog, then the application will be closed.
                    .setNegativeButton("Cancel", new Dialog.OnClickListener() {

                        /**
                         * Method that causes the application to close if the EULA has not been accepted.
                         * @param dialog The design of the negative button.
                         * @param which The integer containing the dialog.
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myActivity.finish();
                        }

                    });

            // Builder to create the EULA dialog box pop up.
            builder.create().show();
        }
    }

}