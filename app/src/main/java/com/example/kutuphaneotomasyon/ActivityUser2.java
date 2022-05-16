package com.example.kutuphaneotomasyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.adapters.UserBookHistoryAdapter;
import com.example.kutuphaneotomasyon.classes.AlinanKitaplar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityUser2 extends AppCompatActivity {

    private Integer kullaniciID;
    private UserBookHistoryAdapter adapter;
    private ArrayList<AlinanKitaplar> vtDenCekilenAlinanlar;
    private RecyclerView rvKullaniciKitapGecmisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);

        kullaniciID = Integer.parseInt(getIntent().getStringExtra("kullanici_id"));
        rvKullaniciKitapGecmisi = findViewById(R.id.rvKullaniciKitapGecmisi);

        kitapGecmisCek(kullaniciID);

    }

    public void kitapGecmisCek(int id){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/alinan_kitaplar_cek.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenCekilenAlinanlar = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray liste = jsonObject.getJSONArray("alinan_kitaplar_table");

                    for (int i=0;i<liste.length();i++){
                        JSONObject j = liste.getJSONObject(i);

                        Integer kayitId = j.getInt("kayit_id");
                        String kitapAd = j.getString("kitap_ad");
                        String yazar = j.getString("kitap_yazar");
                        String alisTarih = j.getString("alis_tarih");
                        String teslimTarih = j.getString("teslim_tarih");
                        String teslimDurumu = j.getString("teslim_durumu");

                        AlinanKitaplar alinanKitap = new AlinanKitaplar(kayitId,kitapAd,yazar,alisTarih,teslimTarih,id);
                        alinanKitap.setTeslim_durumu(Integer.parseInt(teslimDurumu));

                        vtDenCekilenAlinanlar.add(alinanKitap);
                    }

                    adapter = new UserBookHistoryAdapter(ActivityUser2.this,vtDenCekilenAlinanlar);
                    rvKullaniciKitapGecmisi.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                    rvKullaniciKitapGecmisi.setAdapter(adapter);


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
        Volley.newRequestQueue(ActivityUser2.this).add(istek);
    }

}