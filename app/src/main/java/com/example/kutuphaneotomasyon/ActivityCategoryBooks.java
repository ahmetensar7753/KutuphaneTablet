package com.example.kutuphaneotomasyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.adapters.BooksAdapter;
import com.example.kutuphaneotomasyon.classes.Kategoriler;
import com.example.kutuphaneotomasyon.classes.Kitaplar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityCategoryBooks extends AppCompatActivity {

    private Kategoriler kategori;

    private TextView textViewKategoriKitaplariKategoriAdi;
    private RecyclerView rvKategoriKitaplari;

    private ArrayList<Kitaplar> vtDenGelenKitaplar;
    private BooksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_books);

        Integer kategoriID = Integer.parseInt(getIntent().getStringExtra("kategori_id"));
        String kategoriAD = getIntent().getStringExtra("kategori_ad");

        kategori = new Kategoriler(kategoriID,kategoriAD);

        textViewKategoriKitaplariKategoriAdi = findViewById(R.id.textViewKategoriKitaplariKategoriAdi);
        rvKategoriKitaplari = findViewById(R.id.rvKategoriKitaplari);

        textViewKategoriKitaplariKategoriAdi.setText(kategori.getAd());

        kategoriKitaplariCek(kategori.getKategori_id());

    }

    public void kategoriKitaplariCek(int kategori_id){
        String url = "https://kristalekmek.com/kutuphane/kategoriler/kategori_kitaplari_cek.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenGelenKitaplar = new ArrayList<>();
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

                        vtDenGelenKitaplar.add(kitap);
                    }

                    adapter = new BooksAdapter(ActivityCategoryBooks.this,vtDenGelenKitaplar);
                    rvKategoriKitaplari.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
                    rvKategoriKitaplari.setAdapter(adapter);

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
                params.put("kategori_id",String.valueOf(kategori_id));
                return params;
            }
        };
        Volley.newRequestQueue(ActivityCategoryBooks.this).add(istek);
    }

}