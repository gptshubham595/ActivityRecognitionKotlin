package com.example.activityrecognition

import android.app.IntentService
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.tasks.OnSuccessListener

class DetectedActivitiesIntentService : IntentService(TAG) {
    override fun onCreate() {
        super.onCreate()
        Log.d("MOTION","Detected Activity Created")
    }

    override fun onHandleIntent(intent: Intent?) {
        val result = ActivityRecognitionResult.extractResult(intent)
        var mostActivityResult = result.mostProbableActivity
        var activityResult:Int = mostActivityResult.type

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        val detectedActivities = result.probableActivities as ArrayList<*>
        for (activity in detectedActivities) {
            broadcastActivity(activity as DetectedActivity)
        }
    }

    private fun broadcastActivity(activity: DetectedActivity) {
        val intent = Intent(MainActivity.BROADCAST_DETECTED_ACTIVITY)
        intent.putExtra("type", activity.type)
        intent.putExtra("confidence", activity.confidence)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        protected val TAG = DetectedActivitiesIntentService::class.java.simpleName
    }
}// Use the TAG to name the worker thread.