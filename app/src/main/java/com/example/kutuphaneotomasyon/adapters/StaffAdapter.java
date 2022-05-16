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

import com.example.kutuphaneotomasyon.ActivityEditStaff;
import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.classes.Users;

import java.util.ArrayList;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.CardNesneTutucuPersonel>{

    private Context mContext;
    private ArrayList<Users> disaridanGelenPersonelle = new ArrayList<>();

    public StaffAdapter(Context mContext, ArrayList<Users> disaridanGelenPersonelle) {
        this.mContext = mContext;
        this.disaridanGelenPersonelle = disaridanGelenPersonelle;
    }

    public class CardNesneTutucuPersonel extends RecyclerView.ViewHolder{
        CardView cardPersonel;
        TextView textViewCardPerAd,textViewCardPerTel,textViewCardPerGorev;
        public CardNesneTutucuPersonel(@NonNull View itemView) {
            super(itemView);
            cardPersonel = itemView.findViewById(R.id.cardPersonel);
            textViewCardPerAd = itemView.findViewById(R.id.textViewCardPerAd);
            textViewCardPerTel = itemView.findViewById(R.id.textViewCardPerTel);
            textViewCardPerGorev = itemView.findViewById(R.id.textViewCardPerGorev);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucuPersonel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_edit_staff,parent,false);
        return new CardNesneTutucuPersonel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuPersonel holder, int position) {
        Users personel = disaridanGelenPersonelle.get(position);

        holder.textViewCardPerAd.setText(personel.getAd()+" "+personel.getSoyad());
        holder.textViewCardPerTel.setText(personel.getTelefon());
        holder.textViewCardPerGorev.setText(personel.getGorev());

        holder.cardPersonel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityEditStaff.class);
                intent.putExtra("id",String.valueOf(personel.getKullanici_id()));
                intent.putExtra("ad",personel.getAd());
                intent.putExtra("soyad",personel.getSoyad());
                intent.putExtra("telefon",personel.getTelefon());
                intent.putExtra("gorev",personel.getGorev());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return disaridanGelenPersonelle.size();
    }



}
