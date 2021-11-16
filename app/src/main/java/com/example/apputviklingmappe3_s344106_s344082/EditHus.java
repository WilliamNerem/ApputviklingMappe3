package com.example.apputviklingmappe3_s344106_s344082;

import android.app.AlertDialog;
import android.content.ContentValues;
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

public class EditHus extends AppCompatActivity {
    private DBHandler db;
    private int id;
    private TextView tvTitle;
    private ImageView btnList;
    private ImageView btnBack;
    private ImageView btnAdd;
    private Button btn;
    private Button btnAvbryt;
    private Button btnEditAddresse;
    private EditText editBeskrivelse;
    private TextView tvGateadresse;
    private Spinner spinnerEtasjer;
    public List<Address> adresses;
    public static String sendtBeskrivelseEdit = "";
    public static int sendtEtasjerEdit = 0;
    Geocoder geocoder;
    LatLng cords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_edit_hus);
        db = new DBHandler(this);

        id = getIntent().getExtras().getInt("id");
        String oldBeskrivelse = getIntent().getExtras().getString("beskrivelse");
        String oldGateadresse = getIntent().getExtras().getString("gateadresse");
        int oldEtasjer = getIntent().getExtras().getInt("etasjer");

        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(R.string.titleEditHus);
        btnList = (ImageView) findViewById(R.id.list);
        btnList.setVisibility(View.INVISIBLE);
        btnBack = (ImageView) findViewById(R.id.back);
        btnAdd = (ImageView) findViewById(R.id.add);
        btnAdd.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.btnEndre);
        btnAvbryt = (Button) findViewById(R.id.btnAvbryt);
        btnEditAddresse = (Button) findViewById(R.id.editAddresse);
        editBeskrivelse = findViewById(R.id.beskrivelse);
        editBeskrivelse.setText(oldBeskrivelse);
        tvGateadresse = findViewById(R.id.gateadresse);
        spinnerEtasjer = findViewById(R.id.etasjer);
        setSpinner();
        spinnerEtasjer.setSelection(oldEtasjer);

        System.out.println("Bes: " + sendtBeskrivelseEdit);
        System.out.println("Eta: " + sendtEtasjerEdit);

        if(!(sendtBeskrivelseEdit.equals(""))) {
            editBeskrivelse.setText(sendtBeskrivelseEdit);
        }
        if (sendtEtasjerEdit != 0) {
            spinnerEtasjer.setSelection(sendtEtasjerEdit);
        }

        geocoder = new Geocoder(this, Locale.getDefault());
        cords = null;
        tvGateadresse.setText("");
        cords = getIntent().getExtras().getParcelable("lat,long");
        btnEditAddresse.setText(R.string.btnEndreAddresse);
        try {
            adresses = geocoder.getFromLocation(cords.latitude,cords.longitude,1);
            tvGateadresse.setText(adresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        button();
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
                startActivity(new Intent(EditHus.this, Maps.class));
                finishAffinity();
            }
        });

        btnEditAddresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditHus.this, Maps.class);
                Maps.edit = true;
                sendtBeskrivelseEdit = editBeskrivelse.getText().toString();
                if(!(spinnerEtasjer.getSelectedItem().toString().equals("Velg antall Etasjer"))) {
                    sendtEtasjerEdit = Integer.parseInt(spinnerEtasjer.getSelectedItem().toString());
                }
                startActivity(i);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()){
                    //LatLng cords = getIntent().getExtras().getParcelable("lat,long");
                    Hus etHus = new Hus();
                    etHus.set_ID(id);
                    etHus.setBeskrivelse(editBeskrivelse.getText().toString());
                    etHus.setGateadresse(tvGateadresse.getText().toString());
                    etHus.setGps_lat(Double.toString(cords.latitude));
                    etHus.setGps_long(Double.toString(cords.longitude));
                    etHus.setEtasjer(Integer.parseInt(spinnerEtasjer.getSelectedItem().toString()));
                    db.updateHus(etHus);
                    onBackPressed();
                }
            }
        });

        btnAvbryt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean validation(){
        String strBeskrivelse = editBeskrivelse.getText().toString();
        String strGateaddresse = tvGateadresse.getText().toString();
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