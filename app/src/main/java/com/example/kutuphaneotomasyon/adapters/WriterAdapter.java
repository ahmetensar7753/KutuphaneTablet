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

import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.ActivityWriterBooks;
import com.example.kutuphaneotomasyon.classes.Yazarlar;

import java.util.ArrayList;

public class WriterAdapter extends RecyclerView.Adapter<WriterAdapter.CardNesneTutucuYazar>{

    private Context mContext;
    private ArrayList<Yazarlar> disaridanGelenYazarlar = new ArrayList<>();
    private ArrayList<Yazarlar> fullList;

    public WriterAdapter(Context mContext, ArrayList<Yazarlar> disaridanGelenYazarlar) {
        this.mContext = mContext;
        this.disaridanGelenYazarlar = disaridanGelenYazarlar;
        fullList = new ArrayList<>(disaridanGelenYazarlar);
    }

    public class CardNesneTutucuYazar extends RecyclerView.ViewHolder{
        CardView cardViewYazar;
        TextView textViewCardYazarAdi;
        public CardNesneTutucuYazar(@NonNull View itemView) {
            super(itemView);
            cardViewYazar = itemView.findViewById(R.id.cardViewYazar);
            textViewCardYazarAdi = itemView.findViewById(R.id.textViewCardYazarAdi);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucuYazar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_writer_search,parent,false);
        return new CardNesneTutucuYazar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuYazar holder, int position) {
        Yazarlar yazar = disaridanGelenYazarlar.get(position);

        holder.textViewCardYazarAdi.setText(yazar.getYazar_ad());
        holder.cardViewYazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityWriterBooks.class);
                intent.putExtra("yazar_id",String.valueOf(yazar.getYazar_id()));
                intent.putExtra("yazar_ad",yazar.getYazar_ad());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return disaridanGelenYazarlar.size();
    }

    public Filter getFilter(){return Searched_Filter;}

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Yazarlar> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Yazarlar y:fullList){
                    if (y.getYazar_ad().toLowerCase().contains(filterPattern)){
                        filteredList.add(y);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            disaridanGelenYazarlar.clear();
            disaridanGelenYazarlar.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };



}
