package com.project.namecard.connection;

import com.project.namecard.models.LoginModel;
import com.project.namecard.models.SignUpModel;
import com.project.namecard.models.MainFragmentInfoModel;

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
                                                  @Query("PassWord") String PassWord);

    //프레그먼트 카드 리스트 요청
    @GET("FragmentCardListRequest.php")
    Call<String> FragmentCardListRequexst(@Query("DBname") String DBname);

//    //카드 등록 요청
//    @GET("CardRegisterRequest.php")
//    Call<CardRegisterModel> CardRegisterRequest(@Query("ID") String ID,
//                                                @Query("Owner") String Owner,
//                                                @Query("CardImage") String CardImage,
//                                                @Query("Name") String Name,
//                                                @Query("Company") String Company,
//                                                @Query("Depart") String Depart,
//                                                @Query("Position") String Position,
//                                                @Query("CompanyNumber") String CompanyNumber,
//                                                @Query("PhoneNumber") String PhoneNumber,
//                                                @Query("Email") String Email,
//                                                @Query("FaxNumber") String FaxNumber,
//                                                @Query("Address") String Address,
//                                                @Query("Memo") String Memo,
//                                                @Query("DBname") String DBname);




}
