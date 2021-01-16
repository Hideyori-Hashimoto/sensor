package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.textservice.TextInfo;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;

    private TextView lightVal;
    private TextView minVal;
    private TextView maxVal;
    private TextView avgVal;
    private ProgressBar progress_Bar;
    private int minValue;
    private int maxValue;
    private float average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightVal = findViewById(R.id.lightVal);
        minVal = findViewById(R.id.minVal);
        maxVal = findViewById(R.id.maxVal);
        avgVal = findViewById(R.id.avgVal);
        progress_Bar = findViewById(R.id.progress_bar);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            int sensorValue = (int) sensorEvent.values[0];
            lightVal.setText(String.valueOf(sensorValue) + " lx");
            progress_Bar.setProgress(sensorValue >= 100 ? 100 : sensorValue);

            // Calculate max value
            if (sensorValue > maxValue) {
                maxValue = sensorValue;
                maxVal.setText(String.valueOf(maxValue));
            }

            // Calculate min value
            if (minValue > 0) {
                if (sensorValue <= minValue) {
                    int value = 0;
                    if (sensorValue > 0) {
                        value = sensorValue;
                    }
                    minValue = value;
                    minVal.setText(String.valueOf(minValue));
                }

            } else {
                minValue = sensorValue;
                minVal.setText(String.valueOf(minValue));
            }

            // Calculate average
            average = (minValue + maxValue) / 2;
            avgVal.setText(String.valueOf(average));

        } else {
            Log.d("Error", "Unknown sensor type");
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}