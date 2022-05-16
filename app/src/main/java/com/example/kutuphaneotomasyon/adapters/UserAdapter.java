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

import com.example.kutuphaneotomasyon.ActivityUserDetail;
import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.classes.Users;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CardNesneTutucuKullanici>{

    private Context mContext;
    private ArrayList<Users> disaridanGelenKullanicilar = new ArrayList<>();
    private ArrayList<Users> fullList;

    public UserAdapter(Context mContext, ArrayList<Users> disaridanGelenKullanicilar) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_user,parent,false);
        return new CardNesneTutucuKullanici(view);
    }

    /* burada kullanıcılar cardView'lara yerleştiriliyor ve
    * cardView'a tıklandığında ilgili kullanıcıyla ilgili işlemlerin yapılabildiği activity'e ilgili kişinin bilgileri taşınarak geçiş sağlanıyor.*/

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuKullanici holder, int position) {
        Users kullanici = disaridanGelenKullanicilar.get(position);

        holder.textViewCardKullaniciAd.setText(kullanici.getAd()+" "+kullanici.getSoyad());
        holder.cardKullaniciAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityUserDetail.class);
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

    // Filter sınıfı ile kullanıcılar filitrelenebiliyor.Aratılan kullanıcıya göre RecyclerView içerisinde listeleniyor.

    public Filter getFilter(){return Searched_Filter;}

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Users> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Users k:fullList){
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
