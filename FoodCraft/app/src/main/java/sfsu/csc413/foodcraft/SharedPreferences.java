package sfsu.csc413.foodcraft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * The SharedPreferences class provides a general framework that allows the user to save and retrieve persistent key-value pairs of primitive data types.
 * The data will persist across user sessions (even if your application is killed).
 *
 * @author: Sapan Tiwari
 * @version: 1.0
 */
public class SharedPreferences extends AppCompatActivity {

    CheckBox salt, sugar, butter, eggs, pepper, water, flour;
    Button buttonSave, buttonUndo;

    /**
     * This method is called when the activity is first created.
     *
     * @param savedInstanceState Information about the current state of the activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences);
        salt = (CheckBox) findViewById(R.id.salt);
        sugar = (CheckBox) findViewById(R.id.sugar);
        butter = (CheckBox) findViewById(R.id.butter);
        eggs = (CheckBox) findViewById(R.id.eggs);
        pepper = (CheckBox) findViewById(R.id.pepper);
        water = (CheckBox) findViewById(R.id.water);
        flour = (CheckBox) findViewById(R.id.flour);
        buttonSave = (Button) findViewById(R.id.save);
        buttonUndo = (Button) findViewById(R.id.undo);
        savedState();


    }

    /**
     * This method retrieves the data stored in SharedPreferences file.
     * If the data is found about a particular ingredient, the corresponding checkbox is checked.
     */
    public void savedState() {
        android.content.SharedPreferences sharedprefs = getSharedPreferences("myprefs", MODE_PRIVATE);

        String Ingredient1 = sharedprefs.getString(salt.getText().toString(), null);
        if (Ingredient1 != null) {
            salt.setChecked(true);
        } else {
            salt.setChecked(false);
        }

        String Ingredient2 = sharedprefs.getString(sugar.getText().toString(), null);
        if (Ingredient2 != null) {
            sugar.setChecked(true);
        } else {
            sugar.setChecked(false);
        }

        String Ingredient3 = sharedprefs.getString(butter.getText().toString(), null);
        if (Ingredient3 != null) {
            butter.setChecked(true);
        } else {
            butter.setChecked(false);
        }

        String Ingredient4 = sharedprefs.getString(eggs.getText().toString(), null);
        if (Ingredient4 != null) {
            eggs.setChecked(true);
        } else {
            eggs.setChecked(false);
        }

        String Ingredient5 = sharedprefs.getString(pepper.getText().toString(), null);
        if (Ingredient5 != null) {
            pepper.setChecked(true);
        } else {
            pepper.setChecked(false);
        }

        String Ingredient6 = sharedprefs.getString(water.getText().toString(), null);
        if (Ingredient6 != null) {
            water.setChecked(true);
        } else {
            water.setChecked(false);
        }

        String Ingredient7 = sharedprefs.getString(flour.getText().toString(), null);
        if (Ingredient7 != null) {
            flour.setChecked(true);
        } else {
            flour.setChecked(false);
        }

    }

    /**
     * This method is called when the user clicks Save button.
     * If a particular checkbox is checked, this method stores the information about corresponding ingredient
     * in the SharedPreferences file in key-value pair.
     *
     * @param view Called when a view has been clicked.
     */
    public void saveData(View view) {


        //This creates an object for SharedPreferences and determines whether to create a private of public file when saving the SharedPreferences file.
        //The default is MODE_PRIVATE which only allows access to the file by the app that created it.
        android.content.SharedPreferences sharedprefs = getSharedPreferences("myprefs", MODE_PRIVATE);

        //This creates object for Editor.
        android.content.SharedPreferences.Editor editor = sharedprefs.edit();

        //This method is used to put the String value in SharedPreferences and key is used to retrieve the value.
        if (salt.isChecked()) {
            editor.putString(salt.getText().toString(), String.valueOf(salt.isChecked()));
            salt.setChecked(true);
        } else {
            sharedprefs.edit().remove(salt.getText().toString()).commit();
            salt.setChecked(false);
        }
        if (sugar.isChecked()) {
            editor.putString(sugar.getText().toString(), String.valueOf(sugar.isChecked()));
            sugar.setChecked(true);
        } else {
            sharedprefs.edit().remove(sugar.getText().toString()).commit();
            sugar.setChecked(false);
        }

        if (butter.isChecked()) {
            editor.putString(butter.getText().toString(), String.valueOf(butter.isChecked()));
            butter.setChecked(true);
        } else {
            sharedprefs.edit().remove(butter.getText().toString()).commit();
            butter.setChecked(false);
        }

        if (eggs.isChecked()) {
            editor.putString(eggs.getText().toString(), String.valueOf(eggs.isChecked()));
            eggs.setChecked(true);
        } else {
            sharedprefs.edit().remove(eggs.getText().toString()).commit();
            eggs.setChecked(false);
        }

        if (pepper.isChecked()) {
            editor.putString(pepper.getText().toString(), String.valueOf(pepper.isChecked()));
            pepper.setChecked(true);
        } else {
            sharedprefs.edit().remove(pepper.getText().toString()).commit();
            pepper.setChecked(false);
        }

        if (water.isChecked()) {
            editor.putString(water.getText().toString(), String.valueOf(water.isChecked()));
            water.setChecked(true);
        } else {
            sharedprefs.edit().remove(water.getText().toString()).commit();
            water.setChecked(false);
        }

        if (flour.isChecked()) {
            editor.putString(flour.getText().toString(), String.valueOf(flour.isChecked()));
            flour.setChecked(true);
        } else {
            sharedprefs.edit().remove(flour.getText().toString()).commit();
            flour.setChecked(false);
        }

        editor.apply();

        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();

    }

    /**
     * This method is called when the user clicks Undo button.
     * This method restores the checkboxes to the last saved state.
     *
     * @param view Called when a view has been clicked.
     */
    public void undoSave(View view) {
        savedState();

    }

}
