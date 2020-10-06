package com.example.laborator2.Listiner;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.laborator2.ItemAdapter;
import com.example.laborator2.Product;
import com.example.laborator2.R;

public class Light implements SensorEventListener {

    ListView listView;

    public Light(ListView listView) {
        this.listView = listView;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            ItemAdapter a = (ItemAdapter) listView.getAdapter();
            Product p = a.getItem(0);
            float lux = event.values[0];
            // Do something with this sensor value.
            //tv1 = (TextView) findViewById(R.id.text1);
            p.setPrice(String.valueOf(event.values[0]));
            listView.setAdapter(a);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
