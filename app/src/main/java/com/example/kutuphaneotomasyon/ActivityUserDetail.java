package com.example.kutuphaneotomasyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.classes.Kullanicilar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityUserDetail extends AppCompatActivity {

    private Button buttonKitapAlisTeslim,buttonKitapBagis,buttonKullaniciDetayBagisla;
    private ConstraintLayout consKitapBagis;
    private TextView textViewKullaniciDetayAdSoyad,textViewKullaniciDetayKitapBagisAdet,textViewKullaniciDetayMevcutPuan;
    private EditText editTextKullaniciDetayKitapAdetGir;

    private Kullanicilar gelenKullanici;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        textViewKullaniciDetayAdSoyad = findViewById(R.id.textViewKullaniciDetayAdSoyad);
        buttonKitapAlisTeslim = findViewById(R.id.buttonKitapAlisTeslim);
        consKitapBagis = findViewById(R.id.consKitapBagis);
        buttonKitapBagis = findViewById(R.id.buttonKitapBagis);
        buttonKullaniciDetayBagisla = findViewById(R.id.buttonKullaniciDetayBagisla);
        textViewKullaniciDetayKitapBagisAdet = findViewById(R.id.textViewKullaniciDetayKitapBagisAdet);
        textViewKullaniciDetayMevcutPuan = findViewById(R.id.textViewKullaniciDetayMevcutPuan);
        editTextKullaniciDetayKitapAdetGir = findViewById(R.id.editTextKullaniciDetayKitapAdetGir);

        gelenKullanici = new Kullanicilar();
        gelenKullanici.setKullanici_id(Integer.parseInt(getIntent().getStringExtra("kullanici_id")));
        gelenKullanici.setAd(getIntent().getStringExtra("kullanici_ad"));



        textViewKullaniciDetayAdSoyad.setText(gelenKullanici.getAd());

        buttonKitapAlisTeslim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityUserDetail.this, ActivityBookInteraction.class);
                intent.putExtra("kullanici_id",String.valueOf(gelenKullanici.getKullanici_id()));
                startActivity(intent);
            }
        });

        buttonKitapBagis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    consKitapBagis.setVisibility(View.VISIBLE);
                    bagisBilgiCek(gelenKullanici.getKullanici_id());
                }catch (Exception e){
                    Log.e("bagisYapmaException",String.valueOf(e));
                }

            }
        });

        buttonKullaniciDetayBagisla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bagis kaydediliyor.
                int guncelAdet = gelenKullanici.getBagis_kitap_say()+Integer.parseInt(editTextKullaniciDetayKitapAdetGir.getText().toString());
                int eklenecekNot = Integer.parseInt(editTextKullaniciDetayKitapAdetGir.getText().toString()) * 5;
                double guncelNot = gelenKullanici.getNotu()+ (double)eklenecekNot;

                notGuncelle(gelenKullanici.getKullanici_id(),guncelAdet,guncelNot);

                consKitapBagis.setVisibility(View.INVISIBLE);

                //burası klavyeyi kapatıyor.
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextKullaniciDetayKitapAdetGir.getWindowToken(), 0);
            }
        });

    }

    public void bagisBilgiCek(int id){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/bagis_puan_cek.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray liste = jsonObject.getJSONArray("kullanici_table");

                    for (int i=0;i<liste.length();i++){
                        JSONObject j = liste.getJSONObject(i);

                        Integer bagisAdet = j.getInt("bagislanan_kitap_say");
                        double notu = j.getInt("kullanici_notu");

                        gelenKullanici.setBagis_kitap_say(bagisAdet);
                        gelenKullanici.setNotu(notu);
                    }
                    textViewKullaniciDetayKitapBagisAdet.setText(String.valueOf(gelenKullanici.getBagis_kitap_say()));
                    textViewKullaniciDetayMevcutPuan.setText(String.valueOf(gelenKullanici.getNotu()));

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
        Volley.newRequestQueue(ActivityUserDetail.this).add(istek);
    }

    public void notGuncelle(int id,int bagisSay,double not){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/update_bagisadet_not.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("notGnclleRspns",response);
                editTextKullaniciDetayKitapAdetGir.setText("");
                Toast.makeText(getApplicationContext(),"Bağış İşlemi Tamamlandı.Lütfen Kitapları Sisteme Kaydedin.",Toast.LENGTH_SHORT).show();
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
                params.put("bagislanan_kitap_say",String.valueOf(bagisSay));
                params.put("kullanici_notu",String.valueOf(not));
                return params;
            }
        };
        Volley.newRequestQueue(ActivityUserDetail.this).add(istek);
    }


}