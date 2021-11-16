package com.example.apputviklingmappe3_s344106_s344082;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LeggTil extends AppCompatActivity {
    private DBHandler db;
    private TextView tvTitle;
    private ImageView btnList;
    private ImageView btnBack;
    private ImageView btnAdd;
    private Button btn;
    private Button btnEditAddresse;
    private EditText editBeskrivelse;
    private TextView editGateadresse;
    private Spinner spinnerEtasjer;
    public List<Address> adresses;
    Geocoder geocoder;
    LatLng cords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legg_til);
        db = new DBHandler(this);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(R.string.titleLeggTil);
        btnList = (ImageView) findViewById(R.id.list);
        btnBack = (ImageView) findViewById(R.id.back);
        btnAdd = (ImageView) findViewById(R.id.add);
        btnAdd.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.btnLeggTil);
        btnEditAddresse = (Button) findViewById(R.id.editAddresse);
        editBeskrivelse = findViewById(R.id.beskrivelse);
        editGateadresse = findViewById(R.id.gateadresse);
        spinnerEtasjer = findViewById(R.id.etasjer);
        geocoder = new Geocoder(this, Locale.getDefault());

        if (getIntent().getExtras() != null){
            cords = getIntent().getExtras().getParcelable("lat,long");
            btnEditAddresse.setText(R.string.btnLeggTil);
            try {
                adresses = geocoder.getFromLocation(cords.latitude,cords.longitude,1);
                editGateadresse.setText(adresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            btnEditAddresse.setText(R.string.btnLeggTil);
        }


        button();
        setSpinner();
    }

    private void button(){
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LeggTil.this, Maps.class));
                finishAffinity();
            }
        });

        btnEditAddresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()){
                    Hus hus = new Hus();
                    hus.setBeskrivelse(editBeskrivelse.getText().toString());
                    hus.setGateadresse(editGateadresse.getText().toString());
                    hus.setGps_lat(Double.toString(cords.latitude));
                    hus.setGps_long(Double.toString(cords.longitude));
                    hus.setEtasjer(Integer.parseInt(spinnerEtasjer.getSelectedItem().toString()));
                    db.addHus(hus);
                    startActivity(new Intent(LeggTil.this, HusList.class));
                }
            }
        });
    }

    private boolean validation(){
        String strBeskrivelse = editBeskrivelse.getText().toString();
        String strGateaddresse = editGateadresse.getText().toString();
        String strEtasjer = spinnerEtasjer.getSelectedItem().toString();

        if (strBeskrivelse.equals("") || strGateaddresse.equals("") || strEtasjer.equals("Velg antall Etasjer")){
            Toast.makeText(getBaseContext(),"Alle felt må fylles ut", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setSpinner() {
        String[] items = getResources().getStringArray(R.array.etasjer);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEtasjer.setAdapter(adapter);

        spinnerEtasjer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}