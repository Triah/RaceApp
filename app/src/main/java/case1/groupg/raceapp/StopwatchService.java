package case1.groupg.raceapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Nicolai on 02-12-2017.
 */

public class StopwatchService extends IntentService {

    public StopwatchService(String name) {
        super(name);
    }

    public StopwatchService(){
        super("StopwatchService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
