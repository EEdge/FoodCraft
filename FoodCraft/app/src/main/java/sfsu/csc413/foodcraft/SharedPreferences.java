package sfsu.csc413.foodcraft;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SharedPreferences extends AppCompatActivity {

    CheckBox salt, sugar, butter, eggs, pepper, water, flour;
    Button buttonSave;

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
        buttonSave = (Button) findViewById(R.id.button1);

        android.content.SharedPreferences sharedprefs = getSharedPreferences("myprefs", MODE_PRIVATE);

        String Ingredient1 = sharedprefs.getString(salt.getText().toString(), null);
        if(Ingredient1!=null) {
            salt.setChecked(true);
        }
        String Ingredient2 = sharedprefs.getString(sugar.getText().toString(), null);
        if(Ingredient2!=null) {
            sugar.setChecked(true);
        }
        String Ingredient3 = sharedprefs.getString(butter.getText().toString(), null);
        if(Ingredient3!=null) {
            butter.setChecked(true);
        }
        String Ingredient4 = sharedprefs.getString(eggs.getText().toString(), null);
        if(Ingredient4!=null) {
            eggs.setChecked(true);
        }
        String Ingredient5 = sharedprefs.getString(pepper.getText().toString(), null);
        if(Ingredient5!=null) {
            pepper.setChecked(true);
        }
        String Ingredient6 = sharedprefs.getString(water.getText().toString(), null);
        if(Ingredient6!=null) {
            pepper.setChecked(true);
        }
        String Ingredient7 = sharedprefs.getString(flour.getText().toString(), null);
        if(Ingredient7!=null) {
            pepper.setChecked(true);
        }


    }

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
            salt.setChecked(false);// handle the value
        }
        if (sugar.isChecked()) {
            editor.putString(sugar.getText().toString(), String.valueOf(sugar.isChecked()));
            sugar.setChecked(true);
        } else {
            sharedprefs.edit().remove(sugar.getText().toString()).commit();
            sugar.setChecked(false);// handle the value
        }

        if (butter.isChecked()) {
            editor.putString(butter.getText().toString(), String.valueOf(butter.isChecked()));
            butter.setChecked(true);
        } else {
            sharedprefs.edit().remove(butter.getText().toString()).commit();
            butter.setChecked(false);// handle the value
        }

        if (eggs.isChecked()) {
            editor.putString(eggs.getText().toString(), String.valueOf(eggs.isChecked()));
            eggs.setChecked(true);
        } else {
            sharedprefs.edit().remove(eggs.getText().toString()).commit();
            eggs.setChecked(false);// handle the value
        }

        if (pepper.isChecked()) {
            editor.putString(pepper.getText().toString(), String.valueOf(pepper.isChecked()));
            pepper.setChecked(true);
        } else {
            sharedprefs.edit().remove(pepper.getText().toString()).commit();
            pepper.setChecked(false);// handle the value
        }

        if (water.isChecked()) {
            editor.putString(water.getText().toString(), String.valueOf(water.isChecked()));
            water.setChecked(true);
        } else {
            sharedprefs.edit().remove(water.getText().toString()).commit();
            water.setChecked(false);// handle the value
        }

        if (flour.isChecked()) {
            editor.putString(flour.getText().toString(), String.valueOf(flour.isChecked()));
            flour.setChecked(true);
        } else {
            sharedprefs.edit().remove(flour.getText().toString()).commit();
            flour.setChecked(false);// handle the value
        }

        editor.apply();

        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();

    }

    /*public void apply(View view) {

        //This creates an object for SharedPreferences.
        android.content.SharedPreferences sharedprefs = getSharedPreferences("myprefs", MODE_PRIVATE);

        //This is used to get data from the file.
        String I1 = sharedprefs.getString(salt.getText().toString(), null);
        String I2 = sharedprefs.getString(sugar.getText().toString(), null);
        String I3 = sharedprefs.getString(butter.getText().toString(), null);
        String I4 = sharedprefs.getString(eggs.getText().toString(), null);
        String I5 = sharedprefs.getString(pepper.getText().toString(), null);
        String I6 = sharedprefs.getString(water.getText().toString(), null);
        String I7 = sharedprefs.getString(flour.getText().toString(), null);



        StringBuilder builder1 = new StringBuilder();
        builder1.append(I1 + "\n" + I2 + "\n" + I3 + "\n" + I4 + "\n" + I5 +"\n" + I6 +"\n" + I7);
        showMessage("Data", builder1.toString());
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }*/

}
