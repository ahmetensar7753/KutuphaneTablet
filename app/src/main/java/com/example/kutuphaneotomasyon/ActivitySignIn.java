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
import com.example.kutuphaneotomasyon.classes.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitySignIn extends AppCompatActivity {

    private Button buttonGiris;
    private EditText editTextKullaniciAdi,editTextParola;

    private ArrayList<Users> vtDenCekilenKullanicilar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        buttonGiris = findViewById(R.id.buttonGiris);
        editTextKullaniciAdi = findViewById(R.id.editTextKullaniciAdi);
        editTextParola = findViewById(R.id.editTextParola);

        // activity yüklenir yüklenmez tüm kullanıcılar vt'den çekiliyor.
        kullanicilariCek();

        // Hatadan kaçış için listeye boş bir veri ekleniyor.
        vtDenCekilenKullanicilar = new ArrayList<>();
        Users bosKullanici = new Users();
        bosKullanici.setMail("1sdfdsfsdf");
        bosKullanici.setSifre("1hfgewerwer");
        bosKullanici.setKullanici_id(931014);
        vtDenCekilenKullanicilar.add(bosKullanici);

        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kullanıcı-parola alanında boş alan bırakılmaması kontrolü yapılıyor.
                if (!editTextKullaniciAdi.getText().toString().trim().equals("") && !editTextParola.getText().toString().trim().equals("")){
                    // editText'teki textler string ddeğişkenlere alınıyor.
                    String girilenMail = editTextKullaniciAdi.getText().toString();
                    String girilenSifre = editTextParola.getText().toString();

                    int index = 0;
                    Users tempKullanici = new Users();

                    // burada kullanıcının girdiği giriş bilgileri, veritabanındaki giriş bilgileri listesinde aratılıyor.
                    // döngü içerisinde eşleşen kayıt olduğunda break ile döngü kırılıyor.
                    for (Users k : vtDenCekilenKullanicilar){
                        // ilk if bloğunda girilen veriyle şu anki kullanıcı verilerinin eşitlik kontrolü yapılıyor.
                        // veriler eşleşmiyorsa bir sonraki döngü adımına geçiliyor.
                        // eşleşme var ise verinin indexi döngü dışında oluşturulan index değişkenine alınıyor.
                        // ilgili kullanıcının id'si ise şablon olarak oluşturulan kullanıcı nesnesine (tempKullanici) alınıyor. (sebebi for bloğundan sonraki if'te)
                        if (k.getMail().equals(girilenMail) && k.getSifre().equals(girilenSifre)){
                            index = vtDenCekilenKullanicilar.indexOf(k);
                            tempKullanici.setKullanici_id(k.getKullanici_id());

                            // Kullanıcıyla-Personeli birbirinden ayıran kısım burada belirleniyor.
                            // Giriş yapan kişinin yetki durumu var ise personel activity'ye geçişi sağlanıyor ve döngü kırılıyor.
                            if (k.isYetki()){
                                startActivity(new Intent(ActivitySignIn.this, ActivityStaff.class));
                                editTextKullaniciAdi.setText("");
                                editTextParola.setText("");
                                break;
                            // Giriş yapan kişinin yetkisi yok ise id'si ile beraber kullanıcı activity'e geçişi sağlanıyor ve döngü kırılıyor.
                            }else {
                                Intent intent = new Intent(ActivitySignIn.this, ActivityUser.class);
                                intent.putExtra("kullanici_id",String.valueOf(k.getKullanici_id()));
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                    // for bloğu içerisinde eğer kullanıcı giriş yapabilmişse bu blok çalışmıyor.
                    // giriş yapmayı deneyip yanlış bir veri girmiş ise her halükarda bu if bloğu çalışıyor ve kullanıcıya mesaj veriliyor.
                    // çünkü index default olarak 0 olarak belirlendi.Listenin 0'ıncı indexinde ise boş veri olan bir veri var.
                    //  index değeri ve tempKullanıcı id'si eğer doğru giriş yapılmışsa atanıyor ve aşağıdaki koşul sağlanmamış oluyor.
                    if (vtDenCekilenKullanicilar.get(index).getKullanici_id() != tempKullanici.getKullanici_id()){
                        Toast.makeText(ActivitySignIn.this,"Mail yada Şifre yanlış!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    // veritabanındaki tüm kullanıcılar çekilip vtDenCekilenKullanicilar arraylistine alınıyor.
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

                        // yetki değişkeni 1-0 'dan true-false olarak dönüştürülüp kullanılıyor.
                        boolean yetki;
                        if (gelenYetki == 1){
                            yetki = true;
                        }else {
                            yetki = false;
                        }

                        // gelen her veriyle şablon class'tan nesne üretilip listeye atılıyor.
                        Users k = new Users();
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