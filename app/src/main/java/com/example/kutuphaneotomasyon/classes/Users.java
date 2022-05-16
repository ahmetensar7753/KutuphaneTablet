package com.example.kutuphaneotomasyon.classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Users {

    private int kullanici_id;
    private String tc;
    private String ad;
    private String soyad;
    private String telefon;
    private String mail;
    private String sifre;
    private String kayit_tarih;
    private int okudugu_kitap_say;
    private double notu;
    private int bagis_kitap_say;
    private boolean yetki;
    private String gorev;

    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    Date tarih = new Date();



    public Users() {
    }

    public Users(String tc, String ad, String soyad, String telefon, String mail) {
        this.tc = tc;
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
        this.mail = mail;
        this.sifre = tc; // otomatik ilk olusturmada sifre == tcNo olarak atanıyor.
        this.kayit_tarih = df.format(tarih);
    }

    public int getKullanici_id() {
        return kullanici_id;
    }

    public void setKullanici_id(int kullanici_id) {
        this.kullanici_id = kullanici_id;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getKayit_tarih() {
        return kayit_tarih;
    }

    public void setKayit_tarih(String kayit_tarih) {
        this.kayit_tarih = kayit_tarih;
    }

    public int getOkudugu_kitap_say() {
        return okudugu_kitap_say;
    }

    public void setOkudugu_kitap_say(int okudugu_kitap_say) {
        this.okudugu_kitap_say = okudugu_kitap_say;
    }

    public double getNotu() {
        return notu;
    }

    public void setNotu(double notu) {
        this.notu = notu;
    }

    public int getBagis_kitap_say() {
        return bagis_kitap_say;
    }

    public void setBagis_kitap_say(int bagis_kitap_say) {
        this.bagis_kitap_say = bagis_kitap_say;
    }

    public boolean isYetki() {
        return yetki;
    }

    public void setYetki(boolean yetki) {
        this.yetki = yetki;
    }

    public String getGorev() {
        return gorev;
    }

    public void setGorev(String gorev) {
        this.gorev = gorev;
    }

    public DateFormat getDf() {
        return df;
    }

    public void setDf(DateFormat df) {
        this.df = df;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }
}
