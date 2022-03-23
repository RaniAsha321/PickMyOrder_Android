package com.pickmyorder.asharani.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pickmyorder.asharani.R;
import com.pickmyorder.asharani.Storage.databaseSqlite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;

import static com.pickmyorder.asharani.Activities.Next_Login_Page.MY_PREFS_NAME;

public class Project_details_already_added extends AppCompatActivity {

    TextView proj_name,customer_name,full_address,delivery_address,contact_no,email_address;
    String pro_name,customer_nam,full_addres,delivery_addres,contact,email_addres;
    LinearLayout project_remove,project_edit;
    final Handler handler = new Handler();
    final int delay = 1000; // 1000 milliseconds == 1 second
    long Todaymsecond;
    String business_validity;
    com.pickmyorder.asharani.Storage.databaseSqlite databaseSqlite;

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

        Log.e("addres_full",full_addres+"");
        Log.e("addres_delivery",delivery_addres+"");
        databaseSqlite = new databaseSqlite(getApplicationContext());
        proj_name.setText(pro_name);
        customer_name.setText(customer_nam);
        email_address.setText(email_addres);
        contact_no.setText(contact);
        full_address.setText(full_addres);
        delivery_address.setText(delivery_addres);

        SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        business_validity =  sharedPreferences.getString("business_validity",null);
        sharedPreferences.getAll();



    }

    @Override
    protected void onResume() {
        super.onResume();

        Business_Validity_Check(business_validity);

    }


    private void Business_Validity_Check(String business_validity) {

        if(business_validity != null && !business_validity.equals("")){

            processCurrentTime(business_validity);

        }
    }

    private void processCurrentTime(String business_validity) {

        if (!isDataConnectionAvailable(Project_details_already_added .this)) {
            showerrorDialog("No Network coverage!");
        } else {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date mDate = sdf.parse(business_validity);
                long timeInMilliseconds = mDate.getTime();

                Log.e("timeInMilliseconds",timeInMilliseconds+"");
                checkExpiry(timeInMilliseconds);

            } catch (ParseException e) {
                e.printStackTrace();
            }




        }
    }

    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null)
            return false;

        return connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void checkExpiry(long timestampinMillies) {

        Date What_Is_Today= Calendar.getInstance().getTime();
        SimpleDateFormat Dateformat = new SimpleDateFormat("yyyy/MM/dd");
        String Today=Dateformat.format(What_Is_Today);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date mDate = sdf.parse(Today);
            Todaymsecond = mDate.getTime();


        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (timestampinMillies <= Todaymsecond) {
            showerrorDialog("Validity of Trial Version has been Expired");
        }

    }

    private void showerrorDialog(String data) {

        final Dialog dialog= new Dialog(Project_details_already_added.this);
        dialog.setContentView(R.layout.custom_dialog_trial);
        dialog.show();
        Button btn_continue = dialog.findViewById(R.id.btn_continue_trial);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

                SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.clear();

                editor.commit();

                startActivity(intent);

                databaseSqlite.deleteAll();

                Paper.book().write("datarole","");
                Paper.book().write("permission_see_cost","");
                Paper.book().write("permission_cat","");
                Paper.book().write("permission_orders","");
                Paper.book().write("permission_pro_detailsss","");
                Paper.book().write("permission_catelogues","");
                Paper.book().write("permission_all_orders","");
                Paper.book().write("permission_awaiting","");
                Paper.book().write("deviceid","");
                Paper.book().write("permission_wholeseller", "");
                Paper.book().write("ViewWholesellerPage", "100");
            }
        });

        dialog.setCancelable(false);
    }

 }

