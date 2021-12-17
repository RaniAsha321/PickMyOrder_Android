package com.pickmyorder.asharani;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterStockHistory extends RecyclerView.Adapter<AdapterStockHistory.ViewHolder> {

    Context context;
    List<VanReorder> vanReorders;

    public AdapterStockHistory(Context context, List<VanReorder> vanReorders) {

        this.context=context;
        this.vanReorders=vanReorders;
    }

    @NonNull
    @Override
    public AdapterStockHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_stock_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterStockHistory.ViewHolder holder, int position) {

        holder.txtvw_product_history.setText(vanReorders.get(position).getProductname());
        holder.txtvw_qty_history.setText(vanReorders.get(position).getQty());
        holder.txtvw_date_history.setText(vanReorders.get(position).getDateTime());

    }

    @Override
    public int getItemCount() {
        return vanReorders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtvw_product_history,txtvw_qty_history,txtvw_date_history;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtvw_product_history = itemView.findViewById(R.id.txtvw_product_history);
            txtvw_qty_history = itemView.findViewById(R.id.txtvw_qty_history);
            txtvw_date_history = itemView.findViewById(R.id.txtvw_date_history);

        }
    }
}
