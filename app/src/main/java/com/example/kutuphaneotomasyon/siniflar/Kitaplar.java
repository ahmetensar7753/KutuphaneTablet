package com.example.kutuphaneotomasyon.siniflar;

public class Kitaplar {

    private int kitap_id;
    private String kitap_ad;
    private String ozet;
    private String resim_ad;
    private double puan;
    private String bolge;
    private String raf;
    private int bagli_kategori_id;
    private int bagli_yazar_id;
    private String yazar_ad;
    private String kategori_ad;
    private double toplam_puan;
    private int oy_say;

    public double getToplam_puan() {
        return toplam_puan;
    }

    public void setToplam_puan(double toplam_puan) {
        this.toplam_puan = toplam_puan;
    }

    public int getOy_say() {
        return oy_say;
    }

    public void setOy_say(int oy_say) {
        this.oy_say = oy_say;
    }

    public Kitaplar() {
    }

    public Kitaplar(String kitap_ad, String ozet, String bolge, String raf, int bagli_kategori_id, String yazar_ad, String kategori_ad) {
        this.kitap_ad = kitap_ad;
        this.ozet = ozet;
        this.bolge = bolge;
        this.raf = raf;
        this.bagli_kategori_id = bagli_kategori_id;
        this.yazar_ad = yazar_ad;
        this.kategori_ad = kategori_ad;
    }

    public int getKitap_id() {
        return kitap_id;
    }

    public void setKitap_id(int kitap_id) {
        this.kitap_id = kitap_id;
    }

    public String getKitap_ad() {
        return kitap_ad;
    }

    public void setKitap_ad(String kitap_ad) {
        this.kitap_ad = kitap_ad;
    }

    public String getOzet() {
        return ozet;
    }

    public void setOzet(String ozet) {
        this.ozet = ozet;
    }

    public String getResim_ad() {
        return resim_ad;
    }

    public void setResim_ad(String resim_ad) {
        this.resim_ad = resim_ad;
    }

    public double getPuan() {
        return puan;
    }

    public void setPuan(double puan) {
        this.puan = puan;
    }

    public String getBolge() {
        return bolge;
    }

    public void setBolge(String bolge) {
        this.bolge = bolge;
    }

    public String getRaf() {
        return raf;
    }

    public void setRaf(String raf) {
        this.raf = raf;
    }

    public int getBagli_kategori_id() {
        return bagli_kategori_id;
    }

    public void setBagli_kategori_id(int bagli_kategori_id) {
        this.bagli_kategori_id = bagli_kategori_id;
    }

    public int getBagli_yazar_id() {
        return bagli_yazar_id;
    }

    public void setBagli_yazar_id(int bagli_yazar_id) {
        this.bagli_yazar_id = bagli_yazar_id;
    }

    public String getYazar_ad() {
        return yazar_ad;
    }

    public void setYazar_ad(String yazar_ad) {
        this.yazar_ad = yazar_ad;
    }

    public String getKategori_ad() {
        return kategori_ad;
    }

    public void setKategori_ad(String kategori_ad) {
        this.kategori_ad = kategori_ad;
    }
}
