package com.project.namecard.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.namecard.Interface.RetrofitApi;
import com.project.namecard.models.MainFragmentMyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragmentMyViewModel extends AndroidViewModel {
    //뷰 변수
    public MutableLiveData<String> CardID = new MutableLiveData<>();
    public MutableLiveData<String> ID = new MutableLiveData<>();
    public MutableLiveData<String> Owner = new MutableLiveData<>();
    public MutableLiveData<String> CardImage = new MutableLiveData<>();
    public MutableLiveData<String> Name = new MutableLiveData<>();
    public MutableLiveData<String> Company = new MutableLiveData<>();
    public MutableLiveData<String> Depart = new MutableLiveData<>();
    public MutableLiveData<String> Position = new MutableLiveData<>();
    public MutableLiveData<String> CompanyNumber = new MutableLiveData<>();
    public MutableLiveData<String> PhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> Email = new MutableLiveData<>();
    public MutableLiveData<String> FaxNumber = new MutableLiveData<>();
    public MutableLiveData<String> Address = new MutableLiveData<>();
    public MutableLiveData<String> Memo = new MutableLiveData<>();
    public MutableLiveData<MainFragmentMyModel> MyRepCard = new MutableLiveData<>();
    public MutableLiveData<List<MainFragmentMyModel>> NotMineCardList = new MutableLiveData<>();
    //UI 제어 변수
    //레트로핏
    private String DBname = "BC_user_";
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);




    public MainFragmentMyViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ID.setValue(Auto.getString("ID", null));
        DBname += ID;
    }

    //카드 리스트 초기 세팅
    public void StatCardSetting(){
        retrofitApi.FragmentCardListRequest(DBname).enqueue(new Callback<List<MainFragmentMyModel>>() {
            @Override
            public void onResponse(Call<List<MainFragmentMyModel>> call, Response<List<MainFragmentMyModel>> response) {
                //성공
                if(response.body() != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for(int i=0 ; i<jsonArray.length() ; i++){
                            //todo
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<MainFragmentMyModel>> call, Throwable t) {
                //실패
            }
        });
    }

}
