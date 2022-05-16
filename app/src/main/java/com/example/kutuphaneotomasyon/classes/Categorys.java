package com.example.kutuphaneotomasyon.classes;

public class Categorys {
    private int kategori_id;
    private String ad;

    public Categorys() {
    }

    public Categorys(int kategori_id, String ad) {
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
