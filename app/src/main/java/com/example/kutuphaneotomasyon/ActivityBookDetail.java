package com.example.kutuphaneotomasyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.classes.Books;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityBookDetail extends AppCompatActivity {

    private Button buttonKitapDetayKitabiPuanla;
    private TextView textViewKitapDetayKitapAd,textViewKitapDetayYazarAd,textViewKitapDetayKategorisi;
    private ImageView imageViewKitapDetayKitapResim;
    private RatingBar ratingBarKitapDetay;
    private TextView textViewKitapDetayBolge,textViewKitapDetayRaf;
    private TextView textViewKitapDetayKitapOzet;
    private ImageView imageViewKitapDetayAnaSayfaDon;

    private Integer kitapID;
    private Books kitap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        buttonKitapDetayKitabiPuanla = findViewById(R.id.buttonKitapDetayKitabiPuanla);
        textViewKitapDetayKitapAd = findViewById(R.id.textViewKitapDetayKitapAd);
        textViewKitapDetayYazarAd = findViewById(R.id.textViewKitapDetayYazarAd);
        textViewKitapDetayKategorisi = findViewById(R.id.textViewKitapDetayKategorisi);
        imageViewKitapDetayKitapResim = findViewById(R.id.imageViewKitapDetayKitapResim);
        ratingBarKitapDetay = findViewById(R.id.ratingBarKitapDetay);
        textViewKitapDetayBolge = findViewById(R.id.textViewKitapDetayBolge);
        textViewKitapDetayRaf = findViewById(R.id.textViewKitapDetayRaf);
        textViewKitapDetayKitapOzet = findViewById(R.id.textViewKitapDetayKitapOzet);
        imageViewKitapDetayAnaSayfaDon = findViewById(R.id.imageViewKitapDetayAnaSayfaDon);


        // ??nceki activity'den gelen kitapID, bu activity y??klenirken burada ??ekiliyor.
        kitapID = Integer.parseInt(getIntent().getStringExtra("kitap_id"));
        //burada ise id'ye g??re kitapCek fonksiyonu ??a????r??l??yor. Vt'den id'ye g??re sorgu yap??l??yor.
        kitapCek(kitapID);

        // kitab?? puanla butonuna t??kland??ktan sonra alertView a????l??yor ve bu alert view i??erisinde rating bar ile puan verilebiliyor.
        buttonKitapDetayKitabiPuanla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getLayoutInflater().inflate(R.layout.alert_design_book_vote,null);
                RatingBar ratingBarKitapOyla = view.findViewById(R.id.ratingBarKitapOyla);
                AlertDialog.Builder bd = new AlertDialog.Builder(ActivityBookDetail.this);

                bd.setTitle("Kitab?? Oyla");
                bd.setIcon(R.drawable.half_star);
                bd.setView(view);

                /* puan verip oyla butonuyla beraber gelen kitab??n toplam puan
                *  hesaplamas??yla beraber puanVer fonskiyonuna parametre olarak veirlerek vt'ye kayd?? sa??lan??yor.*/
                bd.setPositiveButton("OYLA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        float topPuan = (float) kitap.getToplam_puan() + ratingBarKitapOyla.getRating() ;
                        int oySay = kitap.getOy_say()+1;
                        float ortPuan = topPuan / oySay;

                        puanVer(kitapID,topPuan,oySay,ortPuan);

                    }
                });
                bd.create().show();

            }
        });

        imageViewKitapDetayAnaSayfaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityBookDetail.this,MainActivity.class));
                finish();
            }
        });

    }

    /* bu fonksiyonda id'ye g??re ilgili kitap bilgileri ??ekiliyor.
    *  Gelen kitap bilgileriyle beraber nesne ??retiliyor ve ard??ndan gerekli k??s??mlara yazd??r??l??yor.*/
    public void kitapCek(int kitap_id){
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

                    textViewKitapDetayKitapAd.setText(kitap.getKitap_ad());
                    textViewKitapDetayYazarAd.setText(kitap.getYazar_ad());
                    textViewKitapDetayKategorisi.setText(kitap.getKategori_ad());
                    textViewKitapDetayBolge.setText(kitap.getBolge());
                    textViewKitapDetayRaf.setText(kitap.getRaf());
                    textViewKitapDetayKitapOzet.setText(kitap.getOzet());
                    ratingBarKitapDetay.setRating((float) kitap.getPuan());

                    String url = "https://kristalekmek.com/kutuphane/kitaplar/kitap_resimler/"+kitap.getResim_ad()+".jpg";
                    Picasso.get().load(url).into(imageViewKitapDetayKitapResim);


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
                params.put("kitap_id",String.valueOf(kitap_id));
                return params;
            }
        };
        Volley.newRequestQueue(ActivityBookDetail.this).add(istek);
    }

    public void puanVer(int id,float topPuan,int oySay,float ortPuan){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/update_puan.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("puanVerRspns",response);
                Toast.makeText(ActivityBookDetail.this,"Oylama Yap??ld??.Te??ekk??rler!",Toast.LENGTH_SHORT).show();
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
                params.put("toplam_puan",String.valueOf(topPuan));
                params.put("oy_say",String.valueOf(oySay));
                params.put("kitap_puan",String.valueOf(ortPuan));
                return params;
            }
        };
        Volley.newRequestQueue(ActivityBookDetail.this).add(istek);
    }

}