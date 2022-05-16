package com.example.kutuphaneotomasyon.siniflar;

public class AlinanKitaplar {

    private int kayit_id;
    private String kitap_ad;
    private String kitap_yazar;
    private String alis_tarih;
    private String istenen_teslim_tarih;
    private int kullanici_id;
    private int teslim_durumu;

    public int getTeslim_durumu() {
        return teslim_durumu;
    }

    public void setTeslim_durumu(int teslim_durumu) {
        this.teslim_durumu = teslim_durumu;
    }

    public AlinanKitaplar() {
    }

    public AlinanKitaplar(int kayit_id, String kitap_ad, String kitap_yazar, String alis_tarih, String istenen_teslim_tarih, int kullanici_id) {
        this.kayit_id = kayit_id;
        this.kitap_ad = kitap_ad;
        this.kitap_yazar = kitap_yazar;
        this.alis_tarih = alis_tarih;
        this.istenen_teslim_tarih = istenen_teslim_tarih;
        this.kullanici_id = kullanici_id;
    }

    public int getKayit_id() {
        return kayit_id;
    }

    public void setKayit_id(int kayit_id) {
        this.kayit_id = kayit_id;
    }

    public String getKitap_ad() {
        return kitap_ad;
    }

    public void setKitap_ad(String kitap_ad) {
        this.kitap_ad = kitap_ad;
    }

    public String getKitap_yazar() {
        return kitap_yazar;
    }

    public void setKitap_yazar(String kitap_yazar) {
        this.kitap_yazar = kitap_yazar;
    }

    public String getAlis_tarih() {
        return alis_tarih;
    }

    public void setAlis_tarih(String alis_tarih) {
        this.alis_tarih = alis_tarih;
    }

    public String getIstenen_teslim_tarih() {
        return istenen_teslim_tarih;
    }

    public void setIstenen_teslim_tarih(String istenen_teslim_tarih) {
        this.istenen_teslim_tarih = istenen_teslim_tarih;
    }

    public int getKullanici_id() {
        return kullanici_id;
    }

    public void setKullanici_id(int kullanici_id) {
        this.kullanici_id = kullanici_id;
    }
}
