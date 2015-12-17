package sfsu.csc413.foodcraft;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// Custom Adapter for Spinner
public class SpinnerAdapter extends ArrayAdapter<String> {

    // The context variable necessary to obtain the application information.
    private Context mContext;

    // An array list containing strings named data to store the objects that will be put into the spinner.
    private ArrayList<String> data;

    // A resources object responsible for keeping track of all of the non-code assets within the application.
    public Resources res;

    // Initializes a variable inflater which is responsible for inflating a layout xml file into a corresponding view object.
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, ArrayList<String> objects) {
        super(context, R.layout.activity_spinner, objects);

        // Setting our context variable to context of the application.
        mContext = context;

        // data will contain an array list of objects being passed into the spinner adapter.
        data = objects;

        // The inflater is being set to the layout within the context.
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * Gets a view that displays in the dropdown pop-up in a specified position.
     *
     * @param position    The specified position if each button to be shown in the drop down.
     * @param convertView The view of the items getting collapsed in the drop down menu.
     * @param parent      The parent view group holding each of the other views.
     * @return Custom view that becomes displayed.
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * Returns the main view which will be displayed in the drop down menu.
     *
     * @param position    The specified position if each button to be shown in the drop down.
     * @param convertView The view of the items getting collapsed in the drop down menu.
     * @param parent      The parent view group holding each of the other views.
     * @return Custom view that becomes displayed.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * The method that is called for each new row which is called a number of times equal to the size of the data array.
     *
     * @param position    The specified position if each button to be shown in the drop down.
     * @param convertView The view of the items getting collapsed in the drop down menu.
     * @param parent      The parent view group holding each of the other views.
     * @return Returns the view for the row items displayed as part of the spinner.
     */
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        // Inflating the view using the layout for the spinner activity.
        View row = inflater.inflate(R.layout.activity_spinner, parent, false);

        // The textview is initialized and set to the items in the data array.
        TextView courseCategory = (TextView) row.findViewById(R.id.courseCategory);
        courseCategory.setText(data.get(position).toString());

        return row;
    }
}
