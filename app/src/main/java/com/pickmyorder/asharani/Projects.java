package com.pickmyorder.asharani;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Projects extends Fragment {

    Home home;
    List<ProjectDatum> projectData;
    List<ProjectDatum> mylist;
    Button btn_add;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Adapter_projects adapter_projects;
    LinearLayout layout_add_project;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_projects, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    return true;
                }

                home.drawerLayout.openDrawer(Gravity.START);


                return false;
            }
        });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());


        mylist= new ArrayList<>();
        ((Home)getActivity()).hideView(true);
        home.nav_search_layout.setVisibility(View.VISIBLE);


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Projects");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);


        recyclerView=view.findViewById(R.id.projects_recyclervw);
        btn_add=view.findViewById(R.id.addprobtn);
        layout_add_project=view.findViewById(R.id.layout_add_project);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        if(Paper.book().read("datarole", "5").equals("4")){

            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                layout_add_project.setVisibility(View.GONE);
            }

            else {

                Paper.book().write("layout_project_edit","0");
                layout_add_project.setVisibility(View.GONE);

            }
        }

        else {

            layout_add_project.setVisibility(View.VISIBLE);

        }



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getActivity(), Project_details_add_project.class);
                startActivity(intent);

            }
        });
        getprojects();

        return view;
    }

    private void getprojects() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String userid= Paper.book().read("userid");

        service.PROJECTS_CALL(userid).enqueue(new Callback<ModelProjects>() {
            @Override
            public void onResponse(Call<ModelProjects> call, Response<ModelProjects> response) {

                if(response.body().getStatusCode().equals(200)) {

                    projectData = response.body().getProjectData();

                    for (int i = 0; i < projectData.size(); i++) {

                        ProjectDatum projectDatum = new ProjectDatum();
                        projectDatum.setId(response.body().getProjectData().get(i).getId());
                        projectDatum.setProjectName(response.body().getProjectData().get(i).getProjectName());
                        projectDatum.setAddress(response.body().getProjectData().get(i).getAddress());
                        projectDatum.setJobStatus(response.body().getProjectData().get(i).getJobStatus());
                        projectDatum.setEmailAddress(response.body().getProjectData().get(i).getEmailAddress());
                        projectDatum.setContactNumber(response.body().getProjectData().get(i).getContactNumber());
                        projectDatum.setCustomer(response.body().getProjectData().get(i).getCustomer());
                        projectDatum.setDeliveryAddress(response.body().getProjectData().get(i).getDeliveryAddress());
                        projectDatum.setAllotedEngineers(response.body().getProjectData().get(i).getAllotedEngineers());

                        mylist.add(projectDatum);
                    }

                    Paper.book().write("projects", mylist);

                    mylist.remove(0);
                    adapter_projects = new Adapter_projects(getActivity(), mylist);
                    recyclerView.setAdapter(adapter_projects);
                }
                else{

                    Toast.makeText(getActivity(),"Not Found",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ModelProjects> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("failure",t.getMessage()+"");
            }
        });
    }


    public void onAttach (Activity activity) {
        super.onAttach(activity);
        if(activity.getClass()== Home.class){
            home = (Home) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Projects");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }


}
