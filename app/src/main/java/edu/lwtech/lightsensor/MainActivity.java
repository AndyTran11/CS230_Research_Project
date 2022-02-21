package edu.lwtech.lightsensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

// Implementing SensorEventListener
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;

    private ConstraintLayout mLayout;

    private ImageView mImage;

    // Light and proximity sensors
    private Sensor mSensorProximity;
    private Sensor mSensorLight;

    // Textviews to display sensor values
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLayout = (ConstraintLayout) findViewById(R.id.layout);
        mImage = (ImageView) findViewById(R.id.sizing_image);

        // Getting reference to the Textviews
        mTextSensorLight = (TextView) findViewById(R.id.label_light);
        mTextSensorProximity = (TextView) findViewById(R.id.label_proximity);

        // Getting instances of the sensors
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Getting error message
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        // If sensors are not available, show error string
        if(mSensorLight == null){
            mTextSensorLight.setText(sensor_error);
        }

        if(mSensorProximity == null){
            mTextSensorProximity.setText(sensor_error);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Getting sensor type
        int sensorType = sensorEvent.sensor.getType();

        //Getting sensor value
        float currentValue = sensorEvent.values[0];

        //Handling sensor events
        switch(sensorType){
            //Event from light Sensor
            case Sensor.TYPE_LIGHT:
                mTextSensorLight.setText(getResources().getString(R.string.label_light, currentValue));
                if(currentValue <= 10000){
                    mLayout.setBackgroundResource(R.color.green);
                }
                else if(currentValue > 10000 && currentValue <= 20000){
                    mLayout.setBackgroundResource(R.color.yellow);
                }
                else if(currentValue > 20000 && currentValue <= 30000){
                    mLayout.setBackgroundResource(R.color.orange);
                }
                else{
                    mLayout.setBackgroundResource(R.color.red);
                }
                break;

            //Event from proximity Sensor
            case Sensor.TYPE_PROXIMITY:
                mTextSensorProximity.setText(getResources().getString(R.string.label_proximity, currentValue));
                mImage.setScaleX(currentValue / 2);
                mImage.setScaleY(currentValue / 2);
                break;
            default:
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Registering sensor listeners via onStart()
    @Override
    protected void onStart(){
        super.onStart();

        if(mSensorProximity != null){
            mSensorManager.registerListener(this, mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(mSensorLight != null){
            mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Unregistering all sensors when app pauses via onStop()
    @Override
    protected void onStop(){
        super.onStop();

        mSensorManager.unregisterListener(this);
    }
}