package com.example.kutuphaneotomasyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityStaff extends AppCompatActivity {

    private BottomNavigationView botNavViewPersonelActivity;


    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);


        botNavViewPersonelActivity = findViewById(R.id.botNavViewPersonelActivity);



        getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutPersonelActivity,new FragmentUser()).commit();

        // botNavigationView nesnesindeki item seçimleri burada dinleniyor.
        botNavViewPersonelActivity.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // bottomNavigationView' içerisinde tıklanan item switch yapısı içerisinde alınıyor ve tıklanan iteme göre tempFragment'a ilgili fragment nesnesi atanıyor.
                switch (item.getItemId()){
                    case R.id.action_kitap_islemleri:
                        tempFragment = new FragmentBook();
                        break;
                    case R.id.action_kullanici_islemleri:
                        tempFragment = new FragmentUser();
                        break;
                    case R.id.action_personel_islemleri:
                        tempFragment = new FragmentStaff();
                        break;
                    default:
                        tempFragment = new FragmentUser();
                }
                // tempFragment bu şekilde dinamik oluyor ve fragmentlar arası geçiş işlemi burada başlatılıyor.
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutPersonelActivity,tempFragment).commit();
                return true;
            }
        });

    }
}