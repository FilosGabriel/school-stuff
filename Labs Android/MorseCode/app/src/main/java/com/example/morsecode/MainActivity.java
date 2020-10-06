package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.morsecode.MorseCode.MorseCode;
import com.example.morsecode.MorseCode.MorseFlashTask;
import com.example.morsecode.SMS.DialogSMS;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST = 100;
    private MorseFlashTask task;
    private TextView preview;
    private Button stopBackService;
    private Button startBackService;
    private EditText text;
    private CameraManager cameraManager;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS};
    private Integer duration;
    private boolean showPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        duration = Integer.valueOf(pref.getString("morse_unit", "300"));
        showPreview = pref.getBoolean("preview", false);
        setTitle("Morse code");

        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST);
        }
        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST);

        stopBackService = findViewById(R.id.stop_background_service_button);
        startBackService = findViewById(R.id.start_background_service_button);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        preview = findViewById(R.id.preview);
        text = findViewById(R.id.editText);

        stopBackService.setVisibility(View.GONE);
        stopBackService.setOnClickListener(view -> {
            stopBackService.setVisibility(view.GONE);
            task.cancel(true);
        });

        startBackService.setOnClickListener(view -> {
            TransitionManager.beginDelayedTransition(findViewById(R.id.transitions_container));
            stopBackService.setVisibility(View.VISIBLE);
            if (task != null)
                task.cancel(true);
            task = new MorseFlashTask(cameraManager, preview, stopBackService, duration);
            task.execute(text.getText().toString());
        });
        SharedPreferences.OnSharedPreferenceChangeListener spChanged = (sharedPreferences, key) -> {
            if (key.equals("morse_unit"))
                duration = Integer.valueOf(pref.getString("morse_unit", "300"));
            else if (key.equals("preview"))
                showPreview = pref.getBoolean("preview", false);

        };
        pref.registerOnSharedPreferenceChangeListener(spChanged);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (task != null) {
            stopBackService.setVisibility(View.GONE);
            task.cancel(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
            case R.id.sendSMS: {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            }
            case R.id.decodeSMS: {
                startActivity(new Intent(this, ListSMS.class));
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    private final int REQUEST_CODE = 99;

    //code
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num;
                        String name;
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            numbers.moveToFirst();
                            num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            name = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME));
                            Log.d("Morse COde", "onActivityResult: " + name);
                            DialogSMS dialogSMS = new DialogSMS(getApplicationContext(), text.getText().toString(), num, name);
                            dialogSMS.show(getSupportFragmentManager(), "Morse Code");

                        }
                    }
                    break;
                }
        }
    }


}
