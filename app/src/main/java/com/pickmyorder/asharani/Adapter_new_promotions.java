package com.pickmyorder.asharani;

import android.content.Context;
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

import java.util.List;

import io.paperdb.Paper;

public class Adapter_new_promotions extends RecyclerView.Adapter<Adapter_new_promotions.ViewHolder> {

    Context mcontext;
    List<Shelvesdatum> mlist;

    String catlist;


    public Adapter_new_promotions(Context context, List<Shelvesdatum> mlist) {

        this.mcontext=context;
        this.mlist=mlist;

    }

    @NonNull
    @Override
    public Adapter_new_promotions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_home_shelves, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_new_promotions.ViewHolder holder, int position) {

        holder.SectionName.setText(mlist.get(position).getHeading());
        Glide.with(mcontext).load(mlist.get(position).getSectionImage()).into(holder.CoverImage);

      //  Glide.with(mcontext).load("https://app.pickmyorder.co.uk/images/cetalogues/18529857541617281088").into(holder.CoverImage);

        Log.e("catlist_shelves",mlist.size()+"");

        Log.e("catlist",mlist.get(position).getCetalouge()+"");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  catlist = mlist.get(position).getCatalog();
                List<Cetalouge> myList = mlist.get(position).getCetalouge();

                if(myList.size() != 0){


                    Log.e("catlist55",myList+"");

                    Paper.book().write("CatImageList",myList);

                    FragmentTransaction fragmentTransaction;
                    fragmentTransaction = ((AppCompatActivity) mcontext).getSupportFragmentManager().beginTransaction().addToBackStack("catalogs").replace(R.id.containerr, new Catalogue_Promo_Images());
                    fragmentTransaction.commit();

                }

                else {

                    Toast.makeText(mcontext,"No Catalog found",Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView CoverImage;
        TextView SectionName;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView=itemView.findViewById(R.id.cardview);
            CoverImage=itemView.findViewById(R.id.imgview);
            SectionName=itemView.findViewById(R.id.txt_product_name);
        }
    }
}
