package com.example.apputviklingmappe3_s344106_s344082;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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

public class Maps extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    private ImageView mInfo;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mInfo = (ImageView) findViewById(R.id.image_info);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pilestredet = new LatLng(59.91957, 10.73556);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pilestredet));
        //MarkerOptions markerOptions = new MarkerOptions()
                //.position(pilestredet).title("Pilestredet testhus")
                //.snippet("Dette er en snippet\n Her kan informasjon være");
        //Marker marker = mMap.addMarker(markerOptions);
        Hus ethus = new Hus("Dette er en god beskrivelse", "Nisseveien 19A, 01940 Oslo","59.91957","10.73556",3);
        Hus tohus = new Hus("Dette er en enda mye bedre beskrivelse enn den gode beskrivelse", "Trynedittveien 19A, 01940 Oslo","59.91937","10.73596",3);
        //LatLng noeAnnet = new LatLng(Double.parseDouble(tohus.getGps_lat()), Double.parseDouble(tohus.getGps_long()));
        //LatLng pilestredet2 = new LatLng(59.91937, 10.73596);
        //MarkerOptions markerOptions2 = new MarkerOptions().position(pilestredet2);
        //Marker marker2 = mMap.addMarker(markerOptions2);
        addMarker(ethus);
        addMarker(tohus);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
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
        MarkerOptions markerOp = new MarkerOptions().position(location);
        Marker mark = mMap.addMarker(markerOp);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(hus));
    }

    private void init() {
        LatLng pilestredet = new LatLng(59.91957, 10.73556);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pilestredet).title("Pilestredet testhus")
                .snippet("Dette er en snippet\n Her kan informasjon være");
        Marker marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pilestredet));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        mInfo = (ImageView) findViewById(R.id.image_info);
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (mMarker.isInfoWindowShown()) {
                        mMarker.hideInfoWindow();
                    } else {

                        mMarker.showInfoWindow();
                    }
                } catch (NullPointerException e) {
                    System.out.println("Exception : " + e.getMessage());
                }
            }
        });
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View contents;
        private Hus hus;

        MyInfoWindowAdapter(Hus hus) {
            this.hus = hus;
            contents = getLayoutInflater().inflate(R.layout.infowindow, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
        @Override
        public View getInfoContents(Marker marker) {
            String title = hus.gateadresse;
            TextView txtTitle = ((TextView) contents.findViewById(R.id.title));
            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                txtTitle.setText(titleText);
            } else {
                txtTitle.setText("");
            }
            TextView txtsnippet = ((TextView) contents.findViewById(R.id.snippet));
            txtsnippet.setText("Beskrivelse:"+hus.getBeskrivelse()+"\nKoordinater:"+hus.getGps_lat()+", "+hus.getGps_long()+"\nEtasjer:"+hus.getEtasjer());
            return contents;

        }

    }
}
