package com.example.apputviklingmappe3_s344106_s344082;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DBHandler extends SQLiteOpenHelper {
    static int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "studdb5";
    static String TABLE_HUS = "Hus";
    static String HUS_KEY_ID = "id";
    static String HUS_KEY_BESKRIVELSE = "beskrivelse";
    static String HUS_KEY_GATEADRESSE = "gateadresse";
    static String HUS_KEY_GPSLAT = "gps_lat";
    static String HUS_KEY_GPSLONG = "gps_long";
    static String HUS_KEY_ETASJER = "etasjer";
    static ArrayList<Hus> allHus;


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        new getJSON();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public void addHus(Hus hus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String gps = HUS_KEY_GPSLAT + ", " + HUS_KEY_GPSLONG;

        values.put(HUS_KEY_BESKRIVELSE, hus.getBeskrivelse());
        values.put(HUS_KEY_GATEADRESSE, hus.getGateadresse());
        values.put(gps, hus.getGpsKoordinater());
        values.put(HUS_KEY_ETASJER, hus.getEtasjer());
        db.insert(TABLE_HUS, null, values);
        db.close();
    }

    public List<Hus> findAllHus() {
        getJSON task = new getJSON();
        try {
            return task.execute(new
                    String[]{"http://studdata.cs.oslomet.no/~dbuser5/www/jsonout.php"}).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allHus;
    }

    public void deleteHus(int in_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HUS, HUS_KEY_ID + " =? ",

                new String[]{Integer.toString(in_id)});
        db.close();
    }

    public void updateHus(Hus hus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HUS_KEY_BESKRIVELSE, hus.getBeskrivelse());
        values.put(HUS_KEY_GATEADRESSE, hus.getGateadresse());
        String gps = HUS_KEY_GPSLAT + ", " + HUS_KEY_GPSLONG;
        values.put(gps, hus.getGpsKoordinater());
        values.put(HUS_KEY_ETASJER, hus.getEtasjer());
        db.update(TABLE_HUS, values, HUS_KEY_ID + "= ?",
                new String[]{String.valueOf(hus.get_ID())});
        db.close();
    }

    public Hus findHus(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HUS, new String[]{
                        HUS_KEY_ID, HUS_KEY_BESKRIVELSE, HUS_KEY_GATEADRESSE, HUS_KEY_GPSLAT, HUS_KEY_GPSLONG, HUS_KEY_ETASJER}, HUS_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        String gps = cursor.getString(3) + ", " + cursor.getString(4);
        Hus hus = new
                Hus(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                gps, cursor.getInt(5));
        cursor.close();
        db.close();
        return hus;
    }


    private class getJSON extends AsyncTask<String, Void, ArrayList<Hus>> {
        JSONObject jsonObject;

        @Override
        protected ArrayList<Hus> doInBackground(String... urls) {
            ArrayList<Hus> retur = new ArrayList<>();
            String s = "";
            String output = "";
            for (String url : urls) {
                try {
                    URL urlen = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection)
                            urlen.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept",
                            "application/json");
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    System.out.println("Output from Server .... \n");
                    while ((s = br.readLine()) != null) {
                        output = output + s;
                    }
                    conn.disconnect();
                    try {
                        JSONArray mat = new JSONArray(output);
                        for (int i = 0; i < mat.length(); i++) {
                            JSONObject jsonobject = mat.getJSONObject(i);
                            String id = jsonobject.getString("id");
                            String beskrivelse = jsonobject.getString("beskrivelse");
                            String gateadresse = jsonobject.getString("gateadresse");
                            String gps_lat = jsonobject.getString("gps_lat");
                            String gps_long = jsonobject.getString("gps_long");
                            String etasjer = jsonobject.getString("etasjer");
                            Hus hus = new Hus();
                            hus.set_ID(Integer.parseInt(id));
                            hus.setBeskrivelse(beskrivelse);
                            hus.setGateadresse(gateadresse);
                            String gps = gps_lat + ", " + gps_long;
                            hus.setGpsKoordinater(gps);
                            hus.setEtasjer(Integer.parseInt(etasjer));
                            retur.add(hus);
                        }
                        return retur;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return retur;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return retur;
        }
        @Override
        protected void onPostExecute(ArrayList<Hus> hus) {
            System.out.println("Kommer hit!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            allHus = hus;
        }
    }
}