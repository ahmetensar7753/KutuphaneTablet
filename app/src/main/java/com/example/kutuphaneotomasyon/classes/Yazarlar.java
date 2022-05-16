package com.example.kutuphaneotomasyon.classes;

public class Yazarlar {

    private int yazar_id;
    private String yazar_ad;

    public Yazarlar() {
    }

    public Yazarlar(int yazar_id, String yazar_ad) {
        this.yazar_id = yazar_id;
        this.yazar_ad = yazar_ad;
    }

    public int getYazar_id() {
        return yazar_id;
    }

    public void setYazar_id(int yazar_id) {
        this.yazar_id = yazar_id;
    }

    public String getYazar_ad() {
        return yazar_ad;
    }

    public void setYazar_ad(String yazar_ad) {
        this.yazar_ad = yazar_ad;
    }
}
