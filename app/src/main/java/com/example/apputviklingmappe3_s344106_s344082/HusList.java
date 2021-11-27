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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class HusList extends AppCompatActivity {
    private ListView listView;
    private ImageView btnBack;
    private ImageView btnAdd;
    private DBHandler db;
    private List<Hus> husList;
    private int husID;
    private String husBeskrivelse;
    private String husAdresse;
    private LatLng husKoordinater;
    private int husEtasjer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hus_list);

        listView = (ListView) findViewById(R.id.list_view_hus);
        btnAdd = (ImageView) findViewById(R.id.add);
        btnBack = (ImageView) findViewById(R.id.back);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(R.string.titleList);
        ImageView btnList = (ImageView) findViewById(R.id.list);
        btnList.setVisibility(View.INVISIBLE);

        db = new DBHandler(this);
        husList = db.findAllHus();

        HusListAdapter adapter = new HusListAdapter(this, R.layout.list_item_hus, husList);
        listView.setAdapter(adapter);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int newid = (int) id;
                Hus ethus = husList.get(newid);
                husID = ethus.get_ID();
                husBeskrivelse = ethus.getBeskrivelse();
                husAdresse = ethus.getGateadresse();
                husKoordinater = new LatLng(Double.parseDouble(ethus.getGps_lat()), Double.parseDouble(ethus.getGps_long()));
                husEtasjer = ethus.getEtasjer();
                AlertDialog created = buildAlertDialog(view, newid);
                created.show();
            }
        });
    }


    private AlertDialog buildAlertDialog(View view, int id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setView(R.layout.alert_popup);

        LayoutInflater alertInflater = LayoutInflater.from(view.getContext());
        View alertConvertView = alertInflater.inflate(R.layout.alert_popup, null);


        alertDialog.setView(alertConvertView);
        Button buttonEdit = (Button) alertConvertView.findViewById(R.id.buttonEdit);
        Button buttonSlett = (Button) alertConvertView.findViewById(R.id.buttonSlett);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HusList.this, EditHus.class);
                intent.putExtra("id", husID);
                intent.putExtra("beskrivelse", husBeskrivelse);
                intent.putExtra("gateadresse", husAdresse);
                intent.putExtra("etasjer", husEtasjer);
                intent.putExtra("lat,long", husKoordinater);
                startActivity(intent);
            }
        });

        buttonSlett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DBHandler(HusList.this);
                db.deleteHus(husID);
                onRestart();
            }
        });

        return alertDialog.create();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(HusList.this, HusList.class));
        finish();
    }


}
