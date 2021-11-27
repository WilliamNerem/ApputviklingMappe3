package com.example.apputviklingmappe3_s344106_s344082;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Form extends AppCompatActivity {

    public static boolean validation(Context context, EditText editBeskrivelse, TextView editGateadresse, Spinner spinnerEtasjer){
        String strBeskrivelse = editBeskrivelse.getText().toString();
        String strGateaddresse = editGateadresse.getText().toString();
        String strEtasjer = spinnerEtasjer.getSelectedItem().toString();

        if (strBeskrivelse.equals("") || strGateaddresse.equals("") || strEtasjer.equals("Velg antall Etasjer")){
            Toast.makeText(context,"Alle felt må fylles ut", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static Hus sethus(int id, EditText editBeskrivelse, TextView editGateadresse, LatLng cords, Spinner spinnerEtasjer, String addOrEdit){
        Hus hus = new Hus();
        if (addOrEdit.equals("edit")){
            hus.set_ID(id);
        }
        hus.setBeskrivelse(editBeskrivelse.getText().toString());
        hus.setGateadresse(editGateadresse.getText().toString());
        hus.setGps_lat(Double.toString(cords.latitude));
        hus.setGps_long(Double.toString(cords.longitude));
        hus.setEtasjer(Integer.parseInt(spinnerEtasjer.getSelectedItem().toString()));
        return hus;
    }

}
