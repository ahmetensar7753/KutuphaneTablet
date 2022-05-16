package com.example.kutuphaneotomasyon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.adapters.KategoriAdapter;
import com.example.kutuphaneotomasyon.siniflar.Kategoriler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KategoriAraActivity extends AppCompatActivity {

    private RecyclerView rvKategoriAra;
    private ArrayList<Kategoriler> vtDenCekilenKategoriler;
    private KategoriAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_ara);

        rvKategoriAra = findViewById(R.id.rvKategoriAra);

        kategorilerCek();

    }

    public void kategorilerCek(){
        String url = "https://kristalekmek.com/kutuphane/kategoriler/kategoriler_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenCekilenKategoriler = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kategoriler = jsonObject.getJSONArray("kategori_table");

                    for (int i=0;i<kategoriler.length();i++){
                        JSONObject j = kategoriler.getJSONObject(i);

                        Integer kategoriID = j.getInt("kategori_id");
                        String kategoriAD = j.getString("kategori_ad");

                        Kategoriler k = new Kategoriler(kategoriID,kategoriAD);

                        vtDenCekilenKategoriler.add(k);
                    }

                    adapter = new KategoriAdapter(KategoriAraActivity.this,vtDenCekilenKategoriler);
                    rvKategoriAra.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
                    rvKategoriAra.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(KategoriAraActivity.this).add(istek);
    }

}