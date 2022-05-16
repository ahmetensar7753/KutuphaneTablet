package com.example.kutuphaneotomasyon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kutuphaneotomasyon.KullaniciDetayActivity;
import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.siniflar.Kitaplar;
import com.example.kutuphaneotomasyon.siniflar.Kullanicilar;

import java.util.ArrayList;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.CardNesneTutucuKullanici>{

    private Context mContext;
    private ArrayList<Kullanicilar> disaridanGelenKullanicilar = new ArrayList<>();
    private ArrayList<Kullanicilar> fullList;

    public KullaniciAdapter(Context mContext, ArrayList<Kullanicilar> disaridanGelenKullanicilar) {
        this.mContext = mContext;
        this.disaridanGelenKullanicilar = disaridanGelenKullanicilar;
        fullList = new ArrayList<>(disaridanGelenKullanicilar);
    }

    public class CardNesneTutucuKullanici extends RecyclerView.ViewHolder{
        CardView cardKullaniciAd;
        TextView textViewCardKullaniciAd;
        public CardNesneTutucuKullanici(@NonNull View itemView) {
            super(itemView);
            cardKullaniciAd = itemView.findViewById(R.id.cardKullaniciAd);
            textViewCardKullaniciAd = itemView.findViewById(R.id.textViewCardKullaniciAd);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucuKullanici onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tasarim_kullanici,parent,false);
        return new CardNesneTutucuKullanici(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuKullanici holder, int position) {
        Kullanicilar kullanici = disaridanGelenKullanicilar.get(position);

        holder.textViewCardKullaniciAd.setText(kullanici.getAd()+" "+kullanici.getSoyad());
        holder.cardKullaniciAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KullaniciDetayActivity.class);
                intent.putExtra("kullanici_id",String.valueOf(kullanici.getKullanici_id()));
                intent.putExtra("kullanici_ad",kullanici.getAd()+" "+kullanici.getSoyad());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return disaridanGelenKullanicilar.size();
    }

    public Filter getFilter(){return Searched_Filter;}

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Kullanicilar> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Kullanicilar k:fullList){
                    if (k.getAd().toLowerCase().contains(filterPattern)){
                        filteredList.add(k);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            disaridanGelenKullanicilar.clear();
            disaridanGelenKullanicilar.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };



}
