package com.pickmyorder.asharani.Fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.ModelDefaultDeliveryAddress;
import com.pickmyorder.asharani.R;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Default_Delivery_address extends Fragment  {

    TextView txt_default_delivery_company_postcode,txt_default_delivery_company_city,txt_default_delivery_company_address,txt_default_delivery_company_name
            ,txt_default_delivery_address;
    Home homeobj;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default_delivery_address, container, false);

        txt_default_delivery_address=view.findViewById(R.id.txt_default_delivery_address);
        txt_default_delivery_company_address=view.findViewById(R.id.txt_default_delivery_company_address);
        txt_default_delivery_company_name=view.findViewById(R.id.txt_default_delivery_company_name);
        txt_default_delivery_company_city=view.findViewById(R.id.txt_default_delivery_company_city);
        txt_default_delivery_company_postcode=view.findViewById(R.id.txt_default_delivery_company_postcode);

        /************************************************************* Fragment Back handler  ***********************************************/

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("account", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }

                return false;
            }
        });

        /*************************************************************************************************************************************/

            Default_Delivery_address();

        return view;
    }

    private void Default_Delivery_address() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        service.DEFAULT_DELIVERY_ADDRESS_CALL(Paper.book().read("Wholeseller_engineer_id")).enqueue(new Callback<ModelDefaultDeliveryAddress>() {
            @Override
            public void onResponse(Call<ModelDefaultDeliveryAddress> call, Response<ModelDefaultDeliveryAddress> response) {

                if (response.body().getStatusCode().equals(200)){
                    Paper.book().write("default_delivery_address_statuscode",response.body().getStatusCode());
                    Paper.book().write("default_delivery_address",response.body().getDeleveryAddress());
                    Paper.book().write("default_delivery_company_address",response.body().getDeleveryCompanyAddress());
                    Paper.book().write("default_delivery_company_name",response.body().getDeleveryCompanyName());
                    Paper.book().write("default_delivery_company_city",response.body().getDeleveryCompanyCity());
                    Paper.book().write("default_delivery_company_postcode",response.body().getDeleveryCompanyPostcode());

                    txt_default_delivery_address.setText(Paper.book().read("default_delivery_address"));
                    txt_default_delivery_company_address.setText(Paper.book().read("default_delivery_company_address"));
                    txt_default_delivery_company_name.setText(Paper.book().read("default_delivery_company_name"));
                    txt_default_delivery_company_city.setText(Paper.book().read("default_delivery_company_city"));
                    txt_default_delivery_company_postcode.setText(Paper.book().read("default_delivery_company_postcode"));
                }
                else {

                    Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG);

                }
            }

            @Override
            public void onFailure(Call<ModelDefaultDeliveryAddress> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity.getClass() == Home.class) {
            homeobj = (Home) activity;
        }

    }
}
