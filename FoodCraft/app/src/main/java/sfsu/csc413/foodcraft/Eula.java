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

    public Eula(Activity context) {
        myActivity = context;
    }

    /**
     * Method which gathers the information of the application.
     * @return pi containing the package information.
     */
    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = myActivity.getPackageManager().getPackageInfo(myActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    /**
     * Method that checks if the user has previously accepted the EULA; EULA dialog will appear if the user has not.
     */
    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myActivity);
        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
        if (hasBeenShown == false) {

            String title = myActivity.getString(R.string.eulaTitle);

            String message = myActivity.getString(R.string.updates) + "\n\n" + myActivity.getString(R.string.eula);

            /**
             * Method that creates the entire dialog for the EULA.
             */
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
            builder.create().show();
        }
    }

}