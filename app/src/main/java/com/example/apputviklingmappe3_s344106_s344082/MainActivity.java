package com.example.apputviklingmappe3_s344106_s344082;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apputviklingmappe3_s344106_s344082.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    EditText lokasjon;
    TextView koordinater;
    String minadresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*textView = (TextView) findViewById(R.id.jsontext);
        getJSON task = new getJSON();
        task.execute(new
                String[]{"http://studdata.cs.oslomet.no/~dbuser5/jsonout.php"});
         */
        lokasjon = (EditText) findViewById(R.id.lokasjon);
        koordinater = (TextView) findViewById(R.id.koordinater);
        Button knapp = (Button) findViewById(R.id.hentkoordinater);
    }

    public void hentkoordinater(View v) {
        minadresse = lokasjon.getText().toString();
        GetLocationTask hentadresser = new GetLocationTask(minadresse);
        hentadresser.execute();
    }

    private class GetLocationTask extends AsyncTask<Void, Void, String> {
        JSONObject jsonObject;
        String address;
        String lokasjon;

        public GetLocationTask(String a) {
            this.address = a;
        }

        @Override
        protected String doInBackground(Void... params) {
            String s = "";
            String output = "";
            String query = "https://maps.googleapis.com/maps/api/geocode/json?address = " + address.replaceAll(" ", " % 20 ") + " & key = AIzaSyAkWE1BEH5sTG3s0KZqE6vl4agAuJ7qzwc ";
            try {
                URL urlen = new URL(query);
                HttpURLConnection conn = (HttpURLConnection) urlen.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new
                        InputStreamReader((conn.getInputStream())));
                while ((s = br.readLine()) != null) {
                    output = output + s;
                }
                jsonObject = new JSONObject(output.toString());
                conn.disconnect();
                Double lon = Double.valueOf(0);
                Double lat = Double.valueOf(0);
                lon =
                        ((JSONArray)
                                jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                lat = ((JSONArray)
                        jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                lokasjon = String.valueOf(lon) + " : " + String.valueOf(lat);
                return lokasjon;
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return lokasjon;
        }

        @Override
        protected void onPostExecute(String resultat) {
            koordinater.setText(resultat);
        }
    }

    private class getJSON extends AsyncTask<String, Void, String> {
        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... urls) {
            String retur = "";
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
                            String name = jsonobject.getString("name");
                            retur = retur + name + "\n";
                        }
                        return retur;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return retur;
                } catch (Exception e) {
                    return "Noe gikk feil";
                }
            }
            return retur;
        }
        /*@Override
        protected void onPostExecute(String ss) {
            textView.setText(ss);
        }
         */
    }
}

