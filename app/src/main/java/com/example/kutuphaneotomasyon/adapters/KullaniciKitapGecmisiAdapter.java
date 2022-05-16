package com.example.kutuphaneotomasyon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.siniflar.AlinanKitaplar;



import java.util.ArrayList;


public class KullaniciKitapGecmisiAdapter extends RecyclerView.Adapter<KullaniciKitapGecmisiAdapter.CardNesneTutucuKitapGecmis>{

    private Context mContext;
    private ArrayList<AlinanKitaplar> disaridanGelenAlinanlar = new ArrayList<>();

    public KullaniciKitapGecmisiAdapter(Context mContext, ArrayList<AlinanKitaplar> disaridanGelenAlinanlar) {
        this.mContext = mContext;
        this.disaridanGelenAlinanlar = disaridanGelenAlinanlar;
    }

    public class CardNesneTutucuKitapGecmis extends RecyclerView.ViewHolder{
        TextView textViewCardKitapTeslimKayitID,textViewCardKitapTeslimKitapAd,textViewCardKitapTeslimYazarAd;
        TextView textViewCardKitapTeslimAlisTarihi,textViewCardKitapTeslimTeslimTarihi;
        ImageView imageViewCardKitapTeslimDurumu;
        public CardNesneTutucuKitapGecmis(@NonNull View itemView) {
            super(itemView);
            textViewCardKitapTeslimKayitID = itemView.findViewById(R.id.textViewCardKitapTeslimKayitID);
            textViewCardKitapTeslimKitapAd = itemView.findViewById(R.id.textViewCardKitapTeslimKitapAd);
            textViewCardKitapTeslimYazarAd = itemView.findViewById(R.id.textViewCardKitapTeslimYazarAd);
            textViewCardKitapTeslimAlisTarihi = itemView.findViewById(R.id.textViewCardKitapTeslimAlisTarihi);
            textViewCardKitapTeslimTeslimTarihi = itemView.findViewById(R.id.textViewCardKitapTeslimTeslimTarihi);
            imageViewCardKitapTeslimDurumu = itemView.findViewById(R.id.imageViewCardKitapTeslimDurumu);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucuKitapGecmis onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tasarim_kitap_teslim_al,parent,false);
        return new CardNesneTutucuKitapGecmis(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuKitapGecmis holder, int position) {
        AlinanKitaplar alinanKitap = disaridanGelenAlinanlar.get(position);

        holder.textViewCardKitapTeslimKayitID.setText(String.valueOf(alinanKitap.getKayit_id()));
        holder.textViewCardKitapTeslimKitapAd.setText(alinanKitap.getKitap_ad());
        holder.textViewCardKitapTeslimYazarAd.setText(alinanKitap.getKitap_yazar());
        holder.textViewCardKitapTeslimAlisTarihi.setText(alinanKitap.getAlis_tarih());
        holder.textViewCardKitapTeslimTeslimTarihi.setText(alinanKitap.getIstenen_teslim_tarih());

        if (alinanKitap.getTeslim_durumu() == 1){
            holder.imageViewCardKitapTeslimDurumu.setImageResource(R.drawable.check);
        }else {
            holder.imageViewCardKitapTeslimDurumu.setImageResource(R.drawable.wrong);
        }



    }

    @Override
    public int getItemCount() {
        return disaridanGelenAlinanlar.size();
    }

}
