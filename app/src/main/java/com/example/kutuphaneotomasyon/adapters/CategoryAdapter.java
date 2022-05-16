package com.example.kutuphaneotomasyon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kutuphaneotomasyon.ActivityCategoryBooks;
import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.classes.Kategoriler;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CardNesneTutucuKategori>{

    private Context mContext;
    private ArrayList<Kategoriler> disaridanGelenKategoriler = new ArrayList<>();

    public CategoryAdapter(Context mContext, ArrayList<Kategoriler> disaridanGelenKategoriler) {
        this.mContext = mContext;
        this.disaridanGelenKategoriler = disaridanGelenKategoriler;
    }

    public class CardNesneTutucuKategori extends RecyclerView.ViewHolder{
        CardView cardViewKategori;
        TextView textViewCardKategoriAdi;
        public CardNesneTutucuKategori(@NonNull View itemView) {
            super(itemView);
            cardViewKategori = itemView.findViewById(R.id.cardViewKategori);
            textViewCardKategoriAdi = itemView.findViewById(R.id.textViewCardKategoriAdi);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucuKategori onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_category_search,parent,false);
        return new CardNesneTutucuKategori(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuKategori holder, int position) {
        Kategoriler kategori = disaridanGelenKategoriler.get(position);

        holder.textViewCardKategoriAdi.setText(kategori.getAd());
        holder.cardViewKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityCategoryBooks.class);
                intent.putExtra("kategori_id",String.valueOf(kategori.getKategori_id()));
                intent.putExtra("kategori_ad",String.valueOf(kategori.getAd()));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return disaridanGelenKategoriler.size();
    }



}
