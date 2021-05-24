package com.pickmyorder.asharani;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.List;
import io.paperdb.Paper;

public class Adapter_Super_Category extends RecyclerView.Adapter<Adapter_Super_Category.ViewHolder> {

    Context mContext;
    List<Product> product;
    String publishkey;

    public Adapter_Super_Category(List<Product> products, Context mcontext, String publishkey) {

        this.product=products;
        this.mContext=mcontext;
        this.publishkey=publishkey;
    }
    @NonNull
    @Override
    public Adapter_Super_Category.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_super_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Super_Category.ViewHolder viewHolder, int i) {

        viewHolder.product_name.setText(product.get(i).getTitle());
        Glide.with(mContext).load(product.get(i).getImage()).into(viewHolder.product_image);

        String permission_see_cost=Paper.book().read("permission_see_cost");
       // String is_Van = Paper.book().read("vanstock");

        if(product.get(i).getVanStock().equals("1")){

            if (product.get(i).getIn_stock() != null && Integer.valueOf(product.get(i).getIn_stock()) < 1 ){

                viewHolder.txtvw_product_out.setVisibility(View.VISIBLE);
                viewHolder.layout_van_qnty.setVisibility(View.GONE);


            }
            else {

                viewHolder.txtvw_product_out.setVisibility(View.GONE);
                viewHolder.layout_van_qnty.setVisibility(View.VISIBLE);
                viewHolder.product_available_qnty.setText(product.get(i).getIn_stock());
            }


            Paper.book().write("in_stock",product.get(i).getIn_stock());
        }

        else {

            viewHolder.layout_van_qnty.setVisibility(View.GONE);
            viewHolder.txtvw_product_out.setVisibility(View.GONE);

            Paper.book().write("in_stock","0");
        }




        if(permission_see_cost.equals("1")){

            viewHolder.price.setText(product.get(i).getPrice());
        }

        else {

            viewHolder.price.setText("0.00");

        }

        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

            viewHolder.price.setText(product.get(i).getPrice());

        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction;
                fragmentTransaction=((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().addToBackStack("mo").replace(R.id.containerr,new Product_Detail());
                fragmentTransaction.commit();

                Paper.book().write("whichbusiness",product.get(i).getWhichBusiness());
                Paper.book().write("getProductId",product.get(i).getProductId());
                Paper.book().write("product_name",product.get(i).getTitle());
                Paper.book().write("product_first_img",product.get(i).getImage());
                Paper.book().write("product_second_img",product.get(i).getImageTwo());
                Paper.book().write("product_description",product.get(i).getDescription());
                Paper.book().write("product_specification",product.get(i).getSpecification());
                Paper.book().write("product_pdf_info",product.get(i).getReviews());
                Paper.book().write("product_price",product.get(i).getPrice());
                Paper.book().write("pro_inc_vat",product.get(i).getIncVat());
                Paper.book().write("pdf1",product.get(i).getPdf1());
                Paper.book().write("pdftitle1",product.get(i).getPdf1Title1());
                Paper.book().write("pdf2",product.get(i).getPdf2());
                Paper.book().write("pdftitle2",product.get(i).getPdf2Title2());
                Paper.book().write("pdf3",product.get(i).getPdf3());
                Paper.book().write("pdftitle3",product.get(i).getPdf3Title3());
                Paper.book().write("pdf4",product.get(i).getPdf4());
                Paper.book().write("pdftitle4",product.get(i).getPdf4Title4());
                Paper.book().write("pdf5",product.get(i).getPdf5());
                Paper.book().write("pdftitle5",product.get(i).getPdf5Title5());
                Paper.book().write("pdf6",product.get(i).getPdf6());
                Paper.book().write("pdftitle6",product.get(i).getPdf6Title6());
                Paper.book().write("pdf_name1",product.get(i).getPdfName());
                Paper.book().write("pdf_name2",product.get(i).getPdf2Name());
                Paper.book().write("pdf_name3",product.get(i).getPdf3Name());
                Paper.book().write("pdf_name4",product.get(i).getPdf4Name());
                Paper.book().write("pdf_name5",product.get(i).getPdf5Name());
                Paper.book().write("pdf_name6",product.get(i).getPdf6Name());
                Paper.book().write("publish_key",publishkey);
                Paper.book().write("in_stock",product.get(i).getIn_stock());
                Paper.book().write("vanstock",product.get(i).getVanStock());
            }
        });
    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView product_name,price,product_available_qnty,txtvw_product_out;
        CardView cardView;
        LinearLayout layout_van_qnty;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            price=itemView.findViewById(R.id.product_price);
            layout_van_qnty = itemView.findViewById(R.id.layout_van_qnty);
            product_image=itemView.findViewById(R.id.imgview_super_category);
            product_name=itemView.findViewById(R.id.txt_product_name_super_category);
            cardView=itemView.findViewById(R.id.cardview_super_category);
            product_available_qnty=itemView.findViewById(R.id.product_available_qnty);
            txtvw_product_out=itemView.findViewById(R.id.txtvw_product_out);
        }
    }
}
