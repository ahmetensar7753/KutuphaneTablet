package com.example.kutuphaneotomasyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.adapters.KitaplarAdapter;
import com.example.kutuphaneotomasyon.siniflar.Kitaplar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KitapAraActivity extends AppCompatActivity {

    private Toolbar toolbarKitapAra;
    private RecyclerView rvKitapAra;

    private ArrayList<Kitaplar> vtDenGelenKitaplar;
    private KitaplarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_ara);

        toolbarKitapAra = findViewById(R.id.toolbarKitapAra);
        rvKitapAra = findViewById(R.id.rvKitapAra);
        

        toolbarKitapAra.setTitle("");
        setSupportActionBar(toolbarKitapAra);

        kitaplarCek();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kitap_ara_menu,menu);
        MenuItem item = menu.findItem(R.id.action_kitap_ara);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("newText1",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("newText",newText);

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


    public void kitaplarCek(){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/kitaplar_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenGelenKitaplar = new ArrayList<>();
                Log.e("rspns",response);
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

                    adapter = new KitaplarAdapter(KitapAraActivity.this,vtDenGelenKitaplar);
                    rvKitapAra.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
                    rvKitapAra.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(KitapAraActivity.this).add(istek);
    }


}