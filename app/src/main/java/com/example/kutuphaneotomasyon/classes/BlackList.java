package com.example.kutuphaneotomasyon.classes;

public class BlackList {
    private int kayit_id;
    private String ad;
    private String tarih;

    public BlackList() {
    }

    public BlackList(int kayit_id, String ad, String tarih) {
        this.kayit_id = kayit_id;
        this.ad = ad;
        this.tarih = tarih;
    }

    public int getKayit_id() {
        return kayit_id;
    }

    public void setKayit_id(int kayit_id) {
        this.kayit_id = kayit_id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
