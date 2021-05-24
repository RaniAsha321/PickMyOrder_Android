package com.pickmyorder.asharani;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.paperdb.Paper;

public class Project_details_already_added extends AppCompatActivity {

    TextView proj_name,customer_name,full_address,delivery_address,contact_no,email_address;
    String pro_name,customer_nam,full_addres,delivery_addres,contact,email_addres;
    LinearLayout project_remove,project_edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);
        proj_name=findViewById(R.id.project_names);

        customer_name=findViewById(R.id.customer_name);
        full_address=findViewById(R.id.full_address);
        delivery_address=findViewById(R.id.delivery_address);
        contact_no=findViewById(R.id.contact);
        email_address=findViewById(R.id.project_email);
        project_edit = findViewById(R.id.project_edit);
        project_remove = findViewById(R.id.project_remove);

        pro_name=Paper.book().read("pro_name");
        full_addres =Paper.book().read("full_addres");
        email_addres=Paper.book().read("email_addres");
        contact=Paper.book().read("contact");
        customer_nam=Paper.book().read("customer_nam");
        delivery_addres=Paper.book().read("delivery_addres");

        proj_name.setText(pro_name);
        customer_name.setText(customer_nam);
        email_address.setText(email_addres);
        contact_no.setText(contact);
        full_address.setText(full_addres);
        delivery_address.setText(delivery_addres);

/*
        if(!(Paper.book().read("datarole", "5").equals("4"))){

            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                project_edit.setVisibility(View.VISIBLE);
                project_remove.setVisibility(View.VISIBLE);
            }

            else {

                project_edit.setVisibility(View.GONE);
                project_remove.setVisibility(View.GONE);

            }

        }

        else {

            project_edit.setVisibility(View.GONE);
            project_remove.setVisibility(View.GONE);

        }*/

    }
}

