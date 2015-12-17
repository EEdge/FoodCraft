package sfsu.csc413.foodcraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Empty implementations of the TaskCallback interface. These will be overridden in the activity that's needed.
 */
public abstract class TaskCallback implements TaskCallbackInterface{
    @Override
    public void onTaskCompleted(String result, boolean cached){
    }
    public void onTaskCompleted(List<UPCObject> result){
    }
    abstract void onTaskCompleted(String text);
    abstract void onTaskCompleted(ArrayList<Place> result);
}
