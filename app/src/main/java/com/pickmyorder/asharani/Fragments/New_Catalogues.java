package com.pickmyorder.asharani.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Adapters.Adapter_get_shelves;
import com.pickmyorder.asharani.Adapters.Adapter_new_catalogue;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.util.ItemOffsetDecoration;
import com.pickmyorder.asharani.Models.ModelGetSections;
import com.pickmyorder.asharani.Models.Shelvesdatum;
import com.pickmyorder.asharani.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class New_Catalogues extends Fragment {

    Home homes;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerview_catalogues,recyclerview_promotions;
    Adapter_new_catalogue adapter_catalogues;
    Adapter_get_shelves adapter_new_promotions;
    List<Shelvesdatum> datalist;
    List<Shelvesdatum> mlist;
    String cataloglist;
    TextView shelves_headings;
    private FirebaseAnalytics mFirebaseAnalytics;
    int spanCount = 3;
    int spacing = 25;
    boolean includeEdge = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.catalogue_promotion, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("catalogs_home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }

              //  homes.drawerLayout.openDrawer(Gravity.START);

                return false;
            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "New_Catalogues");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        mlist=new ArrayList<Shelvesdatum>();
        recyclerview_catalogues=view.findViewById(R.id.recyclervw_catalogues_new);
        recyclerview_promotions=view.findViewById(R.id.recyclerview_promotions);
        shelves_headings = view.findViewById(R.id.shelves_headings);
        ((Home)getActivity()).hideView(true);
        homes.nav_search_layout.setVisibility(View.VISIBLE);

        String Shelve_name = Paper.book().read("Shelve_name");

        shelves_headings.setText(Shelve_name);
        Log.e("homes","1");

        getCatalogues();

        adapter_catalogues=new Adapter_new_catalogue(getActivity());
        recyclerview_catalogues.setLayoutManager(new LinearLayoutManager(getActivity(),  RecyclerView.HORIZONTAL, false));
        recyclerview_catalogues.setAdapter(adapter_catalogues);


        layoutManager = new LinearLayoutManager(getActivity());

       /* adapter_new_promotions=new Adapter_new_promotions(getActivity());
        recyclerview_promotions.setAdapter(adapter_new_promotions);
*/

        return view;

    }

    private void getCatalogues() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

       // String userid= Paper.book().read("userid");

        String Shelve_id= Paper.book().read("Shelve_id");

        service.GETSHELVES(Shelve_id).enqueue(new Callback<ModelGetSections>() {
            @Override
            public void onResponse(Call<ModelGetSections> call, Response<ModelGetSections> response) {

                if(response.body().getStatusCode().equals(200)){

                    datalist=response.body().getShelvesdata();

                    for(int i=0;i<datalist.size();i++){

                        Shelvesdatum datum1=new Shelvesdatum();
                        datum1.setId(response.body().getShelvesdata().get(i).getId());
                        datum1.setHeading(response.body().getShelvesdata().get(i).getHeading());
                        datum1.setSectionImage(response.body().getShelvesdata().get(i).getSectionImage());
                        datum1.setCetalouge(response.body().getShelvesdata().get(i).getCetalouge());

                        mlist.add(datum1);

                       // cataloglist = response.body().getShelvesdata().get(i).getShelves();


                    }

                   /* List<String> myList = new ArrayList<String>(Arrays.asList(s.split(",")));

                    Paper.book().write("CatImageList",myList);
*/
                    adapter_new_promotions=new Adapter_get_shelves(getActivity(),mlist);

                    recyclerview_promotions.addItemDecoration(new ItemOffsetDecoration(spanCount, spacing, includeEdge));

                    recyclerview_promotions.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));

                    recyclerview_promotions.setAdapter(adapter_new_promotions);
                }

                else

                {
                    FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.new_cat_container,new NoCatelogues());
                    fragmentTransaction.commit();

                }


            }

            @Override
            public void onFailure(Call<ModelGetSections> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }
        });

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity.getClass() == Home.class) {
            homes = (Home) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "New_Catelogue");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }



}
