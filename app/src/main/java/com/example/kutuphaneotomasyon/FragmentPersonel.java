package com.example.kutuphaneotomasyon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.adapters.PersonelAdapter;
import com.example.kutuphaneotomasyon.siniflar.Kullanicilar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentPersonel extends Fragment {

    private RecyclerView rvPersonelDuzenle;
    private ArrayList<Kullanicilar> vtDenCekilenPersoneller;

    private PersonelAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personel,container,false);

        rvPersonelDuzenle = rootView.findViewById(R.id.rvPersonelDuzenle);

        personelleriCek();

        return rootView;
    }

    public void personelleriCek(){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/personelleri_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenCekilenPersoneller = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray personeller = jsonObject.getJSONArray("kullanici_table");
                    for (int i=0;i<personeller.length();i++){
                        JSONObject j = personeller.getJSONObject(i);

                        Integer id = j.getInt("kullanici_id");
                        String ad = j.getString("kullanici_ad");
                        String soyad = j.getString("kullanici_soyad");
                        String telefon = j.getString("kullanici_telefon");
                        String gorev = j.getString("gorev");

                        Kullanicilar personel = new Kullanicilar();
                        personel.setKullanici_id(id);
                        personel.setAd(ad);
                        personel.setSoyad(soyad);
                        personel.setTelefon(telefon);
                        personel.setGorev(gorev);

                        vtDenCekilenPersoneller.add(personel);
                    }

                    adapter = new PersonelAdapter(getActivity(),vtDenCekilenPersoneller);
                    rvPersonelDuzenle.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                    rvPersonelDuzenle.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getActivity()).add(istek);
    }

}
