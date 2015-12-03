package sfsu.csc413.foodcraft;

import java.util.List;

/**
 * Created by pklein on 11/12/15.
 */
public interface TaskCallbackInterface{
    void  onTaskCompleted(String result, boolean cached);
    void onTaskCompleted(List<UPCObject> result);
}
