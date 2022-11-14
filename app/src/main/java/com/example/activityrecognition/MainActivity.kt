package com.example.activityrecognition

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.DetectedActivity

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    internal lateinit var broadcastReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        startTracking()
//        stopTracking()

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == MainActivity.BROADCAST_DETECTED_ACTIVITY) {
                    val type = intent.getIntExtra("type", -1)
                    val confidence = intent.getIntExtra("confidence", 0)
                    handleUserActivity(type, confidence)
                }
            }
        }
        startTracking()
    }
    private fun handleUserActivity(type: Int, confidence: Int) {
        var label = getString(R.string.activity_unknown)
        when (type) {
            DetectedActivity.IN_VEHICLE -> {
                label = "You are in Vehicle"
            }
            DetectedActivity.ON_BICYCLE -> {
                label = "You are on Bicycle"
            }
            DetectedActivity.ON_FOOT -> {
                label = "You are on Foot"
            }
            DetectedActivity.RUNNING -> {
                label = "You are Running"
            }
            DetectedActivity.STILL -> {
                label = "You are Still"
            }
            DetectedActivity.TILTING -> {
                label = "Your phone is Tilted"
            }
            DetectedActivity.WALKING -> {
                label = "You are Walking"
            }
            DetectedActivity.UNKNOWN -> {
                label = "Unkown Activity"
            }
        }
        Log.e(TAG, "User activity: $label, Confidence: $confidence")
        if (confidence > MainActivity.CONFIDENCE) {
            Log.d("MOTION ACTIVITY","User activity: $label, Confidence: $confidence")
        }
    }
    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
            IntentFilter(BROADCAST_DETECTED_ACTIVITY)
        )
    }
    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }
    private fun startTracking() {
        val intent = Intent(this@MainActivity, BackgroundDetectedActivitiesService::class.java)
        startService(intent)
    }
    private fun stopTracking() {
        val intent = Intent(this@MainActivity, BackgroundDetectedActivitiesService::class.java)
        stopService(intent)
    }
    companion object {
        val BROADCAST_DETECTED_ACTIVITY = "activity_intent"
        internal val DETECTION_INTERVAL_IN_MILLISECONDS: Long = 1000
        val CONFIDENCE = 70
    }
}