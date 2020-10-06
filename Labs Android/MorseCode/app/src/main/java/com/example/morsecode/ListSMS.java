package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.morsecode.SMS.DecodeDialog;
import com.example.morsecode.SMS.SMS;
import com.example.morsecode.SMS.SmsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListSMS extends AppCompatActivity {
    private ArrayAdapter<SMS> adapter;
    private List<SMS> smsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sms);
        ListView listView = findViewById(R.id.listSMS);
        smsList = getAllSms();
        adapter = new SmsAdapter(this, smsList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {

            DecodeDialog dialog = new DecodeDialog(smsList.get(position));
            dialog.show(getSupportFragmentManager(), "Decode Morse Code");
        });
    }




    public List<SMS> getAllSms() {
        List<SMS> lstSms = new ArrayList<>();
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI,
                new String[]{Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.BODY},
                null, null, Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                String message = c.getString(1);
                if (message.matches("^[ *-]+$"))
                    lstSms.add(new SMS(c.getString(0), message));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        return lstSms;
    }

}
