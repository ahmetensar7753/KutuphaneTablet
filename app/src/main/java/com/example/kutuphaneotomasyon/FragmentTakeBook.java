package com.example.kutuphaneotomasyon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.classes.Kitaplar;
import com.example.kutuphaneotomasyon.classes.Kullanicilar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentTakeBook extends Fragment {

    private Spinner spinnerKitapAlisYazarAd,spinnerKitapAlisKitapAd;

    private ImageView imageViewKitapAlisResim;
    private TextView textViewKitapAlisKitapAdi,textViewKitapAlisYazarAdi,textViewKitapAlisKitapKategorisi;
    private TextView textViewKitapAlisKullaniciAdi,textViewKitapAlisMevcutPuan,textViewKitapAlisBugunTarihi,textViewKitapAlisIstenenTeslimTarihi;
    private Button buttonKitapAlisTeslimEt;

    private Integer kullaniciID = ActivityBookInteraction.kullaniciID;
    private Kullanicilar kullanici;
    private Calendar teslmTarihi;

    private ArrayList<String> yazarlarArrayList;
    private ArrayList<String> kitaplarSTRListesi;
    private ArrayList<Kitaplar> kitaplarArrayList;

    private ArrayAdapter<String> veriAdapter1;
    private ArrayAdapter<String> veriAdapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_take_book,container,false);

        spinnerKitapAlisYazarAd = rootView.findViewById(R.id.spinnerKitapAlisYazarAd);
        spinnerKitapAlisKitapAd = rootView.findViewById(R.id.spinnerKitapAlisKitapAd);
        imageViewKitapAlisResim = rootView.findViewById(R.id.imageViewKitapAlisResim);
        textViewKitapAlisKitapAdi = rootView.findViewById(R.id.textViewKitapAlisKitapAdi);
        textViewKitapAlisYazarAdi = rootView.findViewById(R.id.textViewKitapAlisYazarAdi);
        textViewKitapAlisKitapKategorisi = rootView.findViewById(R.id.textViewKitapAlisKitapKategorisi);
        textViewKitapAlisKullaniciAdi = rootView.findViewById(R.id.textViewKitapAlisKullaniciAdi);
        textViewKitapAlisMevcutPuan = rootView.findViewById(R.id.textViewKitapAlisMevcutPuan);
        textViewKitapAlisBugunTarihi = rootView.findViewById(R.id.textViewKitapAlisBugunTarihi);
        textViewKitapAlisIstenenTeslimTarihi = rootView.findViewById(R.id.textViewKitapAlisIstenenTeslimTarihi);
        buttonKitapAlisTeslimEt = rootView.findViewById(R.id.buttonKitapAlisTeslimEt);

        yazarlarCek();
        kullaniciCek(kullaniciID);

        buttonKitapAlisTeslimEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder bd = new AlertDialog.Builder(getActivity());
                bd.setTitle("İşlem Tamamlansın Mı ?");
                bd.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date tarih = new Date();
                        String kitapAd = textViewKitapAlisKitapAdi.getText().toString();
                        String yazarAd = textViewKitapAlisYazarAdi.getText().toString();
                        String teslimTarihi = String.valueOf(sdf.format(teslmTarihi.getTime()));

                        insertKitapAlis(kitapAd,yazarAd,sdf.format(tarih),teslimTarihi,kullaniciID);
                        getActivity().onBackPressed();
                    }
                });
                bd.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //boş
                    }
                });
                bd.create().show();
            }
        });


        spinnerKitapAlisYazarAd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yazarKitaplarCek(yazarlarArrayList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerKitapAlisKitapAd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Kitaplar k = kitaplarArrayList.get(position);
                textViewKitapAlisKitapAdi.setText(k.getKitap_ad());
                textViewKitapAlisYazarAdi.setText(k.getYazar_ad());
                textViewKitapAlisKitapKategorisi.setText(k.getKategori_ad());
                String url = "https://kristalekmek.com/kutuphane/kitaplar/kitap_resimler/"+k.getResim_ad()+".jpg";
                Picasso.get().load(url).into(imageViewKitapAlisResim);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return rootView;
    }

    public void yazarlarCek(){
        String url = "https://kristalekmek.com/kutuphane/yazarlar/yazarlar_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                yazarlarArrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray yazarlar = jsonObject.getJSONArray("yazar_table");

                    for (int i=0;i<yazarlar.length();i++){
                        JSONObject j = yazarlar.getJSONObject(i);

                        String yazarAD = j.getString("yazar_ad");

                        yazarlarArrayList.add(yazarAD);
                    }

                    veriAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,yazarlarArrayList);
                    spinnerKitapAlisYazarAd.setAdapter(veriAdapter1);


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

    public void yazarKitaplarCek(String yazarAd){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/yazar_ada_kitap_cek.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                kitaplarArrayList = new ArrayList<>();
                kitaplarSTRListesi = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray list = jsonObject.getJSONArray("kitaplar_table");
                    for (int i=0;i<list.length();i++){
                        JSONObject j = list.getJSONObject(i);

                        Integer kitapID = j.getInt("kitap_id");
                        String kitapAD = j.getString("kitap_ad");
                        String resimAD = j.getString("kitap_resim_ad");
                        String yazarAD = j.getString("yazar_ad");
                        String kategoriAD = j.getString("kategori_ad");

                        Kitaplar kitap = new Kitaplar();
                        kitap.setKitap_id(kitapID);
                        kitap.setKitap_ad(kitapAD);
                        kitap.setYazar_ad(yazarAD);
                        kitap.setResim_ad(resimAD);
                        kitap.setKategori_ad(kategoriAD);

                        kitaplarArrayList.add(kitap);
                        kitaplarSTRListesi.add(kitap.getKitap_ad());
                    }

                    veriAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,kitaplarSTRListesi);
                    spinnerKitapAlisKitapAd.setAdapter(veriAdapter2);


                } catch (JSONException e) {
                    Log.e("exception",String.valueOf(e));
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
                params.put("yazar_ad",yazarAd);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }
    public void kullaniciCek(int id){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/kullanici_cek.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                kullanici = new Kullanicilar();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray list = jsonObject.getJSONArray("kullanici_table");
                    for (int i=0;i<list.length();i++){
                        JSONObject j = list.getJSONObject(i);

                        String ad = j.getString("kullanici_ad");
                        String soyad = j.getString("kullanici_soyad");
                        double notu = j.getDouble("kullanici_notu");

                        kullanici.setKullanici_id(id);
                        kullanici.setAd(ad);
                        kullanici.setSoyad(soyad);
                        kullanici.setNotu(notu);

                    }

                    textViewKitapAlisKullaniciAdi.setText(kullanici.getAd()+" "+kullanici.getSoyad());
                    textViewKitapAlisMevcutPuan.setText(String.valueOf(kullanici.getNotu()));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    textViewKitapAlisBugunTarihi.setText(sdf.format(c.getTime()));


                    if (kullanici.getNotu() < 50){
                        c.add(Calendar.DATE,7);
                        textViewKitapAlisIstenenTeslimTarihi.setText(sdf.format(c.getTime()));
                        teslmTarihi = c;
                    }else if (kullanici.getNotu() <= 100){
                        c.add(Calendar.DATE,15);
                        textViewKitapAlisIstenenTeslimTarihi.setText(sdf.format(c.getTime()));
                        teslmTarihi = c;
                    }else {
                        c.add(Calendar.DATE,20);
                        textViewKitapAlisIstenenTeslimTarihi.setText(sdf.format(c.getTime()));
                        teslmTarihi = c;
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
                params.put("kullanici_id",String.valueOf(id));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }

    public void insertKitapAlis(String kitapAd,String yazar,String alisTarih,String teslimTarih,int kullanici_id){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/insert_alinan_kitap.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("insrtKtpAlisRspns",response);
                Toast.makeText(getActivity(),"İşlem tamamlandı.",Toast.LENGTH_SHORT).show();
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
                params.put("kitap_ad",kitapAd);
                params.put("kitap_yazar",yazar);
                params.put("alis_tarih",alisTarih);
                params.put("teslim_tarih",teslimTarih);
                params.put("kullanici_id",String.valueOf(kullanici_id));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }

}
