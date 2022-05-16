package com.example.kutuphaneotomasyon.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kutuphaneotomasyon.ActivityBookDetail;
import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.classes.Books;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.CardNesneTutucuKitaplar>{

    private Context mContext;
    private ArrayList<Books> disaridanGelenKitaplar = new ArrayList<>();
    private ArrayList<Books> fullList;

    public BooksAdapter(Context mContext, ArrayList<Books> disaridanGelenKitaplar) {
        this.mContext = mContext;
        this.disaridanGelenKitaplar = disaridanGelenKitaplar;
        fullList = new ArrayList<>(disaridanGelenKitaplar);
    }

    public class CardNesneTutucuKitaplar extends RecyclerView.ViewHolder{
        CardView cardViewKitap;
        ImageView imageViewCardKitapResim;
        TextView textViewCardKitapAdi,textViewCardTasarimYazarAdi;
        public CardNesneTutucuKitaplar(@NonNull View itemView) {
            super(itemView);
            cardViewKitap = itemView.findViewById(R.id.cardViewKitap);
            imageViewCardKitapResim = itemView.findViewById(R.id.imageViewCardKitapResim);
            textViewCardKitapAdi = itemView.findViewById(R.id.textViewCardKitapAdi);
            textViewCardTasarimYazarAdi = itemView.findViewById(R.id.textViewCardTasarimYazarAdi);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucuKitaplar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_book,parent,false);
        return new CardNesneTutucuKitaplar(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuKitaplar holder, int position) {
        Books kitap = disaridanGelenKitaplar.get(position);

        String url = "https://kristalekmek.com/kutuphane/kitaplar/kitap_resimler/"+kitap.getResim_ad()+".jpg";
        Picasso.get().load(url).into(holder.imageViewCardKitapResim);

        holder.textViewCardKitapAdi.setText(kitap.getKitap_ad());
        holder.textViewCardTasarimYazarAdi.setText(kitap.getYazar_ad());

        holder.cardViewKitap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ActivityBookDetail.class);
                intent.putExtra("kitap_id",String.valueOf(kitap.getKitap_id()));
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
            ArrayList<Books> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Books k:fullList){
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
