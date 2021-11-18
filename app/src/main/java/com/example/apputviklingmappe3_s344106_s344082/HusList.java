package com.example.apputviklingmappe3_s344106_s344082;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HusList extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView btnAdd;
    private ImageView btnBack;
    private ImageView btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hus_list);

        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(R.string.titleList);
        ListView listView = (ListView) findViewById(R.id.list_view_hus);
        btnAdd = (ImageView) findViewById(R.id.add);
        btnBack = (ImageView) findViewById(R.id.back);
        btnList = (ImageView) findViewById(R.id.list);
        btnList.setVisibility(View.INVISIBLE);
        button();

        DBHandler db = new DBHandler(this);
        List<Hus> husList = db.findAllHus();

        HusListAdapter adapter = new HusListAdapter(this, R.layout.list_item_hus, husList);
        listView.setAdapter(adapter);
    }

    private void button(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HusList.this, LeggTil.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HusList.this, Maps.class));
                finishAffinity();
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(HusList.this, HusList.class));
        finish();
    }

}
