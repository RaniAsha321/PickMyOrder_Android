package com.pickmyorder.asharani;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import io.paperdb.Paper;

public class Adapter_Home_Sub_Category extends RecyclerView.Adapter<Adapter_Home_Sub_Category.ViewHolder> {

    Context mContext;
    List<SubCategory> modellist_home;
    private FirebaseAnalytics mFirebaseAnalytics;

    public Adapter_Home_Sub_Category(List<SubCategory> list, Context context) {

        this.mContext=context;
        this.modellist_home=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_home_sub_category, viewGroup, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.product_name.setText(modellist_home.get(i).getSubCatName());
        Glide.with(mContext).load(modellist_home.get(i).getCatImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.product_image);

        if(modellist_home.get(i).getStatus()!=2) {
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Bundle subcategory = new Bundle();
                    subcategory.putString("sub_category_id", modellist_home.get(i).getSubCatId());
                    subcategory.putString("sub_category_name", modellist_home.get(i).getSubCatName());
                    mFirebaseAnalytics.logEvent("Product", subcategory);

                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack("l").replace(R.id.containerr, new Home_Sub_Sub_Category());
                    fragmentTransaction.commit();

                    Paper.book().write("tempsubcategoryid", modellist_home.get(i).getSubCatId());
                    Paper.book().write("substatus",String.valueOf(modellist_home.get(i).getStatus()));
                }
            });

        }

        else{

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(mContext,"Category/Products Not Available",Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return modellist_home.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView product_name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image=itemView.findViewById(R.id.imgview_sub_category);
            product_name=itemView.findViewById(R.id.txt_product_name_sub_category);
            cardView=itemView.findViewById(R.id.cardview_sub_category);
        }
    }
}
