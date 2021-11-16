package com.example.apputviklingmappe3_s344106_s344082;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class HusListAdapter extends ArrayAdapter<Hus> {
    private final Context mContext;
    private final int mResource;
    private DBHandler db;
    private final List<Hus> listHus;

    public HusListAdapter(Context context, int resource, List<Hus> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        listHus = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        View currentView = convertView;

        ImageButton buttonEditHus = (ImageButton) convertView.findViewById(R.id.buttonEditHus);
        ImageButton buttonDeleteHus = (ImageButton) convertView.findViewById(R.id.buttonDeleteHus);

        int id = getItem(position).get_ID();
/*
        id = getItem(position).get_ID();
        String beskrivelse = getItem(position).getBeskrivelse();
        String gateadresse = getItem(position).getGateadresse();
        String gpsLat = getItem(position).getGps_lat();
        String gpsLong = getItem(position).getGps_long();
        int etasjer = getItem(position).getEtasjer();
        Hus hus = new Hus(beskrivelse, gateadresse, gpsLat, gpsLong, etasjer);
*/
        TextView tvId = (TextView) convertView.findViewById(R.id.itemHus);
        TextView tvBeskrivelse = (TextView) convertView.findViewById(R.id.itemHusBeskrivelse);
        TextView tvGateadresse = (TextView) convertView.findViewById(R.id.itemHusGateadresse);
        TextView tvEtasjer = (TextView) convertView.findViewById(R.id.itemHusEtasjer);

        String beskrivelse = getItem(position).getBeskrivelse();
        String gateadresse = getItem(position).getGateadresse();
        String lat = getItem(position).getGps_lat();
        String lng = getItem(position).getGps_long();
        LatLng koordinater = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        String strId = String.valueOf(id);
        tvId.setText(strId);
        tvBeskrivelse.setText(beskrivelse);
        tvGateadresse.setText(gateadresse);
        int etasjer = getItem(position).getEtasjer();
        String strEtasjer = String.valueOf(etasjer);
        tvEtasjer.setText(strEtasjer);


        buttonEditHus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AlertDialog created = buildAlertDialog(currentView, id, tvBeskrivelse, tvGateadresse, tvEtasjer);
                //created.show();
                Intent intent = new Intent(mContext, EditHus.class);
                intent.putExtra("id", id);
                intent.putExtra("beskrivelse", beskrivelse);
                intent.putExtra("gateadresse", gateadresse);
                intent.putExtra("etasjer", etasjer);
                intent.putExtra("lat,long", koordinater);
                mContext.startActivity(intent);
            }
        });

        buttonDeleteHus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DBHandler(mContext);
                db.deleteHus(id);
                listHus.clear();
                listHus.addAll(db.findAllHus());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void setSpinner(View v, Spinner spinner) {
        String[] items = v.getResources().getStringArray(R.array.etasjer);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_spinner_item, items) {
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
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public boolean validation(EditText beskrivelse, EditText gateadresse, Spinner etasjer, Context context){
        String strBeskrivelse = beskrivelse.getText().toString();
        String strGateadresse = gateadresse.getText().toString();
        String strEtasjer = etasjer.getSelectedItem().toString();

        if (strBeskrivelse.equals("") || strGateadresse.equals("") || strEtasjer.equals("Velg antall Etasjer")){
            Toast.makeText(context,"Alle felt må fylles ut", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private AlertDialog buildAlertDialog(View view, int id, TextView tvBeskrivelse, TextView tvGateadresse, TextView tvEtasjer){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setView(R.layout.alert_edit_hus);

        LayoutInflater alertInflater = LayoutInflater.from(view.getContext());
        View alertConvertView = alertInflater.inflate(R.layout.alert_edit_hus, null);

        alertDialog.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alertDialog.setPositiveButton("Lagre", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                EditText editBeskrivelse = alertConvertView.findViewById(R.id.beskrivelse);
                EditText editGateadresse = alertConvertView.findViewById(R.id.gateadresse);
                Spinner spinnerEtasjer = alertConvertView.findViewById(R.id.etasjer);

                if (validation(editBeskrivelse, editGateadresse, spinnerEtasjer, alertConvertView.getContext())){
                    db = new DBHandler(alertConvertView.getContext());
                    //LatLng cords = getIntent().getExtras().getParcelable("lat,long");
                    Hus etHus = new Hus();
                    etHus.set_ID(id);
                    etHus.setBeskrivelse(editBeskrivelse.getText().toString());
                    etHus.setGateadresse(editGateadresse.getText().toString());
                    etHus.setEtasjer(Integer.parseInt(spinnerEtasjer.getSelectedItem().toString()));
                    db.updateHus(etHus);
                    ContentValues resValues = new ContentValues();
                    resValues.clear();
                    resValues.put("id", id);
                    resValues.put("beskrivelse", etHus.beskrivelse);
                    resValues.put("addresse", etHus.gateadresse);
                    resValues.put("lat", etHus.gps_lat);
                    resValues.put("long", etHus.gps_long);
                    resValues.put("etasjer", etHus.etasjer);
                    listHus.clear();
                    listHus.addAll(db.findAllHus());
                    notifyDataSetChanged();
                }
            }
        });

        alertDialog.setView(alertConvertView);

        EditText editBeskrivelse = alertConvertView.findViewById(R.id.beskrivelse);
        EditText editGateaddresse = alertConvertView.findViewById(R.id.gateadresse);
        Spinner spinnerEtasjer = alertConvertView.findViewById(R.id.etasjer);
        editBeskrivelse.setText(tvBeskrivelse.getText());
        editGateaddresse.setText(tvGateadresse.getText());

        setSpinner(alertConvertView, spinnerEtasjer);

        return alertDialog.create();

    }
}
