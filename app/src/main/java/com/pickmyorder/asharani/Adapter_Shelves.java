package com.pickmyorder.asharani;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Adapter_Shelves extends RecyclerView.Adapter<Adapter_Shelves.ViewHolder> {

    Context mcontext;
    List<Shelf> mlist;
    String pdf_url;
    LinearLayoutManager layoutManager;
    Adapter_new_promotions adapter_catalogues_new;
    List<String> my_list;



    int spanCount = 1;
    int spacing = 25;
    boolean includeEdge = true;

    public Adapter_Shelves(Context context,List<Shelf> mlist) {

        this.mcontext=context;
        this.mlist=mlist;
    }


    @NonNull
    @Override
    public Adapter_Shelves.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowlayout_shelves, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Shelves.ViewHolder viewHolder, int i) {

        viewHolder.txt_shelves_head.setText(mlist.get(i).getHeading());

        layoutManager = new LinearLayoutManager(mcontext);
        viewHolder.recyclerview_inner_shelves.setLayoutManager(layoutManager);

       viewHolder.txt_view_more.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Paper.book().write("Shelve_id",mlist.get(i).getId());
               Paper.book().write("Shelve_name",mlist.get(i).getHeading());
               FragmentTransaction fragmentTransaction;
               fragmentTransaction = ((AppCompatActivity) mcontext ).getSupportFragmentManager().beginTransaction().addToBackStack("catalogs_home").replace(R.id.containerr, new New_Catalogues());
               fragmentTransaction.commit();

           }
       });



        List<Shelvesdatum> catalogdatumList= new ArrayList<>();

        for (int j = 0; j < mlist.get(i).getShelvesdata().size(); j++) {


            Shelvesdatum datum1=new Shelvesdatum();
            datum1.setId(mlist.get(i).getShelvesdata().get(j).getId());
            datum1.setHeading(mlist.get(i).getShelvesdata().get(j).getHeading());
            datum1.setSectionImage(mlist.get(i).getShelvesdata().get(j).getSectionImage());
            datum1.setCetalouge(mlist.get(i).getShelvesdata().get(j).getCetalouge());

            catalogdatumList.add(datum1);
        }


        adapter_catalogues_new=new Adapter_new_promotions(mcontext,catalogdatumList);

        viewHolder.recyclerview_inner_shelves.setLayoutManager(new GridLayoutManager(mcontext, 1, GridLayoutManager.HORIZONTAL, false));

        viewHolder.recyclerview_inner_shelves.setAdapter(adapter_catalogues_new);

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_shelves_head,txt_view_more;
        CardView cardView;
        RecyclerView recyclerview_inner_shelves;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview_inner_shelves = itemView.findViewById(R.id.recyclerview_inner_shelves);
            txt_shelves_head = itemView.findViewById(R.id.txt_shelves_head);
            txt_view_more = itemView.findViewById(R.id.txt_view_more);

        }

    }
}


