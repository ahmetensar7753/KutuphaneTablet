package com.example.kutuphaneotomasyon.siniflar;

public class Kategoriler {
    private int kategori_id;
    private String ad;

    public Kategoriler() {
    }

    public Kategoriler(int kategori_id, String ad) {
        this.kategori_id = kategori_id;
        this.ad = ad;
    }

    public int getKategori_id() {
        return kategori_id;
    }

    public void setKategori_id(int kategori_id) {
        this.kategori_id = kategori_id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }
}
