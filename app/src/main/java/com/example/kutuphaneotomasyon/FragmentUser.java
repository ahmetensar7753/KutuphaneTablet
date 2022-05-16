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

public class FragmentUser extends Fragment {

    private TabLayout tabLayoutKullaniciFragment;
    private ViewPager2 viewPager2KullaniciFragment;

    private ArrayList<Fragment> fragmentListesi = new ArrayList<>();
    private ArrayList<String> fragmentBaslikListesi = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment tasarımı bu classa bağlanıyor.
        View rootView = inflater.inflate(R.layout.fragment_user,container,false);

        //tasarım içerisindeki viewları bağlıyoruz.
        tabLayoutKullaniciFragment = rootView.findViewById(R.id.tabLayoutKullaniciFragment);
        viewPager2KullaniciFragment = rootView.findViewById(R.id.viewPager2KullaniciFragment);


        fragmentListesi.add(new FragmentShowUser());
        fragmentListesi.add(new FragmentAddUser());
        fragmentListesi.add(new FragmentBlackList());

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity());

        viewPager2KullaniciFragment.setAdapter(adapter);

        fragmentBaslikListesi.add("Kullanıcı Görüntüle");
        fragmentBaslikListesi.add("Kullanıcı Ekle");
        fragmentBaslikListesi.add("Kara Liste");

        new TabLayoutMediator(tabLayoutKullaniciFragment,viewPager2KullaniciFragment,
                (tab,position)->tab.setText(fragmentBaslikListesi.get(position))).attach();


        return rootView;
    }

    private class MyViewPagerAdapter extends FragmentStateAdapter{

        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentListesi.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentListesi.size();
        }
    }

}
