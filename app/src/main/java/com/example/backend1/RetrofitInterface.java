package com.example.backend1;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

//encode the details of the parameters and request methods that we are going to use
public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String>map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String>map);

}
