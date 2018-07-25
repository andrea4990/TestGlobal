package com.global.test.testglobal.Services;

import com.global.test.testglobal.Model.Item;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andrea on 25/07/2018.
 */

public interface GlobalApiServices {

    @GET("list")
    Call<ArrayList<Item>> getItemList();



}
