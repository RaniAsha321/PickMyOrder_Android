package com.pickmyorder.asharani;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.paperdb.Paper;

public class Quote_Adapter extends RecyclerView.Adapter<Quote_Adapter.ViewHolder> {

    Context mcontext;
    List<QuotesDatum>datumList;

    public Quote_Adapter(Context context, List<QuotesDatum> myorderlist) {

        this.mcontext=context;
        this.datumList=myorderlist;
    }

    public Quote_Adapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.demopart, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {



        if(datumList.get(i).getOrderStatus().equals("1")){

            viewHolder.layout_status.setVisibility(View.VISIBLE);

        }

        else if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                viewHolder.layout_status.setVisibility(View.GONE);

        }

        else {

            viewHolder.layout_status.setVisibility(View.GONE);

        }

            viewHolder.product_ref.setText(datumList.get(i).getOrderDescrption());

            viewHolder.ref_no.setText(datumList.get(i).getOrderId());
            viewHolder.project.setText(datumList.get(i).getProject());
            viewHolder.date.setText(datumList.get(i).getDate());

        if(Paper.book().read("permission_see_cost","2").equals("1")){
            viewHolder.Inc_Vat.setText(datumList.get(i).getTotalIncVat());
            viewHolder.Ex_Vat.setText(datumList.get(i).getTotalExVat());

        }
        else {

            viewHolder.Inc_Vat.setText("0.00");
            viewHolder.Ex_Vat.setText("0.00");

        }
            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                viewHolder.Inc_Vat.setText(datumList.get(i).getTotalIncVat());
                viewHolder.Ex_Vat.setText(datumList.get(i).getTotalExVat());
            }


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction;
                fragmentTransaction = ((AppCompatActivity) mcontext).getSupportFragmentManager().beginTransaction().addToBackStack("quote").replace(R.id.containerr, new My_Quote_Screen());
                fragmentTransaction.commit();

                Paper.book().write("PO_Number",datumList.get(i).getOrderId());
                Paper.book().write("newReference",datumList.get(i).getPoReffrence());
                Paper.book().write("tempdate",datumList.get(i).getDate());

            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView product_ref,ref_no, date,Ex_Vat, Inc_Vat,project;
        ImageView arrow_click;
        Context context;
        LinearLayout layout_status,linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView=itemView.findViewById(R.id.cardview_order_menu);
            product_ref=itemView.findViewById(R.id.product_ref);
            ref_no=itemView.findViewById(R.id.ref_no);
            date=itemView.findViewById(R.id.date);
            Ex_Vat=itemView.findViewById(R.id.Ex_Val);
            Inc_Vat=itemView.findViewById(R.id.Inc_Vat);
            arrow_click=itemView.findViewById(R.id.arrow_click);
            project=itemView.findViewById(R.id.project_testing);
            linearLayout=itemView.findViewById(R.id.linear);
            layout_status=itemView.findViewById(R.id.layout_status);
            context=itemView.getContext();

        }
    }
}
