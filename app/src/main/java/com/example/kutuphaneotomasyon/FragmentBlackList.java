package com.example.kutuphaneotomasyon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.kutuphaneotomasyon.adapters.BlackListAdapter;
import com.example.kutuphaneotomasyon.classes.KaraListe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentBlackList extends Fragment {

    private ArrayList<KaraListe> vtDenCekilenKaraListe;
    private RecyclerView rvKaraListe;
    private BlackListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_black_list,container,false);
        rvKaraListe = rootView.findViewById(R.id.rvKaraListe);

        karaListeCek();

        return rootView;
    }

    public void karaListeCek(){
        String url = "https://kristalekmek.com/kutuphane/kara_liste/kara_liste_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenCekilenKaraListe = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray liste = jsonObject.getJSONArray("kara_liste_table");
                    for (int i=0;i<liste.length();i++){
                        JSONObject j = liste.getJSONObject(i);

                        int id = j.getInt("kayit_id");
                        String ad = j.getString("kayit_ad_soyad");
                        String tarih = j.getString("tarih");

                        KaraListe karaListe = new KaraListe(id,ad,tarih);
                        vtDenCekilenKaraListe.add(karaListe);
                    }

                    adapter = new BlackListAdapter(getActivity(),vtDenCekilenKaraListe);
                    rvKaraListe.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                    rvKaraListe.setAdapter(adapter);



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
