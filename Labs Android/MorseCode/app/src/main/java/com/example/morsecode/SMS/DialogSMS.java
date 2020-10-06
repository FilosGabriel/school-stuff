package com.example.morsecode.SMS;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.morsecode.MainActivity;
import com.example.morsecode.MorseCode.MorseCode;
import com.example.morsecode.R;

public class DialogSMS extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MorseCode morseCode = new MorseCode();
        String message = morseCode.encodeToMorseInternational(text);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to send \"" + text + "\" encoded  as \"" + message + "\" to " + person + "(" + num + ")x`")
                .setPositiveButton("Send message", (dialog, id) -> {
                    Intent intent = new Intent(context, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(num, null, message, pi, null);
//
                    Toast.makeText(context, "Message Sent successfully!",
                            Toast.LENGTH_LONG).show();

                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public DialogSMS(Context context, String text, String num, String person) {
        this.text = text;
        this.context = context;
        this.num = num;
        this.person = person;

    }

    private Context context;
    private String num;
    private String person;
    private String text;
}
