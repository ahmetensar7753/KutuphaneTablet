package com.example.kutuphaneotomasyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
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
import com.example.kutuphaneotomasyon.adapters.KategoriAdapter;
import com.example.kutuphaneotomasyon.adapters.KitaplarAdapter;
import com.example.kutuphaneotomasyon.adapters.YazarAdapter;
import com.example.kutuphaneotomasyon.siniflar.Kategoriler;
import com.example.kutuphaneotomasyon.siniflar.Kitaplar;
import com.example.kutuphaneotomasyon.siniflar.Yazarlar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YazarAraActivity extends AppCompatActivity {

    private Toolbar toolbarYazarAra;
    private RecyclerView rvYazarAra;

    private ArrayList<Yazarlar> vtDenGelenYazarlar;
    private YazarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yazar_ara);

        toolbarYazarAra  = findViewById(R.id.toolbarYazarAra);
        rvYazarAra  = findViewById(R.id.rvYazarAra);

        toolbarYazarAra.setTitle("");
        setSupportActionBar(toolbarYazarAra);

        yazarlarCek();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yazar_ara_menu,menu);
        MenuItem item = menu.findItem(R.id.action_yazar_ara);
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

    public void yazarlarCek(){
        String url = "https://kristalekmek.com/kutuphane/yazarlar/yazarlar_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenGelenYazarlar = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray yazarlar = jsonObject.getJSONArray("yazar_table");

                    for (int i=0;i<yazarlar.length();i++){
                        JSONObject j = yazarlar.getJSONObject(i);

                        Integer yazarID = j.getInt("yazar_id");
                        String yazarAD = j.getString("yazar_ad");

                        Yazarlar yazar = new Yazarlar(yazarID,yazarAD);
                        vtDenGelenYazarlar.add(yazar);
                    }

                    adapter = new YazarAdapter(YazarAraActivity.this,vtDenGelenYazarlar);
                    rvYazarAra.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
                    rvYazarAra.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(YazarAraActivity.this).add(istek);
    }

}