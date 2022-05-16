package com.example.kutuphaneotomasyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.classes.Kullanicilar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitySignIn extends AppCompatActivity {

    private Button buttonGiris;
    private EditText editTextKullaniciAdi,editTextParola;

    private ArrayList<Kullanicilar> vtDenCekilenKullanicilar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        buttonGiris = findViewById(R.id.buttonGiris);
        editTextKullaniciAdi = findViewById(R.id.editTextKullaniciAdi);
        editTextParola = findViewById(R.id.editTextParola);

        kullanicilariCek();

        //hata engellemek için olan kısım
        vtDenCekilenKullanicilar = new ArrayList<>();
        Kullanicilar bosKullanici = new Kullanicilar();
        bosKullanici.setMail("1sdfdsfsdf");
        bosKullanici.setSifre("1hfgewerwer");
        bosKullanici.setKullanici_id(931014);
        vtDenCekilenKullanicilar.add(bosKullanici);

        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextKullaniciAdi.getText().toString().trim().equals("") && !editTextParola.getText().toString().trim().equals("")){
                    String girilenMail = editTextKullaniciAdi.getText().toString();
                    String girilenSifre = editTextParola.getText().toString();

                    int index = 0;
                    Kullanicilar tempKullanici = new Kullanicilar();

                    for (Kullanicilar k : vtDenCekilenKullanicilar){
                        if (k.getMail().equals(girilenMail) && k.getSifre().equals(girilenSifre)){
                            index = vtDenCekilenKullanicilar.indexOf(k);
                            tempKullanici.setKullanici_id(k.getKullanici_id());
                            if (k.isYetki()){
                                startActivity(new Intent(ActivitySignIn.this, ActivityStaff.class));
                                editTextKullaniciAdi.setText("");
                                editTextParola.setText("");
                                break;
                            }else {
                                Intent intent = new Intent(ActivitySignIn.this, ActivityUser.class);
                                intent.putExtra("kullanici_id",String.valueOf(k.getKullanici_id()));
                                startActivity(intent);
                                break;
                            }
                        }
                    }

                    if (vtDenCekilenKullanicilar.get(index).getKullanici_id() != tempKullanici.getKullanici_id()){
                        Toast.makeText(ActivitySignIn.this,"Mail yada Şifre yanlış!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void kullanicilariCek(){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/kullanicilari_cek.php";
        StringRequest istek = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vtDenCekilenKullanicilar = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray kullanicilar = jsonObject.getJSONArray("kullanici_table");

                    for (int i=0;i<kullanicilar.length();i++){
                        JSONObject j = kullanicilar.getJSONObject(i);

                        Integer id = j.getInt("kullanici_id");
                        String mail = j.getString("kullanici_mail");
                        String sifre = j.getString("kullanici_sifre");
                        int gelenYetki = j.getInt("yetki");

                        boolean yetki;
                        if (gelenYetki == 1){
                            yetki = true;
                        }else {
                            yetki = false;
                        }

                        Kullanicilar k = new Kullanicilar();
                        k.setKullanici_id(id);
                        k.setMail(mail);
                        k.setSifre(sifre);
                        k.setYetki(yetki);


                        vtDenCekilenKullanicilar.add(k);
                    }

                } catch (JSONException e) {
                    Log.e("EXCEPTİON GİRİYOR!!!",String.valueOf(e));
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(ActivitySignIn.this).add(istek);
    }
}