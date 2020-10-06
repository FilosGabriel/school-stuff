package com.example.laborator2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Product> {
    public ItemAdapter(@NonNull Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView size = (TextView) convertView.findViewById(R.id.size);

        name.setText(product.getName()+"   ");
        price.setText(product.getPrice().toString()+"   ");
        size.setText(product.getSize().toString()+"   ");

        return convertView;

    }

    @Override
    public void insert(@Nullable Product object, int index) {
        super.insert(object, index);
    }
}
