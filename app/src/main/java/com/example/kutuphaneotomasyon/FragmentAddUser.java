package com.example.kutuphaneotomasyon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.classes.Users;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentAddUser extends Fragment {

    private EditText editTextKullaniciEkleTC,editTextKullaniciEkleAd,editTextKullaniciEkleSoyad,editTextKullaniciEkleTelefon,editTextKullaniciEkleEmail;
    private Button buttonKullaniciEkleKaydet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_user,container,false);

        editTextKullaniciEkleTC = rootView.findViewById(R.id.editTextKullaniciEkleTC);
        editTextKullaniciEkleAd = rootView.findViewById(R.id.editTextKullaniciEkleAd);
        editTextKullaniciEkleSoyad = rootView.findViewById(R.id.editTextKullaniciEkleSoyad);
        editTextKullaniciEkleTelefon = rootView.findViewById(R.id.editTextKullaniciEkleTelefon);
        editTextKullaniciEkleEmail = rootView.findViewById(R.id.editTextKullaniciEkleEmail);
        buttonKullaniciEkleKaydet = rootView.findViewById(R.id.buttonKullaniciEkleKaydet);

        buttonKullaniciEkleKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextKullaniciEkleTC.getText().toString().trim().equals("") &&
                        !editTextKullaniciEkleAd.getText().toString().trim().equals("") &&
                        !editTextKullaniciEkleSoyad.getText().toString().trim().equals("") &&
                        !editTextKullaniciEkleTelefon.getText().toString().trim().equals("") &&
                        !editTextKullaniciEkleEmail.getText().toString().trim().equals("")){

                    //kullanicinin sifresi = tc şeklinde otomatik Kullanicilar class'ı içinde cons'ta atanıyor.
                    Users kullanici = new Users(editTextKullaniciEkleTC.getText().toString(),
                            editTextKullaniciEkleAd.getText().toString(),
                            editTextKullaniciEkleSoyad.getText().toString(),
                            editTextKullaniciEkleTelefon.getText().toString(),
                            editTextKullaniciEkleEmail.getText().toString());

                    SimpleDateFormat sekil = new SimpleDateFormat("yyyy-MM-dd");
                    Date tarih = new Date();

                    kullaniciEkle(kullanici.getTc(),kullanici.getAd(),kullanici.getSoyad(),kullanici.getTelefon(),kullanici.getMail(),kullanici.getSifre(),String.valueOf(sekil.format(tarih)));

                }else {
                    Toast.makeText(getActivity(),"Boş alan bırakma!",Toast.LENGTH_SHORT).show();
                }


            }
        });

        return rootView;
    }

    public void kullaniciEkle(String tc,String ad,String soyad,String tel,String mail,String sifre,String tarih){
        String url = "https://kristalekmek.com/kutuphane/kullanicilar/insert_kullanici.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("insrtKlnncRspns",response);
                Toast.makeText(getActivity(),"Kayıt Başarılı!",Toast.LENGTH_SHORT).show();
                editTextKullaniciEkleTC.setText("");
                editTextKullaniciEkleAd.setText("");
                editTextKullaniciEkleSoyad.setText("");
                editTextKullaniciEkleEmail.setText("");
                editTextKullaniciEkleTelefon.setText("");
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
                params.put("kullanici_tc",tc);
                params.put("kullanici_ad",ad);
                params.put("kullanici_soyad",soyad);
                params.put("kullanici_telefon",tel);
                params.put("kullanici_mail",mail);
                params.put("kullanici_sifre",sifre);
                params.put("kayit_tarihi",tarih);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(istek);
    }

}
