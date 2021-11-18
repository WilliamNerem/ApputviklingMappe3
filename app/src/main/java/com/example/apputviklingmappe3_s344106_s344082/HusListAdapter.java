package com.example.apputviklingmappe3_s344106_s344082;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.List;

public class HusListAdapter extends ArrayAdapter<Hus> {
    private final Context mContext;
    private final int mResource;

    public HusListAdapter(Context context, int resource, List<Hus> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvId = (TextView) convertView.findViewById(R.id.itemHus);
        TextView tvBeskrivelse = (TextView) convertView.findViewById(R.id.itemHusBeskrivelse);
        TextView tvGateadresse = (TextView) convertView.findViewById(R.id.itemHusGateadresse);
        TextView tvEtasjer = (TextView) convertView.findViewById(R.id.itemHusEtasjer);

        int id = getItem(position).get_ID();
        String strId = String.valueOf(id);
        String beskrivelse = getItem(position).getBeskrivelse();
        String gateadresse = getItem(position).getGateadresse();
        int etasjer = getItem(position).getEtasjer();
        String strEtasjer = String.valueOf(etasjer);

        tvId.setText(strId);
        tvBeskrivelse.setText(beskrivelse);
        tvGateadresse.setText(gateadresse);
        tvEtasjer.setText(strEtasjer);
        return convertView;
    }

}
