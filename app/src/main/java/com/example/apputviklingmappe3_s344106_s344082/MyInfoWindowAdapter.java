/*package com.example.apputviklingmappe3_s344106_s344082;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    MyInfoWindowAdapter(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        myContentsView = inflater.inflate(R.layout.infowindow, null);
    }


    @Override
    public View getInfoContents(Marker marker) {

        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
        tvTitle.setText(marker.getTitle());
        TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
        tvSnippet.setText(marker.getSnippet());

        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }
}

 */
