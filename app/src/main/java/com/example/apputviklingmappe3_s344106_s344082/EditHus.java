package com.example.apputviklingmappe3_s344106_s344082;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditHus extends AppCompatActivity {
    private DBHandler db;
    private static int id;
    private ImageView btnList;
    private ImageView btnBack;
    private Button btn;
    private Button btnAvbryt;
    private Button btnEditAddresse;
    private EditText editBeskrivelse;
    private TextView tvGateadresse;
    private Spinner spinnerEtasjer;
    public List<Address> adresses;
    public static String sendtBeskrivelseEdit = "";
    public static int sendtEtasjerEdit = 0;
    private static String origin;
    private String oldBeskrivelse;
    private int oldEtasjer;
    public static LatLng latLng;
    Geocoder geocoder;
    LatLng cords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hus_form);
        db = new DBHandler(this);

        oldBeskrivelse = getIntent().getExtras().getString("beskrivelse");
        oldEtasjer = getIntent().getExtras().getInt("etasjer");

        if (getIntent().getExtras().getString("origin") != null){
            origin = getIntent().getExtras().getString("origin");
        }
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(R.string.titleEditHus);
        btnList = (ImageView) findViewById(R.id.list);
        btnList.setVisibility(View.INVISIBLE);
        btnBack = (ImageView) findViewById(R.id.back);
        ImageView btnAdd = (ImageView) findViewById(R.id.add);
        btnAdd.setVisibility(View.INVISIBLE);

        btn = (Button) findViewById(R.id.btnContinue);
        btn.setText(R.string.btnEndre);
        btnAvbryt = (Button) findViewById(R.id.btnAvbryt);
        btnEditAddresse = (Button) findViewById(R.id.editAddresse);

        editBeskrivelse = findViewById(R.id.beskrivelse);
        editBeskrivelse.setText(oldBeskrivelse);
        tvGateadresse = findViewById(R.id.gateadresse);
        spinnerEtasjer = findViewById(R.id.etasjer);
        setSpinner();
        spinnerEtasjer.setSelection(oldEtasjer);

        if(!(sendtBeskrivelseEdit.equals("")) || !(sendtEtasjerEdit == 0)) {
            editBeskrivelse.setText(sendtBeskrivelseEdit);
            spinnerEtasjer.setSelection(sendtEtasjerEdit);
        } else {
            id = getIntent().getExtras().getInt("id");
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
                Maps.editEdit = false;
                startActivity(new Intent(EditHus.this, Maps.class));
                finishAffinity();
            }
        });

        btnEditAddresse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditHus.this, Maps.class);
                Maps.editEdit = true;
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
                if (Form.validation(getBaseContext(), editBeskrivelse, tvGateadresse, spinnerEtasjer)){
                    db.updateHus(Form.sethus(id, editBeskrivelse, tvGateadresse, cords, spinnerEtasjer, "edit"));
                    sendtEtasjerEdit = 0;
                    sendtBeskrivelseEdit = "";
                    Maps.editEdit = false;
                    onBackPressed();
                }
            }
        });

        btnAvbryt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Maps.editEdit = false;
                sendtBeskrivelseEdit = "";
                sendtEtasjerEdit = 0;
                onBackPressed();
            }
        });
    }

    private void setSpinner() {
        String[] items = getResources().getStringArray(R.array.etasjer);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, items) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEtasjer.setAdapter(adapter);

        spinnerEtasjer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                if (position == 0 && tv != null) {
                    tv.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (!origin.equals("maps")) {
            startActivity(new Intent(EditHus.this, HusList.class));
        }
        finish();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        if (Maps.editEdit){
            Maps.editEdit = false;
            Intent intent = new Intent(EditHus.this, EditHus.class);
            if (latLng == null){
                intent.putExtra("lat,long", cords);
            } else {
                intent.putExtra("lat,long", latLng);
            }
            startActivity(intent);
            finish();
        }
    }



}