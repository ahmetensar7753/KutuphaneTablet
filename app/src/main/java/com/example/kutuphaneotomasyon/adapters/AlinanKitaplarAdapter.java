package com.example.kutuphaneotomasyon.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kutuphaneotomasyon.FragmentKitapTeslim;
import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.siniflar.AlinanKitaplar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AlinanKitaplarAdapter extends RecyclerView.Adapter<AlinanKitaplarAdapter.CardNesneTutucuAlinanlar>{

    private Context mContext;
    private ArrayList<AlinanKitaplar> disaridanGelenAlinanlar = new ArrayList<>();

    public AlinanKitaplarAdapter(Context mContext, ArrayList<AlinanKitaplar> disaridanGelenAlinanlar) {
        this.mContext = mContext;
        this.disaridanGelenAlinanlar = disaridanGelenAlinanlar;
    }

    public class CardNesneTutucuAlinanlar extends RecyclerView.ViewHolder{
        CardView cardKitapTeslim;
        TextView textViewCardKitapTeslimKayitID,textViewCardKitapTeslimKitapAd,textViewCardKitapTeslimYazarAd;
        TextView textViewCardKitapTeslimAlisTarihi,textViewCardKitapTeslimTeslimTarihi;
        ImageView imageViewCardKitapTeslimDurumu;
        public CardNesneTutucuAlinanlar(@NonNull View itemView) {
            super(itemView);
            cardKitapTeslim = itemView.findViewById(R.id.cardKitapTeslim);
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
    public CardNesneTutucuAlinanlar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tasarim_kitap_teslim_al,parent,false);
        return new CardNesneTutucuAlinanlar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuAlinanlar holder, int position) {
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

        holder.cardKitapTeslim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BU KISIMDA STRİNG TÜRÜNDEKİ TARİHİ CALENDAR TYPE'A ÇEVİRİP FRAGMENTTAKİ STATİC CALENDAR'A ATIYORUM.
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    cal.setTime(sdf.parse(alinanKitap.getIstenen_teslim_tarih()));
                    FragmentKitapTeslim.beklenenTeslimTarihi = cal;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                FragmentKitapTeslim.textViewKitapTeslimAlKitapAd.setVisibility(View.VISIBLE);
                FragmentKitapTeslim.textViewKitapTeslimAlYazarAd.setVisibility(View.VISIBLE);
                FragmentKitapTeslim.textViewKitapAlisTarihi.setVisibility(View.VISIBLE);
                FragmentKitapTeslim.textViewKitapKayitID.setVisibility(View.VISIBLE);
                FragmentKitapTeslim.buttonKitapTeslimAl.setVisibility(View.VISIBLE);
                FragmentKitapTeslim.textViewKitapTeslimAlKitapAd.setText(alinanKitap.getKitap_ad());
                FragmentKitapTeslim.textViewKitapTeslimAlYazarAd.setText(alinanKitap.getKitap_yazar());
                FragmentKitapTeslim.textViewKitapAlisTarihi.setText(alinanKitap.getAlis_tarih());
                FragmentKitapTeslim.textViewKitapKayitID.setText(String.valueOf(alinanKitap.getKayit_id()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return disaridanGelenAlinanlar.size();
    }



}
