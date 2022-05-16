package com.example.kutuphaneotomasyon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.kutuphaneotomasyon.classes.Categorys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentAddBook extends Fragment {

    private EditText editTextKitapEkleKitapAd,editTextKitapEkleYazarAd,editTextKitapEkleBolum,editTextKitapEkleRaf,editTextKitapEkleKitapResimAd,editTextKitapEkleKitapOzeti;
    private Spinner spinnerKitapEkleKategori;
    private Button buttonKitapEkleEkle;

    private ArrayList<Categorys> vtDenCekilenKategoriler;
    private ArrayList<String> kategoriAdListesi;
    private ArrayAdapter<String> veriAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_book,container,false);

        //burası kalvye açıldığında çerçeveyi soft yapıyor bu sayede edit text üzeri klavyeden dolayı kapanmıyor.
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        editTextKitapEkleKitapAd = rootView.findViewById(R.id.editTextKitapEkleKitapAd);
        editTextKitapEkleYazarAd = rootView.findViewById(R.id.editTextKitapEkleYazarAd);
        editTextKitapEkleBolum = rootView.findViewById(R.id.editTextKitapEkleBolum);
        editTextKitapEkleRaf = rootView.findViewById(R.id.editTextKitapEkleRaf);
        editTextKitapEkleKitapResimAd = rootView.findViewById(R.id.editTextKitapEkleKitapResimAd);
        editTextKitapEkleKitapOzeti = rootView.findViewById(R.id.editTextKitapEkleKitapOzeti);
        spinnerKitapEkleKategori = rootView.findViewById(R.id.spinnerKitapEkleKategori);
        buttonKitapEkleEkle = rootView.findViewById(R.id.buttonKitapEkleEkle);

        kategorileriCek();

        buttonKitapEkleEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder bd = new AlertDialog.Builder(getActivity());
                bd.setTitle("Kitabı Eklemek İstediğinizden Emin Misiniz ?");
                bd.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!editTextKitapEkleKitapAd.getText().toString().trim().equals("") &&
                                !editTextKitapEkleYazarAd.getText().toString().trim().equals("") &&
                                !editTextKitapEkleBolum.getText().toString().trim().equals("") &&
                                !editTextKitapEkleRaf.getText().toString().trim().equals("") &&
                                !editTextKitapEkleKitapResimAd.getText().toString().trim().equals("") &&
                                !editTextKitapEkleKitapOzeti.getText().toString().trim().equals("")){

                            String kitapAd = editTextKitapEkleKitapAd.getText().toString().trim();
                            String yazarAd = editTextKitapEkleYazarAd.getText().toString().trim();
                            String bolum = editTextKitapEkleBolum.getText().toString().trim();
                            String raf = editTextKitapEkleRaf.getText().toString().trim();
                            String resimAd = editTextKitapEkleKitapResimAd.getText().toString().trim();
                            String ozet = editTextKitapEkleKitapOzeti.getText().toString().trim();
                            Integer kategoriID = vtDenCekilenKategoriler.get(spinnerKitapEkleKategori.getSelectedItemPosition()).getKategori_id();

                            insertKitap(kitapAd,yazarAd,bolum,raf,resimAd,ozet,kategoriID);

                        }else {
                            Toast.makeText(getActivity(),"Boş alan bırakma",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                bd.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });
                bd.create().show();
            }
        });



        return rootView;
    }
    public void kategorileriCek(){
        String url = "https://kristalekmek.com/kutuphane/kategoriler/kategoriler_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenCekilenKategoriler = new ArrayList<>();
                kategoriAdListesi = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kategoriler = jsonObject.getJSONArray("kategori_table");

                    for (int i=0;i<kategoriler.length();i++){
                        JSONObject j = kategoriler.getJSONObject(i);

                        Integer kategoriID = j.getInt("kategori_id");
                        String kategoriAD = j.getString("kategori_ad");

                        Categorys k = new Categorys(kategoriID,kategoriAD);

                        vtDenCekilenKategoriler.add(k);
                        kategoriAdListesi.add(k.getAd());
                    }
                    veriAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,kategoriAdListesi);
                    spinnerKitapEkleKategori.setAdapter(veriAdapter);


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

    public void insertKitap(String kitapAd,String yazarAd,String bolum,String raf,String resimAd,String ozet,Integer kategoriID){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/insert_kitap.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("insrtKtpRspns",response);
                Toast.makeText(getActivity(),"Kitap Eklendi.",Toast.LENGTH_SHORT).show();
                editTextKitapEkleKitapAd.setText("");
                editTextKitapEkleYazarAd.setText("");
                editTextKitapEkleBolum.setText("");
                editTextKitapEkleRaf.setText("");
                editTextKitapEkleKitapResimAd.setText("");
                editTextKitapEkleKitapOzeti.setText("");
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
                params.put("kitap_ozet",ozet);
                params.put("kitap_resim_ad",resimAd);
                params.put("kitap_bolge",bolum);
                params.put("kitap_raf",raf);
                params.put("yazar_ad",yazarAd);
                params.put("kategori_id",String.valueOf(kategoriID));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }


}
