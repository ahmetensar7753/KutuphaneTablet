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

import com.example.kutuphaneotomasyon.ActivityEditBook;
import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.classes.Kitaplar;

import java.util.ArrayList;

public class EditBookAdapter extends RecyclerView.Adapter<EditBookAdapter.CardNesneTutucuKitapDuzenle>{

    private Context mContext;
    private ArrayList<Kitaplar> disaridanGelenKitaplar = new ArrayList<>();
    private ArrayList<Kitaplar> fullList;

    public EditBookAdapter(Context mContext, ArrayList<Kitaplar> disaridanGelenKitaplar) {
        this.mContext = mContext;
        this.disaridanGelenKitaplar = disaridanGelenKitaplar;
        fullList = new ArrayList<>(disaridanGelenKitaplar);
    }

    public class CardNesneTutucuKitapDuzenle extends RecyclerView.ViewHolder{
        CardView cardViewKitapRafDuzenle;
        TextView textViewCardKitapDuzenleKitapAd,textViewCardKitapDuzenleYazarAd,textViewCardKitapDuzenleKategori,textViewCardKitapDuzenleBolum,textViewCardKitapDuzenleRaf;
        public CardNesneTutucuKitapDuzenle(@NonNull View itemView) {
            super(itemView);
            cardViewKitapRafDuzenle = itemView.findViewById(R.id.cardViewKitapRafDuzenle);
            textViewCardKitapDuzenleKitapAd = itemView.findViewById(R.id.textViewCardKitapDuzenleKitapAd);
            textViewCardKitapDuzenleYazarAd = itemView.findViewById(R.id.textViewCardKitapDuzenleYazarAd);
            textViewCardKitapDuzenleKategori = itemView.findViewById(R.id.textViewCardKitapDuzenleKategori);
            textViewCardKitapDuzenleBolum = itemView.findViewById(R.id.textViewCardKitapDuzenleBolum);
            textViewCardKitapDuzenleRaf = itemView.findViewById(R.id.textViewCardKitapDuzenleRaf);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucuKitapDuzenle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_edit_book,parent,false);
        return new CardNesneTutucuKitapDuzenle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuKitapDuzenle holder, int position) {
        Kitaplar kitap = disaridanGelenKitaplar.get(position);

        holder.textViewCardKitapDuzenleKitapAd.setText(kitap.getKitap_ad());
        holder.textViewCardKitapDuzenleYazarAd.setText(kitap.getYazar_ad());
        holder.textViewCardKitapDuzenleKategori.setText(kitap.getKategori_ad());
        holder.textViewCardKitapDuzenleBolum.setText(kitap.getBolge());
        holder.textViewCardKitapDuzenleRaf.setText(kitap.getRaf());
        holder.cardViewKitapRafDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityEditBook.class);
                intent.putExtra("kitapID",String.valueOf(kitap.getKitap_id()));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return disaridanGelenKitaplar.size();
    }

    public Filter getFilter(){return Searched_Filter;}

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Kitaplar> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Kitaplar k:fullList){
                    if (k.getKitap_ad().toLowerCase().contains(filterPattern)){
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
            disaridanGelenKitaplar.clear();
            disaridanGelenKitaplar.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };



}
