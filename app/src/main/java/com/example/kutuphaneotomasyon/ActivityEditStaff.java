package com.example.kutuphaneotomasyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ActivityEditStaff extends AppCompatActivity {

    private EditText editTextPersonelDuzenleAd,editTextPersonelDuzenleSoyad,editTextPersonelDuzenleTel,editTextPersonelDuzenleGorev;
    private Button buttonPersonelDuzenleGuncelle;
    private Integer personelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);

        editTextPersonelDuzenleAd = findViewById(R.id.editTextPersonelDuzenleAd);
        editTextPersonelDuzenleSoyad = findViewById(R.id.editTextPersonelDuzenleSoyad);
        editTextPersonelDuzenleTel = findViewById(R.id.editTextPersonelDuzenleTel);
        editTextPersonelDuzenleGorev = findViewById(R.id.editTextPersonelDuzenleGorev);
        buttonPersonelDuzenleGuncelle = findViewById(R.id.buttonPersonelDuzenleGuncelle);

        editTextPersonelDuzenleAd.setText(getIntent().getStringExtra("ad"));
        editTextPersonelDuzenleSoyad.setText(getIntent().getStringExtra("soyad"));
        editTextPersonelDuzenleTel.setText(getIntent().getStringExtra("telefon"));
        editTextPersonelDuzenleGorev.setText(getIntent().getStringExtra("gorev"));
        personelID = Integer.parseInt(getIntent().getStringExtra("id"));

        buttonPersonelDuzenleGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ad = editTextPersonelDuzenleAd.getText().toString().trim();
                String soyad = editTextPersonelDuzenleSoyad.getText().toString().trim();
                String tel = editTextPersonelDuzenleTel.getText().toString().trim();
                String gorev = editTextPersonelDuzenleGorev.getText().toString().trim();

                perGuncelle(personelID,ad,soyad,tel,gorev);

            }
        });

    }
    public void perGuncelle(int id,String ad,String soyad,String tel,String gorev){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/update_personel.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("perGnclleRspns",response);
                Toast.makeText(ActivityEditStaff.this,"Bilgiler Güncellendi.",Toast.LENGTH_SHORT).show();
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
                params.put("kullanici_ad",ad);
                params.put("kullanici_soyad",soyad);
                params.put("kullanici_telefon",tel);
                params.put("gorev",gorev);
                return params;
            }
        };
        Volley.newRequestQueue(ActivityEditStaff.this).add(istek);
    }


}