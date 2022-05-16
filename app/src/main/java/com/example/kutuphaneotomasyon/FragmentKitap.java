package com.example.kutuphaneotomasyon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class FragmentKitap extends Fragment {

    private TabLayout tabLayoutKitapFragment;
    private ViewPager2 viewPager2KitapFragment;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> fragmentBaslikList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kitap,container,false);

        tabLayoutKitapFragment = rootView.findViewById(R.id.tabLayoutKitapFragment);
        viewPager2KitapFragment = rootView.findViewById(R.id.viewPager2KitapFragment);

        fragmentList.add(new FragmentKitapEkle());
        fragmentList.add(new FragmentKitapRafDuzenle());

        MyViewPagerClassKitap adapter = new MyViewPagerClassKitap(getActivity());

        viewPager2KitapFragment.setAdapter(adapter);

        fragmentBaslikList.add("Kitap Ekle");
        fragmentBaslikList.add("Raf-Kitap Düzenle");

        new TabLayoutMediator(tabLayoutKitapFragment,viewPager2KitapFragment,
                (tab,position)->tab.setText(fragmentBaslikList.get(position))).attach();


        return rootView;
    }


    private class MyViewPagerClassKitap extends FragmentStateAdapter{

        public MyViewPagerClassKitap(@NonNull FragmentActivity fragmentActivity) {
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
