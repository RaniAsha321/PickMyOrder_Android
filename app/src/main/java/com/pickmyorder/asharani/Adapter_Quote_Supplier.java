package com.pickmyorder.asharani;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Adapter_Quote_Supplier extends RecyclerView.Adapter<Adapter_Quote_Supplier.ViewHolder> {

    Context mycontext;
    List<SupplierDatum> myupdatelist;
    List<String> selectedStrings= new ArrayList<>();
    List<String> selectedIds= new ArrayList<>();
    Dialog dialog;

    public Adapter_Quote_Supplier(Context applicationContext, List<SupplierDatum> myupdate, Dialog dialog) {

        this.mycontext = applicationContext;
        this.myupdatelist = myupdate;
        this.dialog=dialog;
    }

    @NonNull
    @Override
    public Adapter_Quote_Supplier.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Quote_Supplier.ViewHolder viewHolder, int i) {

       // myupdatelist.remove(0);


        viewHolder.checkboxlist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if((Paper.book().read("uncheck",new ArrayList<String>()))!=null){

                        selectedStrings.add(myupdatelist.get(i).getSuppliersName());
                        selectedIds.add(myupdatelist.get(i).getId());
                        Paper.book().write("check",selectedStrings.toString());
                        Paper.book().write("checked",selectedStrings.size());
                        Paper.book().write("supplier_id",selectedIds.toString() );

                    }

                    else {

                        Paper.book().write("check","");
                    }

                    Log.e("selected",selectedStrings.toString()+"");
                    Log.e("selected",selectedIds.toString()+"");




                }

                else{

                    selectedStrings.remove(myupdatelist.get(i).getSuppliersName());

                    if(selectedStrings.size()!=0){
                        viewHolder.object2.tv_drop_supplier.setText((selectedStrings.toString()).replaceFirst("\\[", "").replaceAll("\\]", ""));
                        Paper.book().write("check","");
                        Paper.book().write("checked",selectedStrings.size());
                    }

                    else {

                        dialog.show();
                        Paper.book().write("checked",selectedStrings.size());
                    }

                    Log.e("selected1",selectedStrings.toString()+"");
                    Log.e("selected1",selectedStrings.size()+"");




                }

            }

        });


        dialog.findViewById(R.id.txt_update_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checked=Paper.book().read("checked");

                if ((checked!=0)){
                    if (!(Paper.book().read("check").equals(""))) {

                        viewHolder.checkboxlist.setChecked(true);
                        String str = (Paper.book().read("check"));
                        viewHolder.object2.tv_drop_supplier.setText(str.replaceFirst("\\[", "").replaceAll("\\]", ""));
                        Paper.book().write("finalString", str.replaceFirst("\\[", "").replaceFirst("\\]", ""));
                        Paper.book().write("supplier_id",selectedIds.toString().replaceFirst("\\[", "").replaceFirst("\\]", "") );
                    }

                    dialog.dismiss();
                }

                else{
                    dialog.show();
                    Toast.makeText(mycontext, "Select AtLeast 1 Engineer", Toast.LENGTH_SHORT).show();

                }
            }
        });


        String[] temp;
        String check=Paper.book().read("check");
        String finalString=viewHolder.object2.tv_drop_supplier.getText().toString();
        if (check!= null ) {
            if (!(finalString.equals("")) ) {
                String project = finalString;
                String[] project2 = project.split(",");
                String[] myarray2 = new String[project2.length];

                for (int k = 0; k < project2.length; k++) {
                    myarray2[k] = project2[k];


                    if (finalString!= null ) {
                        if ((myupdatelist.get(i).getSuppliersName()).equals(viewHolder.object2.tv_drop_supplier.getText().toString())) {
                            viewHolder.checkboxlist.setChecked(true);
                        }
                        else {
                            viewHolder.checkboxlist.setChecked(false);
                        }

                    }

                }

            }
        }


        String projectlist = viewHolder.object2.tv_drop_supplier.getText().toString();
        String[] projectlist2 = projectlist.split(",");
        String[] myarray = new String[projectlist2.length];

       /* for (int j = 0; j < projectlist2.length; j++) {
            myarray[j] = projectlist2[j];
            temp = myarray[j].split(" \\[");
            Paper.book().write("temp1",temp[1]);

            if (myupdatelist.get(i).getSuppliersName().equals(temp[1])){

                viewHolder.checkboxlist.setChecked(true);
            }
        }*/
            viewHolder.layout_update_pro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> mylist=new ArrayList<>();

                    if(viewHolder.checkboxlist.isChecked()){

                        viewHolder.checkboxlist.setChecked(false);

                    }
                    else {
                        viewHolder.checkboxlist.setChecked(true);
                        mylist.add(myupdatelist.get(i).getSuppliersName());

                    }
                }
            });



       viewHolder.layout_update_pro.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(viewHolder.checkboxlist.isChecked()){

                   viewHolder.checkboxlist.setChecked(false);
                   Paper.book().write("check","");

               }
               else {
                       viewHolder.checkboxlist.setChecked(true);

               }

           }
       });

        dialog.findViewById(R.id.txt_update_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

       viewHolder.txtview.setText(myupdatelist.get(i).getSuppliersName());
    }

    @Override
    public int getItemCount() {
        return myupdatelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Cart object2;
        CheckBox checkboxlist;
        TextView txtview;
        LinearLayout layout_update_pro;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkboxlist=itemView.findViewById(R.id.check_list);
            txtview=itemView.findViewById(R.id.text_view);
            layout_update_pro=itemView.findViewById(R.id.layout_update_pro);
            object2=new Cart();


        }
    }



}
