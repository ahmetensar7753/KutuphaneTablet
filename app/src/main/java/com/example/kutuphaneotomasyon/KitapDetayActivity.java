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
import com.example.kutuphaneotomasyon.siniflar.Kitaplar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KitapDetayActivity extends AppCompatActivity {

    private Button buttonKitapDetayKitabiPuanla;
    private TextView textViewKitapDetayKitapAd,textViewKitapDetayYazarAd,textViewKitapDetayKategorisi;
    private ImageView imageViewKitapDetayKitapResim;
    private RatingBar ratingBarKitapDetay;
    private TextView textViewKitapDetayBolge,textViewKitapDetayRaf;
    private TextView textViewKitapDetayKitapOzet;
    private ImageView imageViewKitapDetayAnaSayfaDon;

    private Integer kitapID;
    private Kitaplar kitap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_detay);

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

        kitapID = Integer.parseInt(getIntent().getStringExtra("kitap_id"));

        kitapCek(kitapID);


        buttonKitapDetayKitabiPuanla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getLayoutInflater().inflate(R.layout.alert_tasarim_kitap_puanla,null);
                RatingBar ratingBarKitapOyla = view.findViewById(R.id.ratingBarKitapOyla);
                AlertDialog.Builder bd = new AlertDialog.Builder(KitapDetayActivity.this);

                bd.setTitle("Kitabı Oyla");
                bd.setIcon(R.drawable.yarim_yildiz);
                bd.setView(view);

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
                startActivity(new Intent(KitapDetayActivity.this,MainActivity.class));
                finish();
            }
        });

    }

    public void kitapCek(int kitap_id){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/kitap_cek.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                kitap = new Kitaplar();
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
        Volley.newRequestQueue(KitapDetayActivity.this).add(istek);
    }

    public void puanVer(int id,float topPuan,int oySay,float ortPuan){
        String url = "https://kristalekmek.com/kutuphane/kitaplar/update_puan.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("puanVerRspns",response);
                Toast.makeText(KitapDetayActivity.this,"Oylama Yapıldı.Teşekkürler!",Toast.LENGTH_SHORT).show();
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
        Volley.newRequestQueue(KitapDetayActivity.this).add(istek);
    }

}