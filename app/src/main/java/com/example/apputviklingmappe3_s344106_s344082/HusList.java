package com.example.apputviklingmappe3_s344106_s344082;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HusList extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView btnAdd;
    private ImageView btnBack;
    private ImageView btnList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hus_list);

        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(R.string.titleList);
        listView = (ListView) findViewById(R.id.list_view_hus);
        btnAdd = (ImageView) findViewById(R.id.add);
        btnBack = (ImageView) findViewById(R.id.back);
        btnList = (ImageView) findViewById(R.id.list);
        btnList.setVisibility(View.INVISIBLE);

        DBHandler db = new DBHandler(this);
        List<Hus> husList = db.findAllHus();

        HusListAdapter adapter = new HusListAdapter(this, R.layout.list_item_hus, husList);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);
        button();
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int newid = (int) id;
                AlertDialog created = buildAlertDialog(view, newid);
                created.show();
                return true;
            }
        });
    }

    private AlertDialog buildAlertDialog(View view, int id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setView(R.layout.alert_popup);

        LayoutInflater alertInflater = LayoutInflater.from(view.getContext());
        View alertConvertView = alertInflater.inflate(R.layout.alert_popup, null);


        alertDialog.setView(alertConvertView);

        return alertDialog.create();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(HusList.this, HusList.class));
        finish();
    }


}
