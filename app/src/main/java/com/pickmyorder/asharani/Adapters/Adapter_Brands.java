package com.pickmyorder.asharani.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.Brand;
import com.pickmyorder.asharani.Models.CityList;
import com.pickmyorder.asharani.Models.Wholselear;
import com.pickmyorder.asharani.R;
import com.pickmyorder.asharani.Storage.databaseSqlite;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Adapter_Brands extends RecyclerView.Adapter<Adapter_Brands.Viewholder> {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Context context;
    List<Brand> my_list;
    List<Brand> my_list_whole;
    com.pickmyorder.asharani.Storage.databaseSqlite databaseSqlite;

    public void filterList(ArrayList<Brand> filterdNames) {
        this.my_list = filterdNames;
        notifyDataSetChanged();
    }

    public Adapter_Brands(List<Brand> my_list, Context context) {

        this.context=context;
        this.my_list=my_list;
        this.my_list_whole=my_list;

        databaseSqlite=new databaseSqlite(context);
    }

    @NonNull
    @Override
    public Adapter_Brands.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowlayout_home_categories, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Brands.Viewholder viewholder, int i) {

        if (!my_list.get(i).getBussiness_logo().equals("")){

            Glide.with(context).load(my_list.get(i).getBussiness_logo()).into(viewholder.brand_image);

        }
        viewholder.brand_name.setText(my_list.get(i).getBusinessName());

        viewholder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("search","3");
                Paper.book().write("search_id",my_list.get(i).getId());
                Paper.book().write("menu_Brand","1");
                ((Home)context).getBrandsCategory(my_list.get(i).getId());

            }
        });

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView brand_image;
        TextView brand_name;
        CardView cardView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            brand_image=itemView.findViewById(R.id.imgview);
            brand_name=itemView.findViewById(R.id.txt_product_name);
            cardView=itemView.findViewById(R.id.cardview);
        }
    }
}

