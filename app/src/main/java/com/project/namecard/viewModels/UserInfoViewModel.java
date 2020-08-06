package com.project.namecard.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.namecard.Interface.RetrofitApi;
import com.project.namecard.models.UserInfoModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoViewModel extends AndroidViewModel {
    //뷰 변수
    public MutableLiveData<String> ID = new MutableLiveData<>();
    public MutableLiveData<String> PassWord = new MutableLiveData<>();
    public MutableLiveData<String> Name = new MutableLiveData<>();
    public MutableLiveData<String> Birth = new MutableLiveData<>();
    public MutableLiveData<String> Email = new MutableLiveData<>();
    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    //UI 제어 변수
    public MutableLiveData<String> UIStateText = new MutableLiveData<>("state1");
    public MutableLiveData<String> UserDateText = new MutableLiveData<>("");
    //레트로핏
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
    //모델
    UserInfoModel model = new UserInfoModel();

    //뷰모델 초기세팅
    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        //회원정보 받아오기
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ID.setValue(Auto.getString("ID", null));
        PassWord.setValue(Auto.getString("PassWord", null));
    }

    //유저 기본 정보 받기
    public void GetUserInfo(){
        retrofitApi.UserInfoRequest(ID.getValue(), PassWord.getValue()).enqueue(new Callback<UserInfoModel>() {
            @Override
            public void onResponse(Call<UserInfoModel> call, Response<UserInfoModel> response) {
                //모델 세팅
                model = response.body();
                //성공
                if (model.getSuccess().equals("true")) {
                    //결과 세팅
                    ID.setValue(model.getID());
                    PassWord.setValue(model.getPassWord());
                    Name.setValue(model.getName());
                    Birth.setValue(model.getBirth());
                    int i = model.getEmail().indexOf("@");
                    Email.setValue(model.getEmail().substring(0, i));
                    EmailAddress.setValue(model.getEmail().substring(i + 1));
                }
            }
            @Override
            public void onFailure(Call<UserInfoModel> call, Throwable t) {
                //실패
            }
        });
    }

    //유저 정보 업데이트 요청
    public void BtnUserUpdateRequestClick(){
        UserDateText.setValue("");
        //회원가입 정보 빈값 확인
        if(!("".equals(Name.getValue()) || null == Name.getValue()) &&
                !("".equals(Birth.getValue()) || null == Birth.getValue()) &&
                !("".equals(Email.getValue()) || null == Email.getValue()) &&
                !("".equals(EmailAddress.getValue()) || null == EmailAddress.getValue())){
            //전체 이메일
            String TotalEmail =  Email.getValue() + "@" + EmailAddress.getValue();
            //업데이트 요청 시작
            retrofitApi.UserUpdateRequest(ID.getValue(), PassWord.getValue(), Name.getValue(), Birth.getValue(), TotalEmail)
                    .enqueue(new Callback<UserInfoModel>() {
                        @Override
                        public void onResponse(Call<UserInfoModel> call, Response<UserInfoModel> response) {
                            //성공
                            if(response.body().getSuccess().equals("true")){
                                UserDateText.setValue("updateSuccess");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserInfoModel> call, Throwable t) {
                            //실패
                        }
                    });
        }
        else{
            UserDateText.setValue("pleaseNoBlank");
        }
    }

    //유저 정보 삭제 요청
    public void BtnUSerDeleteRequest() {
        retrofitApi.UserDeleteRequest(ID.getValue(), PassWord.getValue()).enqueue(new Callback<UserInfoModel>() {
            @Override
            public void onResponse(Call<UserInfoModel> call, Response<UserInfoModel> response) {
                //성공
                if(response.body().getSuccess().equals("true")){
                    UserDateText.setValue("deleteSuccess");
                }
            }
            @Override
            public void onFailure(Call<UserInfoModel> call, Throwable t) {
                //실패
            }
        });

    }

    //회원 탈퇴 성공 -> 자동로그인 정보 delete
    public void UserDeleteSuccess() {
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = Auto.edit();
        editor.clear();
        editor.commit();
    }

    //UI 상태 결과 반환
    public LiveData<String> GetUIStateText(){
        return UIStateText;
    }

    //유저 정보 상태 결과 반환
    public LiveData<String> GetUPDateText(){
        return UserDateText;
    }

}
