package com.example.apputviklingmappe3_s344106_s344082;

public class Hus {
    int _ID;
    String beskrivelse;
    String gateadresse;
    String gps_lat;
    String gps_long;
    int etasjer;

    public Hus() {}

    public Hus(String beskrivelse, String gateadresse, String gps_lat, String gps_long, int etasjer) {
        this.beskrivelse = beskrivelse;
        this.gateadresse = gateadresse;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.etasjer = etasjer;
    }

    public Hus(int _ID, String beskrivelse, String gateadresse, String gps_lat, String gps_long, int etasjer) {
        this._ID = _ID;
        this.beskrivelse = beskrivelse;
        this.gateadresse = gateadresse;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.etasjer = etasjer;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getGateadresse() {
        return gateadresse;
    }

    public void setGateadresse(String gateadresse) {
        this.gateadresse = gateadresse;
    }

    public String getGps_lat() {
        return gps_lat;
    }

    public void setGps_lat(String gps_lat) {
        this.gps_lat = gps_lat;
    }

    public String getGps_long() {
        return gps_long;
    }

    public void setGps_long(String gps_long) {
        this.gps_long = gps_long;
    }

    public int getEtasjer() {
        return etasjer;
    }

    public void setEtasjer(int etasjer) {
        this.etasjer = etasjer;
    }
}
