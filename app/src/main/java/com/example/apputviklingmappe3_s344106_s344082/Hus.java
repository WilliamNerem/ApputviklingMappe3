package com.example.apputviklingmappe3_s344106_s344082;

public class Hus {
    int _ID;
    String beskrivelse;
    String gateadresse;
    String gpsKoordinater;
    int etasjer;

    public Hus() {}

    public Hus(String beskrivelse, String gateadresse, String gpsKoordinater, int etasjer) {
        this.beskrivelse = beskrivelse;
        this.gateadresse = gateadresse;
        this.gpsKoordinater = gpsKoordinater;
        this.etasjer = etasjer;
    }

    public Hus(int _ID, String beskrivelse, String gateadresse, String gpsKoordinater, int etasjer) {
        this._ID = _ID;
        this.beskrivelse = beskrivelse;
        this.gateadresse = gateadresse;
        this.gpsKoordinater = gpsKoordinater;
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

    public String getGpsKoordinater() {
        return gpsKoordinater;
    }

    public void setGpsKoordinater(String gpsKoordinater) {
        this.gpsKoordinater = gpsKoordinater;
    }

    public int getEtasjer() {
        return etasjer;
    }

    public void setEtasjer(int etasjer) {
        this.etasjer = etasjer;
    }
}
