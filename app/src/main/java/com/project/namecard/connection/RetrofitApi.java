package com.project.namecard.connection;

import com.project.namecard.models.ResultModel;
import com.project.namecard.models.LoginModel;
import com.project.namecard.models.MainFragmentInfoModel;
import com.project.namecard.models.SignUpModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {
    String BASE_URL = "https://rnryehd111.cafe24.com/NameCard/";

    //로그인 요청
    @GET("LoginRequest.php")
    Call<LoginModel> LoginRequest(@Query("ID") String ID,
                                  @Query("PassWord") String PassWord);

    //회원 가입 -> 아이디 중복 확인 요청
    @GET("LoginSignUpIDCheck.php")
    Call<SignUpModel> IDCheckRequest(@Query("ID") String ID);

    //회원가입 요청
    @GET("LoginSignUpRequest.php")
    Call<SignUpModel> SignUpRequest(@Query("ID") String ID,
                                    @Query("PassWord") String PassWord,
                                    @Query("Name") String Name,
                                    @Query("Birth") String Birth,
                                    @Query("Email") String Email,
                                    @Query("DBname") String DBname);

    //회원 정보 요청
    @GET("UserInfoRequest.php")
    Call<MainFragmentInfoModel> UserInfoRequest(@Query("ID") String ID,
                                                @Query("PassWord") String PassWord);

    //회원정보 수정 요청
    @GET("UserUpdateRequest.php")
    Call<MainFragmentInfoModel> UserUpdateRequest(@Query("ID") String ID,
                                                  @Query("PassWord") String PassWord,
                                                  @Query("Name") String Name,
                                                  @Query("Birth") String Birth,
                                                  @Query("Email") String Email);

    //회원 정보 삭제 요청
    @GET("UserDeleteRequest.php")
    Call<MainFragmentInfoModel> UserDeleteRequest(@Query("ID") String ID,
                                                  @Query("PassWord") String PassWord,
                                                  @Query("DBname") String DBname);

    @GET("CardDeleteMineRequest.php")
    Call<ResultModel> CardDeleteMineRequest(@Query("CardID") String CardID,
                                            @Query("DBname") String DBname);

    @GET("CardDeleteNotMineByMadeMeRequest.php")
    Call<ResultModel> CardDeleteNotMineByMadeMeRequest(@Query("CardID") String CardID,
                                                       @Query("DBname") String DBname);

    @GET("CardDeleteNotMineByExchangeRequest.php")
    Call<ResultModel> CardDeleteNotMineByExchangeRequest(@Query("CardID") String CardID,
                                                         @Query("DBname") String DBname);

    @GET("ChangeRepCardRequest.php")
    Call<ResultModel> ChangeRepCardRequest(@Query("CardID") String CardID,
                                           @Query("DBname") String DBname);

    @GET("QRCodeExchangeRequest.php")
    Call<ResultModel> QRCodeExchangeRequest(@Query("DBname") String DBname,
                                            @Query("CardID") String CardID,
                                            @Query("UserDB") String UserDB,
                                            @Query("UserID") String UserID);

}
