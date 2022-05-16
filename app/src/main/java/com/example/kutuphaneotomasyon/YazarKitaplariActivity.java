package com.example.kutuphaneotomasyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.adapters.KitaplarAdapter;
import com.example.kutuphaneotomasyon.siniflar.Kitaplar;
import com.example.kutuphaneotomasyon.siniflar.Yazarlar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YazarKitaplariActivity extends AppCompatActivity {

    private TextView textViewYazarKitaplariYazarAdi;
    private RecyclerView rvYazarKitaplari;

    private Yazarlar yazar;

    private ArrayList<Kitaplar> yazarKitaplari;
    private KitaplarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yazar_kitaplari);

        textViewYazarKitaplariYazarAdi = findViewById(R.id.textViewYazarKitaplariYazarAdi);
        rvYazarKitaplari = findViewById(R.id.rvYazarKitaplari);

        yazar = new Yazarlar();

        yazar.setYazar_id(Integer.parseInt(getIntent().getStringExtra("yazar_id")));
        yazar.setYazar_ad(getIntent().getStringExtra("yazar_ad"));

        textViewYazarKitaplariYazarAdi.setText(yazar.getYazar_ad());

        yazarKitaplariCek(yazar.getYazar_id());

    }

    public void yazarKitaplariCek(int yazar_id){
        String url = "https://kristalekmek.com/kutuphane/yazarlar/yazar_kitaplari_cek.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                yazarKitaplari = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray list = jsonObject.getJSONArray("kitaplar_table");

                    for (int i = 0;i<list.length();i++){
                        JSONObject j = list.getJSONObject(i);

                        int id = j.getInt("kitap_id");
                        String ad = j.getString("kitap_ad");
                        String resimAd = j.getString("kitap_resim_ad");
                        String yazarAd = j.getString("yazar_ad");

                        Kitaplar kitap = new Kitaplar();
                        kitap.setKitap_id(id);
                        kitap.setKitap_ad(ad);
                        kitap.setResim_ad(resimAd);
                        kitap.setYazar_ad(yazarAd);

                        yazarKitaplari.add(kitap);
                    }

                    adapter = new KitaplarAdapter(YazarKitaplariActivity.this,yazarKitaplari);
                    rvYazarKitaplari.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
                    rvYazarKitaplari.setAdapter(adapter);

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
                params.put("yazar_id",String.valueOf(yazar_id));
                return params;
            }
        };
        Volley.newRequestQueue(YazarKitaplariActivity.this).add(istek);
    }

}