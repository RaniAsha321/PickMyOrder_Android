package com.pickmyorder.asharani;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Adapter_Order_AddStock extends RecyclerView.Adapter<Adapter_Order_AddStock.ViewHolder> {

    List<SingleOrderDatum> datumList;
    Context context;
    Interface_AddStock interface_addStock;
    List<ModelStock> modelStockList = new ArrayList<>();
    List<ModelStock> modelStockLists = new ArrayList<>();
    My_Order_AddStock my_order_addStock;

    public Adapter_Order_AddStock(Context context, List<SingleOrderDatum> singleOrderList,My_Order_AddStock my_order_addStock) {

        this.datumList=singleOrderList;
        this.context=context;
        this.my_order_addStock=my_order_addStock;
    }

    public Adapter_Order_AddStock() {

        this.context=context;

    }

    public void setOnItemSelected(Context context) {

        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_order_addstock, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.qnty.setText(String.valueOf(datumList.get(i).getPendingItem()));
        viewHolder.product_name.setText(datumList.get(i).getProductName());
        Glide.with(context).load(datumList.get(i).getImage()).into(viewHolder.product_image);

        viewHolder.edtxt_add_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("text1",s+"");

            }
            @Override
            public void afterTextChanged(Editable s) {


                modelStockList = new ArrayList<>();


                modelStockLists = Paper.book().read("Stock_List");

                if (s.length() != 0) {

                if (modelStockLists != null && modelStockLists.size() != 0) {


                    Log.e("pidradeep78", modelStockLists.size()+"");

                    for (int j = 0; j < modelStockLists.size(); j++) {

                        if (modelStockLists.get(j).getPro_id() != null) {

                            if (!(datumList.get(i).getProductId().equals(modelStockLists.get(j).getPro_id()))) {

                                Log.e("pidradeep", "if");

                                my_order_addStock.getData(datumList.get(i).getProductId(), datumList.get(i).getQty(), String.valueOf(s), viewHolder.edtxt_add_qty, i, datumList.size(), modelStockList);

                            } else {

                                Log.e("pidradeep", "else");

                                modelStockLists.remove(j);
                                my_order_addStock.getData(datumList.get(i).getProductId(), datumList.get(i).getQty(), String.valueOf(s), viewHolder.edtxt_add_qty, i, datumList.size(), modelStockLists);

/*
                                ModelStock modelStock = new ModelStock();
                                modelStock.setPro_id(datumList.get(i).getProductId());
                                modelStock.setOrdered_qty(datumList.get(i).getQty());
                                modelStock.setReorder_qty(String.valueOf(s));

                                modelStockLists.add(modelStock);

                               // modelStockLists.get(j).setReorder_qty(String.valueOf(s));

                                Paper.book().write("Stock_List", modelStockLists);*/

                            }

                        }

                    }
                } else {
                    my_order_addStock.getData(datumList.get(i).getProductId(), datumList.get(i).getQty(), String.valueOf(s), viewHolder.edtxt_add_qty, i, datumList.size(), modelStockList);


                }

            }

                else {

                    Toast.makeText(context,"Please Enter quantity",Toast.LENGTH_SHORT).show();
                }


            }
        });

        if(!(datumList.get(i).getVariationOptionName().equals("0"))){

            viewHolder.variation.setText(datumList.get(i).getVariationOptionName());
            viewHolder.slash.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.variation.setVisibility(View.GONE);
            viewHolder.slash.setVisibility(View.GONE);
        }


        if(Paper.book().read("permission_see_cost","2").equals("1")){
            viewHolder.price.setText(String.format("%.2f", Double.valueOf(((Double.valueOf(datumList.get(i).getPrice()))*(Integer.parseInt(datumList.get(i).getQty()))))));
        }



        else {

            viewHolder.price.setText("0.00");

            if(Paper.book().read("datarole", "5").equals("4")){

                if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                    viewHolder.price.setText(String.format("%.2f", Double.valueOf(((Double.valueOf(datumList.get(i).getPrice()))*(Integer.parseInt(datumList.get(i).getQty()))))));

                }

            }

        }

        Log.e("keyer",datumList.get(i).getProductId()+"   "+datumList.get(i).getPendingItem()+"");





       // getstock("1", "10","2","check");



       // interface_addStock.onItemSelected(viewHolder.edtxt_add_qty);


    }

    private void getData(String productId, String qty, String reorder) {

        ModelStock modelStock = new ModelStock();
        String reference= Paper.book().read("newReference");


        modelStock.setPro_id(productId);
        modelStock.setOrdered_qty(qty);
        modelStock.setOrder_id(reference);
        modelStock.setReorder_qty(reorder);

        modelStockList.add(modelStock);

        Log.e("mango",modelStockList.size()+"");

        Paper.book().write("Stock_List",modelStockList);
    }



    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView product_name,qnty,price,variation,slash;
        ImageView product_image;
        public static EditText edtxt_add_qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            slash=itemView.findViewById(R.id.variation_slash);
            variation=itemView.findViewById(R.id.variation);
            product_name=itemView.findViewById(R.id.product);
            cardView=itemView.findViewById(R.id.cardview_order_menu);
            qnty=itemView.findViewById(R.id.qnty);
            price=itemView.findViewById(R.id.price_product);
            product_image=itemView.findViewById(R.id.image_product);
            edtxt_add_qty=itemView.findViewById(R.id.edtxt_add_qty);

        }
    }
}
