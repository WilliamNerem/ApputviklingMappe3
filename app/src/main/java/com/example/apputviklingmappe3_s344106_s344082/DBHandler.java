package com.example.apputviklingmappe3_s344106_s344082;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBHandler extends SQLiteOpenHelper {
    static int DATABASE_VERSION = 3;
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
        JSONObject jsonOutput = new JSONObject();
        try {
            jsonOutput.put("beskrivelse", hus.getBeskrivelse());
            jsonOutput.put("gateadresse", hus.getGateadresse());
            jsonOutput.put("gps-lat", hus.getGps_lat());
            jsonOutput.put("gps-long", hus.getGps_long());
            jsonOutput.put("etasjer", hus.getEtasjer());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonOutputString = jsonOutput.toString();

        getJSON task = new getJSON();
        String[] params = {"http://studdata.cs.oslomet.no/~dbuser5/www/jsonin.php", "POST", jsonOutputString};
        task.execute(params);
    }

    public List<Hus> findAllHus() {
        getJSON task = new getJSON();
        String[] params = {"http://studdata.cs.oslomet.no/~dbuser5/www/jsonout.php", "GET"};
        try {
            return task.execute(params).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return allHus;
    }

    public void deleteHus(int in_id) {
        getJSON task = new getJSON();
        System.out.println(in_id);
        String[] params = {"http://studdata.cs.oslomet.no/~dbuser5/www/deleteHus.php?id=" + in_id, "GET"};
        task.execute(params);
    }

    public void updateHus(Hus hus) {
        JSONObject jsonOutput = new JSONObject();
        try {
            jsonOutput.put("id", hus.get_ID());
            jsonOutput.put("beskrivelse", hus.getBeskrivelse());
            jsonOutput.put("gateadresse", hus.getGateadresse());
            jsonOutput.put("gps-lat", hus.getGps_lat());
            jsonOutput.put("gps-long", hus.getGps_long());
            jsonOutput.put("etasjer", hus.getEtasjer());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String jsonOutputString = jsonOutput.toString();

        getJSON task = new getJSON();
        String[] params = {"http://studdata.cs.oslomet.no/~dbuser5/www/editHus.php?id=" + hus.get_ID(), "POST", jsonOutputString};
        task.execute(params);
    }

    public Hus findHus(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HUS, new String[]{
                        HUS_KEY_ID, HUS_KEY_BESKRIVELSE, HUS_KEY_GATEADRESSE, HUS_KEY_GPSLAT, HUS_KEY_GPSLONG, HUS_KEY_ETASJER}, HUS_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Hus hus = new
                Hus(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),
                cursor.getString(4), cursor.getInt(5));
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
                    HttpURLConnection conn = (HttpURLConnection) urlen.openConnection();
                    conn.setRequestMethod(urls[1]);
                    conn.setRequestProperty("Accept", "application/json");
                    if (urls[1].equals("POST")){
                        conn.setDoOutput(true);
                        try(OutputStream os = conn.getOutputStream()) {
                            System.out.println(urls[2]);
                            byte[] input = urls[2].getBytes(StandardCharsets.UTF_8);
                            os.write(input, 0, input.length);
                        }
                    }
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
                            hus.setGps_lat(gps_lat);
                            hus.setGps_long(gps_long);
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