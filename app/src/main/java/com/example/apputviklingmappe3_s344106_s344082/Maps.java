package com.example.apputviklingmappe3_s344106_s344082;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Maps extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    DBHandler db;
    private ImageView btnLeggTil;
    static public ArrayList<Marker> markers;
    static public List<Hus> alleHus;
    public List<Address> adresses;
    LatLng newMark;
    Geocoder geocoder;
    boolean alreadyMark;
    Marker markAdded;

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
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getTitle().equals("Trykk her for å legge til hus")) {
                    Intent i = new Intent(Maps.this, LeggTil.class);
                    System.out.println(marker.getPosition());
                    i.putExtra("lat,long", marker.getPosition());
                    startActivity(i);
                }
            }
        });
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if(alreadyMark) {
                    markAdded.remove();
                    alreadyMark = false;
                }
                newMark = new LatLng(latLng.latitude, latLng.longitude);
                geocoder = new Geocoder(Maps.this, Locale.getDefault());
                try {
                    adresses = geocoder.getFromLocation(newMark.latitude, newMark.longitude, 1);
                } catch (IOException e) {
                    Toast.makeText(Maps.this, "Ikke en gyldig adresse!", Toast.LENGTH_SHORT).show();

                }
                if (adresses == null) {
                    Toast.makeText(Maps.this, "Ikke en gyldig adresse!", Toast.LENGTH_LONG);
                } else {
                    MarkerOptions markerOptions = new MarkerOptions();
                    if ((latLng.latitude < 59.91910325593771 || latLng.latitude > 59.92291805910473)
                            && (latLng.longitude < 10.73202922940254 || latLng.longitude > 10.738811194896696)) {
                        Toast.makeText(Maps.this, "Adressen er ikke på OsloMet!", Toast.LENGTH_SHORT).show();
                    } else {
                        markerOptions.position(latLng);
                        markerOptions.title("Trykk her for å legge til hus");
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        markAdded = mMap.addMarker(markerOptions);
                        markers.add(markAdded);
                    }
                    alreadyMark = true;
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
