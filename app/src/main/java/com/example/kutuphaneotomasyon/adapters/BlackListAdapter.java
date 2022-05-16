package com.example.kutuphaneotomasyon.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kutuphaneotomasyon.R;
import com.example.kutuphaneotomasyon.classes.BlackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.CardNesneTutucuKaraListe>{

    private Context mContext;
    private ArrayList<BlackList> disaridanGelenKaraListe = new ArrayList<>();

    public BlackListAdapter(Context mContext, ArrayList<BlackList> disaridanGelenKaraListe) {
        this.mContext = mContext;
        this.disaridanGelenKaraListe = disaridanGelenKaraListe;
    }

    public class CardNesneTutucuKaraListe extends RecyclerView.ViewHolder{
        TextView textViewCardKaraListeAd,textViewCardKaraListeTarih;
        Button buttonCardKaraListeCikar;
        public CardNesneTutucuKaraListe(@NonNull View itemView) {
            super(itemView);
            buttonCardKaraListeCikar = itemView.findViewById(R.id.buttonCardKaraListeCikar);
            textViewCardKaraListeAd = itemView.findViewById(R.id.textViewCardKaraListeAd);
            textViewCardKaraListeTarih = itemView.findViewById(R.id.textViewCardKaraListeTarih);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucuKaraListe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design_black_list,parent,false);
        return new CardNesneTutucuKaraListe(view);
    }

    // Cardlara gelen veriler yerleştiriliyor ve card üzerindeki buton sayesinde istenildiği durumda personel kullanıcıyı kara listeden çıkarabiliyor.
    // Kara listeden çıkarılan kullanıcının tekrar kayıt olması gerekiyor. Veritabanında bir trigger ile bu kayıt sağlanabilir.
    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucuKaraListe holder, int position) {
        BlackList karaListe = disaridanGelenKaraListe.get(position);

        holder.textViewCardKaraListeAd.setText(karaListe.getAd());
        holder.textViewCardKaraListeTarih.setText(karaListe.getTarih());
        holder.buttonCardKaraListeCikar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteKaraListe(karaListe.getKayit_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return disaridanGelenKaraListe.size();
    }

    //Parametre olarak verilen kullanıcının id'sine göre kara liste tablosundan ilgili kayıt siliniyor.
    public void deleteKaraListe(int id){
        String url = "https://kristalekmek.com/kutuphane/kara_liste/delete_kara_liste.php";
        StringRequest istek = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dlteKaraLstRspns",response);
                Toast.makeText(mContext,"Kara Listeden Çıkarıldı, Tekrar Kayıt Ediniz.",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("kayit_id",String.valueOf(id));
                return params;
            }
        };
        Volley.newRequestQueue(mContext).add(istek);
    }



}
