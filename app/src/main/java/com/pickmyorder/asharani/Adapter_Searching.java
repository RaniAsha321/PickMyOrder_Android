package com.pickmyorder.asharani;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import io.paperdb.Paper;

public class Adapter_Searching extends RecyclerView.Adapter<Adapter_Searching.ViewHolder> {


    Context mcontext;
    List<SearchDatum> searchlist;
    String publishkey;
    private FirebaseAnalytics mFirebaseAnalytics;

    public Adapter_Searching(Context context, List<SearchDatum> mylist, String publishkey) {

        this.mcontext=context;
        this.searchlist=mylist;
        this.publishkey=publishkey;

    }

    @NonNull
    @Override
    public Adapter_Searching.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_searching, viewGroup, false);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mcontext);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if(Paper.book().read("permission_see_cost","2").equals("1")){
            viewHolder.txt_price.setText( searchlist.get(i).getExVat());
        }

        else {

            viewHolder.txt_price.setText("0.00");

        }

        if(searchlist.get(i).getVanStock().equals("1")){


            Log.e("search_out","if");

            Log.e("search_out_number",searchlist.get(i).getInStock()+"");

            if (searchlist.get(i).getInStock() != null && Integer.valueOf(searchlist.get(i).getInStock()) < 1 ){

                Log.e("search_out1","if if");
                viewHolder.layout_van_qnty.setVisibility(View.GONE);
                viewHolder.txtvw_search_out.setVisibility(View.VISIBLE);

            }
            else {
                Log.e("search_out2","if else");
                viewHolder.txtvw_search_out.setVisibility(View.GONE);
                viewHolder.layout_van_qnty.setVisibility(View.VISIBLE);
                viewHolder.product_available_qnty.setText(searchlist.get(i).getInStock());
            }

            Paper.book().write("in_stock",searchlist.get(i).getInStock());
        }

        else {

            Log.e("search_out3","else");
            viewHolder.layout_van_qnty.setVisibility(View.GONE);
            viewHolder.txtvw_search_out.setVisibility(View.GONE);

            Paper.book().write("in_stock","0");
        }



        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

            viewHolder.txt_price.setText( searchlist.get(i).getExVat());

        }


        viewHolder.txt_searching.setText(searchlist.get(i).getTitle());

        Glide.with(mcontext).load(searchlist.get(i).getProductsImages()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.imageView_searching);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle search = new Bundle();
                search.putString(FirebaseAnalytics.Param.ITEM_NAME, searchlist.get(i).getTitle());
                search.putString(FirebaseAnalytics.Param.ITEM_ID, searchlist.get(i).getId());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, search);

                FragmentTransaction fragmentTransaction ;
                fragmentTransaction = ((AppCompatActivity) mcontext).getSupportFragmentManager().beginTransaction().addToBackStack("mo").replace(R.id.containerr, new Product_Detail());
                fragmentTransaction.commit();

                Paper.book().write("getProductId",searchlist.get(i).getId());
                Paper.book().write("product_name",searchlist.get(i).getTitle());
                Paper.book().write("product_first_img",searchlist.get(i).getProductsImages());
                Paper.book().write("product_second_img",searchlist.get(i).getProductImageTwo());
                Paper.book().write("product_description",searchlist.get(i).getDescription());
                Paper.book().write("product_specification",searchlist.get(i).getSpecification());
                Paper.book().write("product_pdf_info",searchlist.get(i).getReviews());
                Paper.book().write("product_price",searchlist.get(i).getExVat());
                Paper.book().write("pro_inc_vat",searchlist.get(i).getIncVat());
                Paper.book().write("whichbusiness",searchlist.get(i).getBusinessId());
                Paper.book().write("pdf1",searchlist.get(i).getPdfManual());
                Paper.book().write("pdf2", searchlist.get(i).getPdfManual2());
                Paper.book().write("pdf3", searchlist.get(i).getPdfManual3());
                Paper.book().write("pdf4", searchlist.get(i).getPdfManual4());
                Paper.book().write("pdf5", searchlist.get(i).getPdfManual5());
                Paper.book().write("pdf6", searchlist.get(i).getPdfManual6());
                Paper.book().write("pdftitle1",searchlist.get(i).getPdfManual());
                Paper.book().write("pdftitle2",searchlist.get(i).getPdfManual2());
                Paper.book().write("pdftitle3",searchlist.get(i).getPdfManual3());
                Paper.book().write("pdftitle4",searchlist.get(i).getPdfManual4());
                Paper.book().write("pdftitle5",searchlist.get(i).getPdfManual5());
                Paper.book().write("pdftitle6",searchlist.get(i).getPdfManual6());
                Paper.book().write("pdf_name1",searchlist.get(i).getPdfName());
                Paper.book().write("pdf_name2",searchlist.get(i).getPdf2Name());
                Paper.book().write("pdf_name3",searchlist.get(i).getPdf3Name());
                Paper.book().write("pdf_name4",searchlist.get(i).getPdf4Name());
                Paper.book().write("pdf_name5",searchlist.get(i).getPdf5Name());
                Paper.book().write("pdf_name6",searchlist.get(i).getPdf6Name());
                Paper.book().write("publish_key",publishkey);
                Paper.book().write("in_stock",searchlist.get(i).getInStock());
                Paper.book().write("vanstock",searchlist.get(i).getVanStock());

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView_searching;
        TextView txt_searching,txt_price,product_available_qnty,txtvw_search_out;
        LinearLayout layout_van_qnty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView_searching=itemView.findViewById(R.id.imgview_searching);
            txt_searching=itemView.findViewById(R.id.txt_searching);
            txt_price=itemView.findViewById(R.id.product_price_searching);
            cardView=itemView.findViewById(R.id.cardview_searching);
            layout_van_qnty = itemView.findViewById(R.id.layout_van_qnty_searching);
            product_available_qnty=itemView.findViewById(R.id.product_available_qnty);
            txtvw_search_out=itemView.findViewById(R.id.txtvw_search_out);
        }
    }
}

