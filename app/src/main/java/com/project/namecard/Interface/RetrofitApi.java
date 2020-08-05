package com.project.namecard.Interface;

import com.project.namecard.models.LoginModel;
import com.project.namecard.models.SignUpModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {
    String BASE_URL = "https://rnryehd111.cafe24.com/BusinessCard/";

    @GET("LoginRequest.php")
    Call<LoginModel> LoginRequest(@Query("ID") String ID,
                           @Query("PassWord") String PassWord);

    @GET("LoginSignUpIDCheck.php")
    Call<SignUpModel> IDCheckRequest(@Query("ID") String ID);

    @GET("LoginSignUpRequest.php")
    Call<SignUpModel> SignUpRequest(@Query("ID") String ID,
                                    @Query("PassWord") String PassWord,
                                    @Query("Name") String Name,
                                    @Query("Birth") String Birth,
                                    @Query("Email") String Email,
                                    @Query("DBname") String DBname);
}
