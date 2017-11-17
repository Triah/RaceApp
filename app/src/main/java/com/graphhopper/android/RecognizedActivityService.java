package com.graphhopper.android;
/*
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class RecognizedActivityService extends IntentService {
    public RecognizedActivityService() {
        super("RecognizedActivityService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for (int i = 0; i < probableActivities.size(); i++) {
            DetectedActivity detectedActivity = probableActivities.get(i);
            switch (detectedActivity.getType()) {
                case DetectedActivity.STILL: {
                    Log.i("Activity Recognition: ", "STILL, " + detectedActivity.getConfidence());
                    if (detectedActivity.getConfidence() >= 75)
                        sendDetectedActivity("STILL");
                    break;
                }

                case DetectedActivity.ON_FOOT: {
                    Log.i("Activity Recognition: ", "ON_FOOT, " + detectedActivity.getConfidence());
                    if (detectedActivity.getConfidence() >= 75)
                        sendDetectedActivity("ON_FOOT");
                    break;
                }

                case DetectedActivity.ON_BICYCLE: {
                    Log.i("Activity Recognition: ", "ON_BICYCLE, " + detectedActivity.getConfidence());
                    if (detectedActivity.getConfidence() >= 75)
                        sendDetectedActivity("ON_BICYCLE");
                    break;
                }

                case DetectedActivity.RUNNING: {
                    Log.i("Activity Recognition: ", "RUNNING, " + detectedActivity.getConfidence());
                    if (detectedActivity.getConfidence() >= 75)
                        sendDetectedActivity("RUNNING");
                    break;
                }

                case DetectedActivity.WALKING: {
                    Log.i("Activity Recognition: ", "WALKING, " + detectedActivity.getConfidence());
                    if (detectedActivity.getConfidence() >= 75)
                        sendDetectedActivity("WALKING");
                    break;
                }

                case DetectedActivity.TILTING: {
                    Log.i("Activity Recognition: ", "TILTING, " + detectedActivity.getConfidence());
                    if (detectedActivity.getConfidence() >= 75)
                        sendDetectedActivity("TILTING");
                    break;
                }
            }
        }
    }

    public void sendDetectedActivity(String text) {
        Intent intent = new Intent(MainActivity.BROADCAST_RECOGNIZED_ACTIVITY_ID);
        intent.putExtra(MainActivity.BROADCAST_RECOGNIZED_ACTIVITY_TEXT, text);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.sendBroadcast(intent);
    }
}
*/