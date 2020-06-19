package com.example.wanderfoapp.Shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wanderfoapp.R;

import java.util.ArrayList;

public class SpinnerTypeAdapter extends ArrayAdapter<SpinnerTypeElement> {
    public SpinnerTypeAdapter(Context context, ArrayList<SpinnerTypeElement> typeList){
        super(context, 0,typeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        // check if it was previously inflated
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_type, parent, false
            );
        }
        TextView cardTypeTextView =convertView.findViewById(R.id.cardType);

        SpinnerTypeElement spinnerElem = getItem(position);
        if(spinnerElem!=null) {
            cardTypeTextView.setText(spinnerElem.getType());
        }
        return convertView;
    }


}
