package com.example.apputviklingmappe3_s344106_s344082;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Maps extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    DBHandler db;
    private ImageView btnLeggTil;
    static public ArrayList<Marker> markers;
    static public List<Hus> alleHus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = new DBHandler(this);
        alleHus = db.findAllHus();
        btnLeggTil = (ImageView) findViewById(R.id.list);
        System.out.println();
        markers = new ArrayList<>();
        buttons();
    }

    private void buttons(){
        btnLeggTil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Maps.this, HusList.class));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (Hus hus : alleHus) {
            System.out.println(hus);
            addMarker(hus);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(59.91957,10.73556))); // Viser Pilestredet
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                    return false;
                } else {
                    marker.showInfoWindow();
                    return true;
                }
            }
        });
    }

    private void addMarker(Hus hus) {
        LatLng location = new LatLng(Double.parseDouble(hus.getGps_lat()), Double.parseDouble(hus.getGps_long()));
        MarkerOptions markerOp = new MarkerOptions().position(location).title(hus.gateadresse).snippet("Beskrivelse:"+hus.getBeskrivelse()+"\nKoordinater:"+hus.getGps_lat()+", "+hus.getGps_long()+"\nEtasjer:"+hus.getEtasjer());
        Marker marker =  mMap.addMarker(markerOp);
        markers.add(marker);
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private View view;
        private TextView textTittel;
        private TextView textSnippet;

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
        @Override
        public View getInfoContents(Marker marker) {
            for(Marker mark : markers) {
                if(marker.equals(mark)) {
                    LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.infowindow, null);
                    textTittel = ((TextView) view.findViewById(R.id.title));
                    String title = marker.getTitle();
                    if (title != null) {
                        SpannableString titleText = new SpannableString(title);
                        titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                        textTittel.setText(titleText);
                    } else {
                        textTittel.setText("");
                    }
                    textSnippet = ((TextView) view.findViewById(R.id.snippet));
                    textSnippet.setText(marker.getSnippet());
                    return view;
                }
            }
            return null;
        }

    }
}
