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

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Maps extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
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
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pilestredet).title("Pilestredet testhus")
                .snippet("Dette er en snippet\n Her kan informasjon være");
        Marker marker = googleMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                marker.showInfoWindow();
            }
        });
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

        MyInfoWindowAdapter() {
            contents = getLayoutInflater().inflate(R.layout.infowindow, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
        @Override
        public View getInfoContents(Marker marker) {
            String title = marker.getTitle();
            TextView txtTitle = ((TextView) contents.findViewById(R.id.title));
            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                txtTitle.setText(titleText);
            } else {
                txtTitle.setText("");
            }
            TextView txtType = ((TextView) contents.findViewById(R.id.snippet));
            txtType.setText("La oss håpe dette funker\nFor dette funker fortsatt?\nDET ER JO ET HUS!!\nSa brura\nI gangen hehe");
            return contents;

        }

    }
}
