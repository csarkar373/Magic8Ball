package com.westhillcs.magic8ball;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

/**
 * Created by Chandan on 7/9/2016.
 */
public class Magic8Ball implements SensorEventListener {

    private SensorManager sm;
    private Sensor accelerometer;

    private long curTime, lastUpdate;
    private float x, y, z, last_x, last_y, last_z;
    private final static long UPDATEPERIOD = 300; // measured in milliseconds
    private final static int SHAKE_THRESHOLD = 800;

    private TextToSpeech tts;

    Context thisContext;

    private final String [] predictions = { "Yes, definitely",
                                            "No way",
                                            "Let me think about that",
                                            "Maybe"};


    public Magic8Ball(Context thisContext) {

        this.thisContext = thisContext;
        sm = (SensorManager)thisContext.getSystemService(thisContext.SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        curTime = lastUpdate = (long)0.0;
        x = y = z = last_x = last_y = last_z = (float)0.0;

        tts = new TextToSpeech(thisContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.v("Inside onInit", "");
                if (status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                } else {
                    Log.v("Speech setup failed", "");
                }
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > UPDATEPERIOD) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                //Toast.makeText(thisContext, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                this.makePrediction();
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }

    public void makePrediction() {
        Random rg = new Random();
        int index = rg.nextInt( predictions.length);
        String message = predictions[index];
        Log.v("MESSAGE:", message);
        Toast.makeText(thisContext, message, Toast.LENGTH_LONG).show();

        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "");
    }
}
