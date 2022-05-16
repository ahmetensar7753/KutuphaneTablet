package com.example.kutuphaneotomasyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PersonelActivity extends AppCompatActivity {

    private BottomNavigationView botNavViewPersonelActivity;


    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel);


        botNavViewPersonelActivity = findViewById(R.id.botNavViewPersonelActivity);



        getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutPersonelActivity,new FragmentKullanici()).commit();

        botNavViewPersonelActivity.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_kitap_islemleri:
                        tempFragment = new FragmentKitap();
                        break;
                    case R.id.action_kullanici_islemleri:
                        tempFragment = new FragmentKullanici();
                        break;
                    case R.id.action_personel_islemleri:
                        tempFragment = new FragmentPersonel();
                        break;
                    default:
                        tempFragment = new FragmentKullanici();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutPersonelActivity,tempFragment).commit();
                return true;
            }
        });

    }
}