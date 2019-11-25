package com.example.assignmentapi.Utils;

import com.example.assignmentapi.Models.DataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("cardData")
    Call<List<DataModel>> getJSON();
}