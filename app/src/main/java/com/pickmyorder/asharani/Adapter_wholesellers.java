package com.pickmyorder.asharani;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.paperdb.Paper;

public class Adapter_wholesellers extends RecyclerView.Adapter<Adapter_wholesellers.Viewholder> {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Context context;
    List<Wholselear> my_list;
    List<Wholselear> my_list_whole;
    com.pickmyorder.asharani.databaseSqlite databaseSqlite;


    public Adapter_wholesellers(List<Wholselear> my_list, Context context) {

        this.context=context;
        this.my_list=my_list;
        this.my_list_whole=my_list;

        databaseSqlite=new databaseSqlite(context);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowlayout_home_categories, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        Glide.with(context).load(my_list.get(i).getBussinessLogo()).into(viewholder.product_image);
        viewholder.wholseller.setText(my_list.get(i).getBusiness());

        viewholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().write("wholeseller_business_id",my_list.get(i).getBusinessId());
                Paper.book().write("wholeseller_bus_id",my_list.get(i).getBusinessId());
                Paper.book().write("stripe_publish_key",my_list.get(i).getStripePublishkey());
                Paper.book().write("search_id",my_list.get(i).getId());
                ((Home)context).getProducts(my_list.get(i).getId(),"0");

            }
        });

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView wholseller;
        CardView cardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            product_image=itemView.findViewById(R.id.imgview);
            wholseller=itemView.findViewById(R.id.txt_product_name);
            cardView=itemView.findViewById(R.id.cardview);
        }
    }
}
