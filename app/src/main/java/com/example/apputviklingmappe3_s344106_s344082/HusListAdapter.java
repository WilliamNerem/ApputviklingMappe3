package com.example.apputviklingmappe3_s344106_s344082;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
        String beskrivelse = getItem(position).getBeskrivelse();
        String gateadresse = getItem(position).getGateadresse();
        String gpsKoordinater = getItem(position).getGpsKoordinater();
        int etasjer = getItem(position).getEtasjer();
        Hus hus = new Hus(beskrivelse, gateadresse, gpsKoordinater, etasjer);

        TextView tvId = (TextView) convertView.findViewById(R.id.itemHus);
        TextView tvBeskrivelse = (TextView) convertView.findViewById(R.id.itemHusBeskrivelse);
        TextView tvGateadresse = (TextView) convertView.findViewById(R.id.itemHusGateadresse);
        TextView tvGpsKoordinater = (TextView) convertView.findViewById(R.id.itemHusGpsKoordinater);
        TextView tvEtasjer = (TextView) convertView.findViewById(R.id.itemHusEtasjer);

        String strId = String.valueOf(id);
        tvId.setText(strId);
        tvBeskrivelse.setText(beskrivelse);
        tvGateadresse.setText(gateadresse);
        tvGpsKoordinater.setText(gpsKoordinater);
        String strEtasjer = String.valueOf(etasjer);
        tvEtasjer.setText(strEtasjer);


        buttonEditHus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog created = buildAlertDialog(currentView, id, tvBeskrivelse, tvGateadresse, tvGpsKoordinater, tvEtasjer);
                created.show();
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

    public boolean validation(EditText beskrivelse, EditText gateadresse, EditText gpsKoordinater, EditText etasjer, Context context){
        String strBeskrivelse = beskrivelse.getText().toString();
        String strGateadresse = gateadresse.getText().toString();
        String strGpsKoordinater = gpsKoordinater.getText().toString();
        String strEtasjer = etasjer.getText().toString();

        if (strBeskrivelse.equals("") || strGateadresse.equals("") || strGpsKoordinater.equals("") || strEtasjer.equals("")){
            Toast.makeText(context,"Alle felt må fylles ut", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private AlertDialog buildAlertDialog(View view, int id, TextView tvBeskrivelse, TextView tvGateadresse, TextView tvGpsKoordinater, TextView tvEtasjer){
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
                EditText editGpsKoordinater = alertConvertView.findViewById(R.id.gpsKoordinater);
                EditText editEtasjer = alertConvertView.findViewById(R.id.etasjer);

                if (validation(editBeskrivelse, editGateadresse, editGpsKoordinater, editEtasjer, alertConvertView.getContext())){
                    db = new DBHandler(alertConvertView.getContext());
                    Hus etHus = db.findHus(id);
                    etHus.setBeskrivelse(editBeskrivelse.getText().toString());
                    etHus.setGateadresse(editGateadresse.getText().toString());
                    etHus.setGpsKoordinater(editGpsKoordinater.getText().toString());
                    etHus.setEtasjer(Integer.parseInt(editEtasjer.getText().toString()));
                    db.updateHus(etHus);
                    ContentValues resValues = new ContentValues();
                    resValues.clear();
                    resValues.put("id", id);
                    resValues.put("beskrivelse", etHus.beskrivelse);
                    resValues.put("address", etHus.gateadresse);
                    resValues.put("phone", etHus.gpsKoordinater);
                    resValues.put("type", etHus.etasjer);
                    listHus.clear();
                    listHus.addAll(db.findAllHus());
                    notifyDataSetChanged();
                }
            }
        });

        alertDialog.setView(alertConvertView);

        EditText editBeskrivelse = alertConvertView.findViewById(R.id.beskrivelse);
        EditText editGateaddresse = alertConvertView.findViewById(R.id.gateadresse);
        EditText editGpsKoordinater = alertConvertView.findViewById(R.id.gpsKoordinater);
        EditText editEtasjer = alertConvertView.findViewById(R.id.etasjer);
        editBeskrivelse.setText(tvBeskrivelse.getText());
        editGateaddresse.setText(tvGateadresse.getText());
        editGpsKoordinater.setText(tvGpsKoordinater.getText());
        editEtasjer.setText(tvEtasjer.getText());

        return alertDialog.create();

    }
}
