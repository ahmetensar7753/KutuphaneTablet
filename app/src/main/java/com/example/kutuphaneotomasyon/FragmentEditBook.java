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
import com.example.kutuphaneotomasyon.adapters.EditBookAdapter;
import com.example.kutuphaneotomasyon.classes.Kitaplar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentEditBook extends Fragment {

    private Toolbar toolbarKitapRafDuzenle;
    private RecyclerView rvKitapRafDuzenle;
    private int sayac2 = 0;

    private ArrayList<Kitaplar> vtDenCekilenKitaplar;
    private EditBookAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_book,container,false);

        toolbarKitapRafDuzenle = rootView.findViewById(R.id.toolbarKitapRafDuzenle);
        rvKitapRafDuzenle = rootView.findViewById(R.id.rvKitapRafDuzenle);

        toolbarKitapRafDuzenle.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarKitapRafDuzenle);
        setHasOptionsMenu(true);

        tumKitaplarCek();


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (sayac2 == 0){
            super.onCreateOptionsMenu(menu, inflater);
            getActivity().getMenuInflater().inflate(R.menu.edit_book_menu,menu);
            sayac2++;
        }
        try {
            MenuItem item = menu.findItem(R.id.action_kitap_raf_duzenle);
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


    public void tumKitaplarCek(){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/tum_kitaplar_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenCekilenKitaplar = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kitaplar = jsonObject.getJSONArray("kitaplar_table");
                    for (int i=0;i<kitaplar.length();i++){
                        JSONObject j = kitaplar.getJSONObject(i);

                        Integer kitapID = j.getInt("kitap_id");
                        String kitapAD = j.getString("kitap_ad");
                        String kitapBolge = j.getString("kitap_bolge");
                        String kitapRaf = j.getString("kitap_raf");
                        String yazarAD = j.getString("yazar_ad");
                        String kategoriAD = j.getString("kategori_ad");

                        Kitaplar kitap = new Kitaplar();
                        kitap.setKitap_id(kitapID);
                        kitap.setKitap_ad(kitapAD);
                        kitap.setBolge(kitapBolge);
                        kitap.setRaf(kitapRaf);
                        kitap.setYazar_ad(yazarAD);
                        kitap.setKategori_ad(kategoriAD);

                        vtDenCekilenKitaplar.add(kitap);
                    }

                    adapter = new EditBookAdapter(getActivity(),vtDenCekilenKitaplar);
                    rvKitapRafDuzenle.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                    rvKitapRafDuzenle.setAdapter(adapter);

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
