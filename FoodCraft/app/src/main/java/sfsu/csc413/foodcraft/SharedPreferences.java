package sfsu.csc413.foodcraft;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SharedPreferences extends AppCompatActivity {

    CheckBox check1, check2, check3, check4, check5;
    Button buttonSave, buttonDisplay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences);
        check1 = (CheckBox)findViewById(R.id.checkBox1);
        check2 = (CheckBox)findViewById(R.id.checkBox2);
        check3 = (CheckBox)findViewById(R.id.checkBox3);
        check4 = (CheckBox)findViewById(R.id.checkBox4);
        check5 = (CheckBox)findViewById(R.id.checkBox5);
        buttonSave = (Button)findViewById(R.id.button1);
        buttonDisplay = (Button)findViewById(R.id.button2);

    }

    public void saveData (View view) {


        //This creates an object for SharedPreferences and determines whether to create a private of public file when saving the SharedPreferences file.
        //The default is MODE_PRIVATE which only allows access to the file by the app that created it.
        android.content.SharedPreferences sharedprefs = getSharedPreferences("myprefs", MODE_PRIVATE);

        //This creates object for Editor.
        android.content.SharedPreferences.Editor editor = sharedprefs.edit();

        //This method is used to put the String value in SharedPreferences and key is used to retrieve the value.
        editor.putString(check1.getText().toString(), String.valueOf(check1.isChecked()));

        editor.putString(check2.getText().toString(), String.valueOf(check2.isChecked()));

        editor.putString(check3.getText().toString(), String.valueOf(check3.isChecked()));

        editor.putString(check4.getText().toString(), String.valueOf(check4.isChecked()));

        editor.putString(check5.getText().toString(), String.valueOf(check5.isChecked()));

        //This saves your data into memory immediately
        editor.apply();

        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();





    }
    public void displayData (View view) {

        //This creates an object for SharedPreferences.
        android.content.SharedPreferences sharedprefs = getSharedPreferences("myprefs", MODE_PRIVATE);

        //This is used to get data from the file.
        String Ingredient1 = sharedprefs.getString(check1.getText().toString(), null);
        String Ingredient2 = sharedprefs.getString(check2.getText().toString(), null);
        String Ingredient3 = sharedprefs.getString(check3.getText().toString(), null);
        String Ingredient4 = sharedprefs.getString(check4.getText().toString(), null);
        String Ingredient5 = sharedprefs.getString(check5.getText().toString(), null);

        //This displays data using AlertDialogueBuilder
        showMessage("Data", check1.getText().toString() + " - " + Ingredient1 + "\n" + check2.getText().toString() + " - " + Ingredient2 + "\n" + check3.getText().toString()
                + " - " + Ingredient3 + "\n" + check4.getText().toString() + " - " + Ingredient4 + "\n" + check5.getText().toString() + " - " + Ingredient5);


    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
