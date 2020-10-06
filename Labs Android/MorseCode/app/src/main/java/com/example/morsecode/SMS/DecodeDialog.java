package com.example.morsecode.SMS;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.morsecode.MorseCode.MorseCode;

public class DecodeDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MorseCode morseCode = new MorseCode();
        String message = morseCode.decode(sms.getMessageInternaly());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Decoded message from " + sms.getPerson() + " is \n\"" + message + "\"");
        return builder.create();
    }

    public DecodeDialog(SMS sms) {
        this.sms = sms;
    }

    private SMS sms;
}
