package com.pickmyorder.asharani;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Adapter_Cat_Promo extends RecyclerView.Adapter<Adapter_Cat_Promo.ViewHolder>  {

    List<Cetalouge> my_list;
    Context mcontext;
    Catalogue_Promo_Images catalogue_promo_images;
    ImageView img_main_cat_promo;
    DownloadManager manager;
    File file;
    String []SepString ;
    String  []MainImage;
    String  []MainImage2;
    String []SepString2 ;
    String delimite_word ;
    String type,file_image,file_data,file_description;


    public Adapter_Cat_Promo(Context context, List<Cetalouge> my_list, Catalogue_Promo_Images cataloguePromoImages, ImageView img_main_cat_promo) {

        this.my_list=my_list;
        this.mcontext=context;
        this.catalogue_promo_images=cataloguePromoImages;
        this.img_main_cat_promo=img_main_cat_promo;

    }

    @NonNull
    @Override
    public Adapter_Cat_Promo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_cat_promo_img, parent, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        type = my_list.get(position).getType();
        file_image = my_list.get(position).getImageToDisplay();
        file_data = my_list.get(position).getUrl();
        file_description = my_list.get(position).getDescription();

        holder.txt_desc_catalog_item.setText(my_list.get(position).getDescription());
        Glide.with(mcontext).load(my_list.get(0).getImageToDisplay()).into(catalogue_promo_images.img_main_cat_promo);

        if (type.equals("jpeg") || type.equals("png") || type.equals("gif") || type.equals("webp") || type.equals("tiif")){

            Paper.book().write("pdf","0");
            Glide.with(mcontext).load(file_image).into(holder.img_row_promo);

        }

        else {
            Paper.book().write("pdf","1");
            if (type != null) {

                Glide.with(mcontext).load(file_image).into(holder.img_row_promo);
            }

        }


        catalogue_promo_images.img_main_cat_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("jpeg") || !type.equals("png") || !type.equals("gif") || !type.equals("webp") || !type.equals("tiif")) {

                    String pdf_permission = Paper.book().read("pdf");
                    String path = Paper.book().read("fullpath");

                    if (pdf_permission != null && pdf_permission.equals("1")) {

                        String fullPath = String.format(Locale.ENGLISH, file_data, "PDF_URL_HERE");

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullPath));
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mcontext.startActivity(browserIntent);
                    }

                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("jpeg") || type.equals("png") || type.equals("gif") || type.equals("webp") || type.equals("tiif")){

                    Paper.book().write("pdf","0");
                  //  catalogue_promo_images.txt_desc_catalog.setText(my_list.get(position).getDescription());
                   /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    catalogue_promo_images.img_main_cat_promo.setLayoutParams(params);*/
                    Glide.with(mcontext).load(my_list.get(position).getImageToDisplay()).into(catalogue_promo_images.img_main_cat_promo);


                }

                else {

                    Paper.book().write("pdf","1");
                   /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
                    catalogue_promo_images.img_main_cat_promo.setLayoutParams(params);*/
                   // catalogue_promo_images.txt_desc_catalog.setText(my_list.get(position).getDescription());
                    Glide.with(mcontext).load(my_list.get(position).getImageToDisplay()).into(catalogue_promo_images.img_main_cat_promo);
                    Paper.book().write("fullpath",my_list.get(position).getImageToDisplay());

                }


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap  pdfToBitmap(File pdfFile) {

        Bitmap bitmap = null;
        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
            final int pageCount = renderer.getPageCount();
            if(pageCount>0){
                PdfRenderer.Page page = renderer.openPage(0);
                int width = (int) (page.getWidth());
                int height = (int) (page.getHeight());
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_row_promo;
        TextView txt_desc_catalog_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            img_row_promo = itemView.findViewById(R.id.img_row_promo);
            txt_desc_catalog_item = itemView.findViewById(R.id.txt_desc_catalog_item);

        }
    }
}
