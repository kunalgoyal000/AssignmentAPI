package com.example.assignmentapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.assignmentapi.Adapters.DataAdapter;
import com.example.assignmentapi.Models.DataModel;
import com.example.assignmentapi.Utils.ApiClient;
import com.example.assignmentapi.Utils.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Dialog loadingDialog;
    private DataAdapter dataAdapter;
    private RecyclerView recyclerView;
    private LinearLayout noInternetContainer;
    private Button retryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////// Loading Dialog

        loadingDialog = new Dialog(MainActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));

        //////////// Loading Dialog

        recyclerView = findViewById(R.id.api_recyclerview);
        noInternetContainer = findViewById(R.id.no_internet_container);
        retryBtn = findViewById(R.id.retry_button);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //Loads data if internet is available
        if (isConnected()) {
            loadJSONData();
        } // if internet is not available
           else {
            recyclerView.setVisibility(View.GONE);
            noInternetContainer.setVisibility(View.VISIBLE);

            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reloadPage();
                }
            });
        }
    }

    //Loading JSON data from API and setting data in recyclerView
    private void loadJSONData() {

        loadingDialog.show();
        recyclerView.setVisibility(View.VISIBLE);
        noInternetContainer.setVisibility(View.GONE);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<DataModel>> call = apiService.getJSON();
        call.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                List<DataModel> dataModels = response.body();

                if(response !=null) {
                    dataAdapter = new DataAdapter(dataModels);
                    recyclerView.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    //Reload the page on clicking retry button
    private void reloadPage() {

        //if interent
        if (isConnected()) {
            Toast.makeText(this, "Internet is Available", Toast.LENGTH_SHORT).show();
            loadJSONData();
        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            noInternetContainer.setVisibility(View.VISIBLE);
        }
    }

    //Checking if internet is available or not
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting() == true) {
            return true;
        } else {
            return false;
        }
    }
}
