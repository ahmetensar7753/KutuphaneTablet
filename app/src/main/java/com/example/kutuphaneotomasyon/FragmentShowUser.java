package com.example.kutuphaneotomasyon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.adapters.UserAdapter;
import com.example.kutuphaneotomasyon.classes.Kullanicilar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentShowUser extends Fragment {

    private Toolbar toolbarKullaniciGoruntule;
    private RecyclerView rvKullanicilar;
    private int sayac0 = 0 ;

    private ArrayList<Kullanicilar> vtDenGelenKullanicilar;
    private UserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_user,container,false);
        
        toolbarKullaniciGoruntule = rootView.findViewById(R.id.toolbarKullaniciGoruntule);
        rvKullanicilar = rootView.findViewById(R.id.rvKullanicilar);

        toolbarKullaniciGoruntule.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarKullaniciGoruntule);
        setHasOptionsMenu(true);

        tumKullanicilariCek();



        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //BURADA SAYAÇ KULLANMAMIN AMACI, PARENT FRAGMENT GEÇİŞLERİNDE GERİ GELİNDİĞİNDE AYNI FRAGMENTA BURADAKİ MENU TEKRAR TEKRAR ÇALIŞTIĞINDA BİRDEN-
        //FAZLA MENU İTEMİ OLUŞUYOR. SAYAÇ İLE BU ÖNLENİYOR.
        if (sayac0 == 0){
            super.onCreateOptionsMenu(menu,inflater);
            getActivity().getMenuInflater().inflate(R.menu.search_user_menu,menu);
            sayac0++;
        }
        try {
            MenuItem item = menu.findItem(R.id.action_kullanici_ara);
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
        }catch (Exception e){
            Log.e("EXCEPTİON",String.valueOf(e)+"-"+"ÇÖZÜLMESEDE OLUR ÇALIŞIYOR sorunsuz");
        }

    }

    public void tumKullanicilariCek(){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/tum_kullanicilari_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenGelenKullanicilar = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kullanicilar = jsonObject.getJSONArray("kullanici_table");

                    for (int i=0;i<kullanicilar.length();i++){
                        JSONObject j = kullanicilar.getJSONObject(i);

                        Integer id = j.getInt("kullanici_id");
                        String ad = j.getString("kullanici_ad");
                        String soyad = j.getString("kullanici_soyad");

                        Kullanicilar k = new Kullanicilar();
                        k.setKullanici_id(id);
                        k.setAd(ad);
                        k.setSoyad(soyad);

                        vtDenGelenKullanicilar.add(k);
                    }

                    adapter = new UserAdapter(getActivity(),vtDenGelenKullanicilar);
                    rvKullanicilar.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
                    rvKullanicilar.setAdapter(adapter);


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
