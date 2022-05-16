package com.example.kutuphaneotomasyon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.adapters.TookBookAdapter;
import com.example.kutuphaneotomasyon.classes.TookBook;
import com.example.kutuphaneotomasyon.classes.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentDeliveryBook extends Fragment {

    private Integer kullaniciID = ActivityBookInteraction.kullaniciID;
    private RecyclerView rvKitapTeslim;
    public static TextView textViewKitapTeslimAlKitapAd,textViewKitapTeslimAlYazarAd,textViewKitapAlisTarihi,textViewKitapKayitID;
    public static Button buttonKitapTeslimAl;
    private TextView textViewTarih;

    public static Calendar beklenenTeslimTarihi;
    private Users kullanici = new Users();

    private ArrayList<TookBook> vtDenCekilenAlinanlar;
    private TookBookAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delivery_book,container,false);

        rvKitapTeslim = rootView.findViewById(R.id.rvKitapTeslim);
        textViewKitapTeslimAlKitapAd = rootView.findViewById(R.id.textViewKitapTeslimAlKitapAd);
        textViewKitapTeslimAlYazarAd = rootView.findViewById(R.id.textViewKitapTeslimAlYazarAd);
        textViewKitapAlisTarihi = rootView.findViewById(R.id.textViewKitapAlisTarihi);
        textViewKitapKayitID = rootView.findViewById(R.id.textViewKitapKayitID);
        buttonKitapTeslimAl = rootView.findViewById(R.id.buttonKitapTeslimAl);
        textViewTarih = rootView.findViewById(R.id.textViewTarih);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date tarih = new Date();
        textViewTarih.setText(sdf.format(tarih));

        kullaniciVerilerCek(kullaniciID);
        alinanlariCek(kullaniciID);

        buttonKitapTeslimAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kitapTeslim(Integer.parseInt(textViewKitapKayitID.getText().toString()),1);
                //kullanıcı table guncellemeleri
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();

                if (c.after(beklenenTeslimTarihi)){
                    //puan düşürülür
                    kullanici.setNotu((kullanici.getNotu() - 20.0));
                    if (c.get(Calendar.DATE) == beklenenTeslimTarihi.get(Calendar.DATE)){
                        kullanici.setNotu((kullanici.getNotu() + 30.0)); // bu kısım beklenen teslim tarihiyle bugün eşit olduğunda çalışıyor ve totalde +10 puan veriliyor.
                    }                                                       // c.equals(beklenenTarih) çalışmıyor. // tarihler eşit olduğunda nedense ilk baştaki if true geliyor.
                }else if (c.before(beklenenTeslimTarihi)){
                    //puan arttırılır
                    kullanici.setNotu((kullanici.getNotu() + 20.0));
                }
                kullanici.setOkudugu_kitap_say((kullanici.getOkudugu_kitap_say() + 1));

                notGuncelle(kullanici); //verileri guncellenen kullanici nesnesi guncelleme fonskiyonuna gönderiliyor.

                textViewKitapTeslimAlKitapAd.setVisibility(View.INVISIBLE);
                textViewKitapTeslimAlYazarAd.setVisibility(View.INVISIBLE);
                textViewKitapAlisTarihi.setVisibility(View.INVISIBLE);
                textViewKitapKayitID.setVisibility(View.INVISIBLE);
                buttonKitapTeslimAl.setVisibility(View.INVISIBLE);
                if (kullanici.getNotu() < 20){
                    deleteKullanici(kullaniciID);
                    Date date = new Date();
                    insertKaraListe(kullanici.getAd()+" "+kullanici.getSoyad(),String.valueOf(sdf.format(date)));
                    Toast.makeText(getActivity(),"Kullanıcı Kara Listeye Düştü.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    public void alinanlariCek(int kullanici_id){
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

                        TookBook alinanKitap = new TookBook(kayitId,kitapAd,yazar,alisTarih,teslimTarih,kullanici_id);
                        alinanKitap.setTeslim_durumu(Integer.parseInt(teslimDurumu));

                        vtDenCekilenAlinanlar.add(alinanKitap);
                    }

                    adapter = new TookBookAdapter(getActivity(),vtDenCekilenAlinanlar);
                    rvKitapTeslim.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                    rvKitapTeslim.setAdapter(adapter);


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
                params.put("kullanici_id",String.valueOf(kullanici_id));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }

    public void kitapTeslim(int kayit_id,int teslimDurumu){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/kitap_teslim.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("kitapTslmRspns",response);
                Toast.makeText(getActivity(),"Kitap Teslim Alındı.",Toast.LENGTH_SHORT).show();
                alinanlariCek(kullaniciID);
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
                params.put("kayit_id",String.valueOf(kayit_id));
                params.put("teslim_durumu",String.valueOf(teslimDurumu));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }

    public void kullaniciVerilerCek(int kullanici_id){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/kullanici_kitap_teslim_skorlar.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray list = jsonObject.getJSONArray("kullanici_table");
                    for (int i=0;i<list.length();i++){
                        JSONObject j = list.getJSONObject(i);
                        Integer okuduguKitapSay = j.getInt("okudugu_kitap_say");
                        Double kullaniciNotu = j.getDouble("kullanici_notu");
                        String ad = j.getString("kullanici_ad");
                        String soyad = j.getString("kullanici_soyad");

                        kullanici.setOkudugu_kitap_say(okuduguKitapSay);
                        kullanici.setNotu(kullaniciNotu);
                        kullanici.setKullanici_id(kullanici_id);
                        kullanici.setAd(ad);
                        kullanici.setSoyad(soyad);
                    }

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
                params.put("kullanici_id",String.valueOf(kullanici_id));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }

    public void notGuncelle(Users k){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/update_not.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("notGnclleRspns",response);
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
                params.put("kullanici_id",String.valueOf(k.getKullanici_id()));
                params.put("okudugu_kitap_say",String.valueOf(k.getOkudugu_kitap_say()));
                params.put("kullanici_notu",String.valueOf(k.getNotu()));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }
    public void deleteKullanici(int id){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/delete_kullanici.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("deleteKullnRspns",response);
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
        Volley.newRequestQueue(getActivity()).add(istek);
    }
    public void insertKaraListe(String ad,String tarih){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/insert_kara_liste.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("insrtKaraLsteRspns",response);
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
                params.put("kayit_ad_soyad",ad);
                params.put("tarih",tarih);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }

}
