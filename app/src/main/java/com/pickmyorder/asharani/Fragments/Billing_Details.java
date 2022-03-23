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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.ModelBillingDetails;
import com.pickmyorder.asharani.R;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Billing_Details extends Fragment  {

    TextView txt_billing_company_postcode,txt_billing_company_city,txt_billing_company_address,txt_billing_company_name
            ,txt_billing_address;
    private FirebaseAnalytics mFirebaseAnalytics;
    Home homeobj;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billing_details, container, false);

        txt_billing_address=view.findViewById(R.id.txt_billing_address);
        txt_billing_company_address=view.findViewById(R.id.txt_billing_company_address);
        txt_billing_company_name=view.findViewById(R.id.txt_billing_company_name);
        txt_billing_company_city=view.findViewById(R.id.txt_billing_company_city);
        txt_billing_company_postcode=view.findViewById(R.id.txt_billing_company_postcode);

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

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Billing_Details");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);


        /*************************************************************************************************************************************/

            Billing();

        return view;
    }

    private void Billing() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        service.BILLING_DETAILS_CALL(Paper.book().read("Wholeseller_engineer_id")).enqueue(new Callback<ModelBillingDetails>() {
            @Override
            public void onResponse(Call<ModelBillingDetails> call, Response<ModelBillingDetails> response) {

                if (response.body().getStatusCode().equals(200)){

                    Paper.book().write("txt_billing_address_statuscode",response.body().getStatusCode());
                    Paper.book().write("txt_billing_address",response.body().getBillingAddress());
                    Paper.book().write("txt_billing_company_address",response.body().getCompanyAddress());
                    Paper.book().write("txt_billing_company_name",response.body().getCompanyName());
                    Paper.book().write("txt_billing_company_city",response.body().getCompanyCity());
                    Paper.book().write("txt_billing_company_postcode",response.body().getCompanyPostcode());

                    txt_billing_address.setText(Paper.book().read("txt_billing_address"));
                    txt_billing_company_address.setText(Paper.book().read("txt_billing_company_address"));
                    txt_billing_company_name.setText(Paper.book().read("txt_billing_company_name"));
                    txt_billing_company_city.setText(Paper.book().read("txt_billing_company_city"));
                    txt_billing_company_postcode.setText(Paper.book().read("txt_billing_company_postcode"));

                }
                else {

                    Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG);

                }
            }

            @Override
            public void onFailure(Call<ModelBillingDetails> call, Throwable t) {

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

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Billing_Details");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

    }
}
