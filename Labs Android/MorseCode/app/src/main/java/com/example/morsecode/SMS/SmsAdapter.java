package com.example.morsecode.SMS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.morsecode.R;

import java.util.List;

public class SmsAdapter extends ArrayAdapter<SMS> {
    public SmsAdapter(@NonNull Context context, List<SMS> smsList) {
        super(context, 0, smsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SMS user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView person = convertView.findViewById(R.id.person);
        TextView messagge = convertView.findViewById(R.id.message);
        person.setText(user.getPerson());
        messagge.setText(user.getMessage());
        return convertView;
    }
}
