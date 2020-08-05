package com.project.namecard.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.namecard.Interface.RetrofitApi;
import com.project.namecard.models.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginViewModel extends ViewModel {

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

    //로그인 버튼 클릭 이벤트
    public void SignInBtnClick(){

        String id = ID.getValue();
        String password = PassWord.getValue();

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
