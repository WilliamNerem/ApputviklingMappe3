package com.example.apputviklingmappe3_s344106_s344082;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LeggTil extends AppCompatActivity {
    private DBHandler db;
    private Button btn;
    private EditText editBeskrivelse;
    private EditText editGateadresse;
    private EditText editGpsLat;
    private EditText editGpsLong;
    private EditText editEtasjer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legg_til);
        db = new DBHandler(this);
        btn = (Button) findViewById(R.id.btnLeggTil);
        editBeskrivelse = findViewById(R.id.beskrivelse);
        editGateadresse = findViewById(R.id.gateadresse);
        editGpsLat = findViewById(R.id.gpsLat);
        editGpsLong = findViewById(R.id.gpsLong);
        editEtasjer = findViewById(R.id.etasjer);
        button();

    }

    private void button(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()){
                    Hus hus = new Hus();
                    hus.setBeskrivelse(editBeskrivelse.getText().toString());
                    hus.setGateadresse(editGateadresse.getText().toString());
                    hus.setGps_lat(editGpsLat.getText().toString());
                    hus.setGps_long(editGpsLong.getText().toString());
                    hus.setEtasjer(Integer.parseInt(editEtasjer.getText().toString()));
                    db.addHus(hus);
                    startActivity(new Intent(LeggTil.this, HusList.class));
                }
            }
        });
    }

    private boolean validation(){
        String strBeskrivelse = editBeskrivelse.getText().toString();
        String strGateaddresse = editGateadresse.getText().toString();
        String strEtasjer = editEtasjer.getText().toString();

        if (strBeskrivelse.equals("") || strGateaddresse.equals("") || strEtasjer.equals("")){
            Toast.makeText(getBaseContext(),"Alle felt må fylles ut", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}