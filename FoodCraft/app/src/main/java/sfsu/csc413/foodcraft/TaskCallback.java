package sfsu.csc413.foodcraft;

import java.util.List;

/**
 * Created by pklein on 11/12/15.
 */
public class TaskCallback implements TaskCallbackInterface{
    @Override
    public void onTaskCompleted(String result, boolean cached){
    }
    public void onTaskCompleted(List<UPCObject> result){
    }
}