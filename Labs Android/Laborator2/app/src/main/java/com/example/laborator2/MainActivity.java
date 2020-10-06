package com.example.laborator2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private List<Product> products;
    private String messsage = "Not selected";
    private int value;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        boolean t = pref.getBoolean("dark_mode", false);
        value = Integer.valueOf(pref.getString("list_size", "5"));
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.list);
        products = new ArrayList<>(Util.getProducts().subList(0, value));
        ArrayAdapter<Product> adapter = new ItemAdapter(this, products);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                messsage = products.get(position).getName();
                Toast.makeText(getApplicationContext(),
                        "Name " + products.get(position).getName(), Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, SecondAct.class);
        TextView editText = (TextView) findViewById(R.id.textView);
        intent.putExtra("product", this.messsage);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getTitle().toString().equals("Dialog")) {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure to Exit")
                    .setMessage("Exiting will call finish() method")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "Nothing Happened", Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();
        } else {
            switch (item.getItemId()) {
                case R.id.title: {
                    Intent intent = new Intent(this, SecondAct.class);
                    intent.putExtra("product", messsage);
                    startActivity(intent);
                    break;
                }
                case R.id.titl2: {
                    Intent intent = new Intent(this, Settings.class);
                    startActivity(intent);
                    break;
                }
                case R.id.save_data: {
                    String filename = "myfile";
                    Log.d("Save", "1: ");
                    try (FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE)) {
                        Log.d("Save", "2: ");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(products);
                        Log.d("Save", "3");
                        Log.d("Save", String.valueOf(products.size()));
                        Log.d("Save", "4");
                        fos.write(byteArrayOutputStream.toByteArray());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case R.id.sensors_list: {
                    Intent intent = new Intent(this, Lab6.class);
                    startActivity(intent);
                    break;
                }
                case R.id.camera: {
                    Intent intent = new Intent(this, Laborator7.class);
                    startActivity(intent);
                    break;
                }
                default:
                    break;

            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", "onStop invoked");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("message", "This is my message to be reloaded");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("restore", savedInstanceState.getString("message"));
    }


    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, androidx.preference.Preference pref) {

        return false;
    }
}
