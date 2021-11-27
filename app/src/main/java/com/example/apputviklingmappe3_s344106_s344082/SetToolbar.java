package com.example.apputviklingmappe3_s344106_s344082;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SetToolbar {
    public static void items(View v, boolean list, boolean add, boolean back, String title){
        TextView tvTitle = (TextView) v.findViewById(R.id.title);
        tvTitle.setText(R.string.titleLeggTil);
        ImageView btnList = (ImageView) v.findViewById(R.id.list);
        if(!list){
            btnList.setVisibility(View.INVISIBLE);
        }
        ImageView btnBack = (ImageView) v.findViewById(R.id.back);
        if(!back){
            btnBack.setVisibility(View.INVISIBLE);
        }
        ImageView btnAdd = (ImageView) v.findViewById(R.id.add);
        if(!add){
            btnAdd.setVisibility(View.INVISIBLE);
        }
    }
}
