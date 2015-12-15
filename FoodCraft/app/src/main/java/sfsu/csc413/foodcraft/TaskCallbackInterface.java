package sfsu.csc413.foodcraft;

import java.util.List;

/**
 * This is an interface for implementing different task callbacks. We use these to share data from non-activity classes
 * to activities asynchronously.
 */
public interface TaskCallbackInterface{
    void  onTaskCompleted(String result, boolean cached);
    void onTaskCompleted(List<UPCObject> result);
}
