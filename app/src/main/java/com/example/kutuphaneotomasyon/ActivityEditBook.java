package com.example.kutuphaneotomasyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.classes.Categorys;
import com.example.kutuphaneotomasyon.classes.Books;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityEditBook extends AppCompatActivity {

    private TextView textViewKitapDuzenleID;
    private EditText editTextKitapDuzenleKitapAd,editTextKitapDuzenleYazarAd,editTextKitapDuzenleKitapResimAd,editTextKitapDuzenleBolum,editTextKitapDuzenleRaf;
    private Button buttonKitapRafDuzenleGuncelle;
    private Spinner spinnerKitapDuzenleKategori;

    private ArrayList<Categorys> vtDenCekilenKategoriler;
    private ArrayList<String> kategoriAdListesi;
    private ArrayAdapter<String> veriAdapter;

    private Books kitap;
    private Integer gelenKitapID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        textViewKitapDuzenleID = findViewById(R.id.textViewKitapDuzenleID);
        editTextKitapDuzenleKitapAd = findViewById(R.id.editTextKitapDuzenleKitapAd);
        editTextKitapDuzenleYazarAd = findViewById(R.id.editTextKitapDuzenleYazarAd);
        editTextKitapDuzenleKitapResimAd = findViewById(R.id.editTextKitapDuzenleKitapResimAd);
        editTextKitapDuzenleBolum = findViewById(R.id.editTextKitapDuzenleBolum);
        editTextKitapDuzenleRaf = findViewById(R.id.editTextKitapDuzenleRaf);
        buttonKitapRafDuzenleGuncelle = findViewById(R.id.buttonKitapRafDuzenleGuncelle);
        spinnerKitapDuzenleKategori = findViewById(R.id.spinnerKitapDuzenleKategori);

        // ??nceki activity'den gelen kitapID burada bir de??i??kene al??n??yor ve ilgili textview'a set ediliyor.
        gelenKitapID = Integer.parseInt(getIntent().getStringExtra("kitapID"));
        textViewKitapDuzenleID.setText(String.valueOf(gelenKitapID));

        // gelen kitapID'ye g??re ilgili kitab??n bilgileri bu fonksiyonla vt'den ??ekiliyor ve ilgili yerlere set ediliyor.
        kitapCek(gelenKitapID);


        // d??zenlen butonuyla d??zenlenen kitap bilgileri burada al??n??yor ve g??ncelleme fonskiyonuna g??nderiliyor.
        buttonKitapRafDuzenleGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kitapAd = editTextKitapDuzenleKitapAd.getText().toString().trim();
                String resimAd = editTextKitapDuzenleKitapResimAd.getText().toString().trim();
                String bolge = editTextKitapDuzenleBolum.getText().toString().trim();
                String raf = editTextKitapDuzenleRaf.getText().toString().trim();
                int kategori_id = vtDenCekilenKategoriler.get(spinnerKitapDuzenleKategori.getSelectedItemPosition()).getKategori_id();
                String yazarAd = editTextKitapDuzenleYazarAd.getText().toString().trim();

                // g??ncel bilgiler bu fonskyionla vt'de g??ncelleniyor. ID bilgisi verilerek istenen kay??t d??zenleniyor.
                kitapGuncelle(gelenKitapID,kitapAd,resimAd,bolge,raf,kategori_id,yazarAd);

            }
        });


    }

    public void kitapCek(int id){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/kitap_cek.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                kitap = new Books();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray list = jsonObject.getJSONArray("kitaplar_table");

                    for (int i=0 ; i<list.length();i++){
                        JSONObject j = list.getJSONObject(i);

                        String kitapAd = j.getString("kitap_ad");
                        String yazarAd = j.getString("yazar_ad");
                        String kategoriAd = j.getString("kategori_ad");
                        String resimAd = j.getString("kitap_resim_ad");
                        Double kitapPuan = j.getDouble("kitap_puan");
                        String kitapBolge = j.getString("kitap_bolge");
                        String kitapRaf = j.getString("kitap_raf");
                        String ozet = j.getString("kitap_ozet");
                        Double toplamPuan = j.getDouble("toplam_puan");
                        Integer oySay = j.getInt("oy_say");

                        kitap.setKitap_ad(kitapAd);
                        kitap.setYazar_ad(yazarAd);
                        kitap.setKategori_ad(kategoriAd);
                        kitap.setResim_ad(resimAd);
                        kitap.setPuan(kitapPuan);
                        kitap.setBolge(kitapBolge);
                        kitap.setRaf(kitapRaf);
                        kitap.setOzet(ozet);
                        kitap.setToplam_puan(toplamPuan);
                        kitap.setOy_say(oySay);

                    }

                    editTextKitapDuzenleKitapAd.setText(kitap.getKitap_ad());
                    editTextKitapDuzenleYazarAd.setText(kitap.getYazar_ad());
                    editTextKitapDuzenleKitapResimAd.setText(kitap.getResim_ad());
                    editTextKitapDuzenleBolum.setText(kitap.getBolge());
                    editTextKitapDuzenleRaf.setText(kitap.getRaf());
                    // ilgili kitab??n kategorisinin d??zenlenebilmesi i??in bu fonskiyonda t??m kategoriler ??ekiliyor.
                    // Kitab??n mevcut kategorisi paremetre olarak verilmesinin sebebi spinner yap??s??nda ilk s??rada g??z??kmesi i??lemi yap??l??yor.
                    kategorilerCek(kitap.getKategori_ad());


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
                params.put("kitap_id",String.valueOf(id));
                return params;
            }
        };
        Volley.newRequestQueue(ActivityEditBook.this).add(istek);
    }

    public void kategorilerCek(String kitapKategori){
        String url = "https://kristalekmek.com/kutuphane/kategoriler/kategoriler_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Burada kitab??n mevcut kategorisi ilk olarak spinnerda kullan??lacak olan listeye at??l??yor (ilk s??rada g??z??kmesi i??in).
                vtDenCekilenKategoriler = new ArrayList<>();
                kategoriAdListesi = new ArrayList<>();
                Categorys kitab??nKategorisi = new Categorys();
                vtDenCekilenKategoriler.add(kitab??nKategorisi);
                kategoriAdListesi.add(kitapKategori);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kategoriler = jsonObject.getJSONArray("kategori_table");

                    for (int i=0;i<kategoriler.length();i++){
                        JSONObject j = kategoriler.getJSONObject(i);

                        Integer kategoriID = j.getInt("kategori_id");
                        String kategoriAD = j.getString("kategori_ad");

                        // Burada ise mevcut kategori liste i??inde tekrar etmemesi i??in if kontrol?? ile e??de??er kay??t yakalan??yor.
                        if (!kategoriAD.equals(kitapKategori)){
                            Categorys k = new Categorys(kategoriID,kategoriAD);

                            vtDenCekilenKategoriler.add(k);
                            kategoriAdListesi.add(k.getAd());
                        }else {
                            vtDenCekilenKategoriler.get(0).setKategori_id(kategoriID);
                            vtDenCekilenKategoriler.get(0).setAd(kitapKategori);
                        }
                    }
                    // tamamlanan liste adapter'a yollan??yor ve spinner'a set ediliyor.
                    veriAdapter = new ArrayAdapter<String>(ActivityEditBook.this, android.R.layout.simple_list_item_1,android.R.id.text1,kategoriAdListesi);
                    spinnerKitapDuzenleKategori.setAdapter(veriAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(ActivityEditBook.this).add(istek);
    }

    public void kitapGuncelle(int id,String kitapAd,String resimAd,String bolge,String raf,int kategori_id,String yazar_ad){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/update_kitap.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("updteKitapRsnpns",response);
                Toast.makeText(ActivityEditBook.this,"Kitap G??ncellendi.",Toast.LENGTH_SHORT).show();
                onBackPressed();
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
                params.put("kitap_id",String.valueOf(id));
                params.put("kitap_ad",kitapAd);
                params.put("kitap_resim_ad",resimAd);
                params.put("kitap_bolge",bolge);
                params.put("kitap_raf",raf);
                params.put("kategori_id",String.valueOf(kategori_id));
                params.put("yazar_ad",yazar_ad);
                return params;
            }
        };
        Volley.newRequestQueue(ActivityEditBook.this).add(istek);
    }

}