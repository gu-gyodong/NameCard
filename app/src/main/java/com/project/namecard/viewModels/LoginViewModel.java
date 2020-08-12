package com.project.namecard.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.namecard.Connection.RetrofitApi;
import com.project.namecard.models.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginViewModel extends AndroidViewModel {

    //뷰 변수
    public MutableLiveData<String> ID = new MutableLiveData<>();
    public MutableLiveData<String> PassWord = new MutableLiveData<>();
    public MutableLiveData<String> success = new MutableLiveData<>();
    //뷰 반환 변수
    public MutableLiveData<LoginModel> result = new MutableLiveData<>();
    //모델 변수
    private LoginModel model = new LoginModel();
    //레트로핏
    private RetrofitApi retrofitApi;
    //유저 디비

    //뷰모델 초기세팅
    public LoginViewModel(@NonNull Application application) {
        super(application);

        //자동 로그인 정보 확인
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ID.setValue(Auto.getString("ID", null));
        PassWord.setValue(Auto.getString("PassWord", null));
        //자동 로그인 정보 != null 로그인 시도
        if(!("".equals(ID.getValue()) || null == ID.getValue()) &&
                !("".equals(PassWord.getValue()) || null == PassWord.getValue())){
            SignInBtnClick();
        }
    }

    //로그인 버튼 클릭 이벤트
    public void SignInBtnClick(){

        final String id = ID.getValue();
        final String password = PassWord.getValue();
        final String DBname = "BC_user_" + id;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitApi = retrofit.create(RetrofitApi.class);
        retrofitApi.LoginRequest(id, password).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if(response.body() !=null){
                    //성공
                    if(response.body().getSuccess().equals("true")){
                        success.setValue(response.body().getSuccess());
                        //자동 로그인 쉐어프리퍼렌스
                        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = Auto.edit();
                        editor.putString("ID", id);
                        editor.putString("PassWord", password);
                        editor.putString("DBname", DBname);
                        editor.commit();
                    }
                    else{
                        success.setValue("false");
                    }
                    model.setLogin(success.getValue(), ID.getValue(), PassWord.getValue());
                    result.setValue(model);
                }
            }
            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                //실패
            }
        });
    }

    //로그인 결과 반환
    public LiveData<LoginModel> getLoginResult(){
        if(result == null){
            result = new MutableLiveData<>();
        }
        return  result;
    }

}
