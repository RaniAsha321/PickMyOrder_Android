package com.pickmyorder.asharani.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pickmyorder.asharani.Models.Shelf;
import com.pickmyorder.asharani.R;

import java.util.List;

public class Adapter_Sub_Shelves extends RecyclerView.Adapter<Adapter_Sub_Shelves.ViewHolder> {

    Context mcontext;
    List<Shelf> mlist;
    String pdf_url;
    LinearLayoutManager layoutManager;

    public Adapter_Sub_Shelves(Context context, List<Shelf> mlist) {

        this.mcontext=context;
        this.mlist=mlist;
    }


    @NonNull
    @Override
    public Adapter_Sub_Shelves.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowlayout_shelves, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Sub_Shelves.ViewHolder viewHolder, int i) {

        viewHolder.txt_shelves_head.setText(mlist.get(i).getHeading());

        layoutManager = new LinearLayoutManager(mcontext);
        viewHolder.recyclerview_inner_shelves.setLayoutManager(layoutManager);



    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_shelves_head;
        CardView cardView;
        RecyclerView recyclerview_inner_shelves;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_inner_shelves = itemView.findViewById(R.id.recyclerview_inner_shelves);
            txt_shelves_head = itemView.findViewById(R.id.txt_shelves_head);


          /*  pdf_size=itemView.findViewById(R.id.txt_size);
            pdf_date=itemView.findViewById(R.id.txt_date);
            pdf_Image=itemView.findViewById(R.id.imgview_pdf);
            pdf_Name=itemView.findViewById(R.id.txt_catalogue);
            cardView=itemView.findViewById(R.id.catalogue_cardview);*/
        }

    }
}


