 package com.pickmyorder.asharani.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Fragments.Super_Home;
import com.pickmyorder.asharani.Models.SuperSubSubCategory;
import com.pickmyorder.asharani.R;

import java.util.List;

import io.paperdb.Paper;

public class Adapter_Home_Sub_Sub_Sub_Category extends RecyclerView.Adapter<Adapter_Home_Sub_Sub_Sub_Category.ViewHolder> {

    Bundle bundle;
    Context mContext;
    List<SuperSubSubCategory> modellist_home;
    private FirebaseAnalytics mFirebaseAnalytics;

    public Adapter_Home_Sub_Sub_Sub_Category(List<SuperSubSubCategory> list, Context context) {

        this.mContext=context;
        this.modellist_home=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_home_sub_sub_category, viewGroup, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.product_name.setText(modellist_home.get(i).getSuperSubSubCatName());
        Glide.with(mContext).load(modellist_home.get(i).getImage()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.product_image);

        Log.e("statusbypradeep",modellist_home.get(i).getStatus()+"");

        if (modellist_home.get(i).getStatus()!=2) {

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Bundle sub_sub_sub_category = new Bundle();
                    sub_sub_sub_category.putString("sub_sub_sub_category_id", modellist_home.get(i).getSuperSubSubCatId());
                    sub_sub_sub_category.putString("sub_sub_sub_category_name", modellist_home.get(i).getSuperSubSubCatName());
                    mFirebaseAnalytics.logEvent("Sub_Sub_Sub_Category", sub_sub_sub_category);

                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack("m").replace(R.id.containerr, new Super_Home());
                    fragmentTransaction.commit();

                    Paper.book().write("tempSuperSubCategory", modellist_home.get(i).getSuperSubSubCatId());
                    Paper.book().write("superstatus", modellist_home.get(i).getStatus());

                }
            });
        }
        else {

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
        return modellist_home.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView product_name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image=itemView.findViewById(R.id.imgview_sub_sub_category);
            product_name=itemView.findViewById(R.id.txt_product_name_sub_sub_category);
            cardView=itemView.findViewById(R.id.cardview_sub_sub_category);

        }
    }
}
