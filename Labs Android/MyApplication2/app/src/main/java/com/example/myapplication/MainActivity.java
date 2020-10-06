package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickTest(View view) {
        EditText t1 = (EditText) findViewById(R.id.editText);
        TextView t2 = (TextView) findViewById(R.id.textView);
        t2.setText("HELLO0000000000");
    }
}
