package com.example.kutuphaneotomasyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.siniflar.Kullanicilar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KullaniciActivity extends AppCompatActivity {

    private TextView textViewKullaniciId,textViewKullaniciAdSoyad,textViewKullaniciTelNo,textViewKullaniciKayitTarih,
            textViewKullaniciOkuduguKitapSay,textViewKullaniciNot,textViewKullaniciBagisKitapSay;
    private Button buttonAldigiKitaplariGoruntule;

    private Integer kullaniciID;
    private Kullanicilar personel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici);

        kullaniciID = Integer.parseInt(getIntent().getStringExtra("kullanici_id"));

        textViewKullaniciId = findViewById(R.id.textViewKullaniciId);
        textViewKullaniciAdSoyad = findViewById(R.id.textViewKullaniciAdSoyad);
        textViewKullaniciTelNo = findViewById(R.id.textViewKullaniciTelNo);
        textViewKullaniciKayitTarih = findViewById(R.id.textViewKullaniciKayitTarih);
        textViewKullaniciOkuduguKitapSay = findViewById(R.id.textViewKullaniciOkuduguKitapSay);
        textViewKullaniciBagisKitapSay = findViewById(R.id.textViewKullaniciBagisKitapSay);
        textViewKullaniciNot = findViewById(R.id.textViewKullaniciNot);
        buttonAldigiKitaplariGoruntule = findViewById(R.id.buttonAldigiKitaplariGoruntule);

        textViewKullaniciId.setText(String.valueOf(kullaniciID));
        kullaniciCek3(kullaniciID);

        buttonAldigiKitaplariGoruntule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KullaniciActivity.this,KullaniciActivity2.class);
                intent.putExtra("kullanici_id",String.valueOf(kullaniciID));
                startActivity(intent);
            }
        });

    }

    public void kullaniciCek3(int id){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/kullanici_cek2.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                personel = new Kullanicilar();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray liste = jsonObject.getJSONArray("kullanici_table");
                    for (int i=0;i<liste.length();i++){
                        JSONObject j = liste.getJSONObject(i);

                        String ad = j.getString("kullanici_ad");
                        String soyad = j.getString("kullanici_soyad");
                        String tel = j.getString("kullanici_telefon");
                        String kayit_tarih = j.getString("kayit_tarihi");
                        String okuduguKitapSay = j.getString("okudugu_kitap_say");
                        String not = j.getString("kullanici_notu");
                        String bagisAdet = j.getString("bagislanan_kitap_say");

                        personel.setKullanici_id(id);
                        personel.setAd(ad);
                        personel.setSoyad(soyad);
                        personel.setTelefon(tel);
                        personel.setKayit_tarih(kayit_tarih);
                        personel.setOkudugu_kitap_say(Integer.parseInt(okuduguKitapSay));
                        personel.setNotu(Double.parseDouble(not));
                        personel.setBagis_kitap_say(Integer.parseInt(bagisAdet));
                    }

                    textViewKullaniciAdSoyad.setText(personel.getAd()+" "+personel.getSoyad());
                    textViewKullaniciTelNo.setText(personel.getTelefon());
                    textViewKullaniciKayitTarih.setText(personel.getKayit_tarih());
                    textViewKullaniciOkuduguKitapSay.setText(String.valueOf(personel.getOkudugu_kitap_say()));
                    textViewKullaniciNot.setText(String.valueOf(personel.getNotu()));
                    textViewKullaniciBagisKitapSay.setText(String.valueOf(personel.getBagis_kitap_say()));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("kullanici_id",String.valueOf(id));
                return params;
            }
        };
        Volley.newRequestQueue(KullaniciActivity.this).add(istek);
    }

}