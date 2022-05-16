package com.example.kutuphaneotomasyon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class KitapAlisTeslimActivity extends AppCompatActivity {

    private TabLayout tabLayoutKitapTeslimAlis;
    private ViewPager2 viewPager2KitapTeslimAlis;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> fragmentBaslikList = new ArrayList<>();

    public static Integer kullaniciID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_alis_teslim);

        tabLayoutKitapTeslimAlis = findViewById(R.id.tabLayoutKitapAlisTeslim);
        viewPager2KitapTeslimAlis = findViewById(R.id.viewPager2KitapAlisTeslim);

        kullaniciID = Integer.parseInt(getIntent().getStringExtra("kullanici_id"));

        fragmentList.add(new FragmentKitapTeslim());
        fragmentList.add(new FragmentKitapAlis());

        MyViewPagerAdapterKitap adapter = new MyViewPagerAdapterKitap(this);

        viewPager2KitapTeslimAlis.setAdapter(adapter);

        fragmentBaslikList.add("Kitap Teslim");
        fragmentBaslikList.add("Kitap Alış");

        new TabLayoutMediator(tabLayoutKitapTeslimAlis,viewPager2KitapTeslimAlis,
                (tab,position)->tab.setText(fragmentBaslikList.get(position))).attach();


    }

    private class MyViewPagerAdapterKitap extends FragmentStateAdapter{

        public MyViewPagerAdapterKitap(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }



}