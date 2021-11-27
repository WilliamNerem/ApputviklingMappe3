package com.example.apputviklingmappe3_s344106_s344082;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;

public class EditDeletePopup extends AppCompatActivity {

    public static AlertDialog buildAlertDialog(View view, int husID, String husBeskrivelse, String husAdresse, int husEtasjer, LatLng husKoordinater, String redirect){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setView(R.layout.alert_popup);

        LayoutInflater alertInflater = LayoutInflater.from(view.getContext());
        View alertConvertView = alertInflater.inflate(R.layout.alert_popup, null);

        alertDialog.setView(alertConvertView);
        Button buttonEdit = (Button) alertConvertView.findViewById(R.id.buttonEdit);
        Button buttonSlett = (Button) alertConvertView.findViewById(R.id.buttonSlett);

        AlertDialog dialog = alertDialog.create();

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditHus.class);
                if (redirect.equals("maps")){
                    intent.putExtra("origin", "maps");
                } else {
                    intent.putExtra("origin", "list");
                }
                intent.putExtra("id", husID);
                intent.putExtra("beskrivelse", husBeskrivelse);
                intent.putExtra("gateadresse", husAdresse);
                intent.putExtra("etasjer", husEtasjer);
                intent.putExtra("lat,long", husKoordinater);

                dialog.dismiss();
                view.getContext().startActivity(intent);
                if (redirect.equals("husList")){
                    ((Activity)view.getContext()).finish();
                }
            }
        });

        buttonSlett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHandler db = new DBHandler(view.getContext());
                db.deleteHus(husID);
                dialog.dismiss();
                if (redirect.equals("husList")){
                    view.getContext().startActivity(new Intent(view.getContext(), HusList.class));
                } else {
                    view.getContext().startActivity(new Intent(view.getContext(), Maps.class));
                }
                ((Activity)view.getContext()).finish();
            }
        });
        return dialog;
    }
}
