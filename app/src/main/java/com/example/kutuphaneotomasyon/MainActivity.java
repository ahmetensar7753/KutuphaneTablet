package com.example.kutuphaneotomasyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    private Button buttonSistemeGiris,buttonKitapAra,buttonKategoriAra,buttonYazarAra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSistemeGiris = findViewById(R.id.buttonSistemeGiris);
        buttonYazarAra = findViewById(R.id.buttonYazarAra);
        buttonKategoriAra = findViewById(R.id.buttonKategoriAra);
        buttonKitapAra = findViewById(R.id.buttonKitapAra);

        buttonKitapAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,KitapAraActivity.class));
            }
        });

        buttonKategoriAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,KategoriAraActivity.class));
            }
        });

        buttonYazarAra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,YazarAraActivity.class));
            }
        });

        buttonSistemeGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GirisActivity.class));
            }
        });

    }
}