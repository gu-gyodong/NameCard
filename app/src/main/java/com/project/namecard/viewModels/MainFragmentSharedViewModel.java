package com.project.namecard.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.namecard.connection.RetrofitApi;
import com.project.namecard.models.CardListModel;
import com.project.namecard.models.MainFragmentInfoModel;
import com.project.namecard.models.ResultModel;
import com.project.namecard.repository.MainFragmentCardRepository;
import com.project.namecard.views.CardClickView;
import com.project.namecard.views.MainView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragmentSharedViewModel extends AndroidViewModel {

    //레트로핏
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

    //////////Card 변수//////////
    //카드 레포지토리 변수
    public MainFragmentCardRepository mainFragmentCardRepository;

    //////////Exchange 변수//////////
    public MutableLiveData<String> MyRepCardID = new MutableLiveData<>();
    public MutableLiveData<String> UserID = new MutableLiveData<>();
    public MutableLiveData<String> UserDB = new MutableLiveData<>();
    public MutableLiveData<String> ExchangeResult = new MutableLiveData<>();

    //////////Info 변수//////////
    //Info -> 유저 정보 뷰 변수
    public MutableLiveData<String> ID = new MutableLiveData<>();
    public MutableLiveData<String> PassWord = new MutableLiveData<>();
    public MutableLiveData<String> DBname = new MutableLiveData<>();
    public MutableLiveData<String> Name = new MutableLiveData<>();
    public MutableLiveData<String> Birth = new MutableLiveData<>();
    public MutableLiveData<String> Email = new MutableLiveData<>();
    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    //UI 제어 변수
    public MutableLiveData<String> UIStateText = new MutableLiveData<>("state1");
    public MutableLiveData<String> UserDateText = new MutableLiveData<>("");
    //모델
    MainFragmentInfoModel InfoModel = new MainFragmentInfoModel();

    //viewModel 초기 설정 - > shared Fragment viewModel
    public MainFragmentSharedViewModel(@NonNull Application application) {
        super(application);

        //회원정보 받아오기
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ID.setValue(Auto.getString("ID", null));
        PassWord.setValue(Auto.getString("PassWord", null));
        DBname.setValue(Auto.getString("DBname", null));

        //카드 레포지토리 생성
        mainFragmentCardRepository = new MainFragmentCardRepository(getApplication());
    }

    //////////Card 메소드//////////
    //내 대표 카드 반환
    public LiveData<CardListModel> getMyRepCard(){
        return mainFragmentCardRepository.MyRepCard;
    }
    //교환한 카드 반환
    public LiveData<ArrayList<CardListModel>> getNotMineCardList(){
        return mainFragmentCardRepository.NotMineCardList;
    }
    //내 대표카드 아이디 반환
    public LiveData<String> getMyRepCardID() {
        return mainFragmentCardRepository.MyRepCardID;
    }

    //////////Exchange 메소드//////////
    //QR코드 교환
    public void CardExchange() {
        retrofitApi.CardExchangeRequest(DBname.getValue(), getMyRepCardID().getValue(), UserDB.getValue(), UserID.getValue()).enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                //성공
                if(response.body().getSuccess().equals("true")){
                    ExchangeResult.setValue("success");
                }
                else {
                    ExchangeResult.setValue("fail");
                }
            }
            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                //실패
            }
        });
    }
    //QR코드 결과 반환
    public LiveData<String> GetExchangeResult() {
        return ExchangeResult;
    }

    //////////Info 메소드//////////
    //유저 기본 정보 받기
    public void GetUserInfo(){
        retrofitApi.UserInfoRequest(ID.getValue(), PassWord.getValue()).enqueue(new Callback<MainFragmentInfoModel>() {
            @Override
            public void onResponse(Call<MainFragmentInfoModel> call, Response<MainFragmentInfoModel> response) {
                //모델 세팅
                InfoModel = response.body();
                //성공
                if (InfoModel.getSuccess().equals("true")) {
                    //결과 세팅
                    ID.setValue(InfoModel.getID());
                    PassWord.setValue(InfoModel.getPassWord());
                    Name.setValue(InfoModel.getName());
                    Birth.setValue(InfoModel.getBirth());
                    int i = InfoModel.getEmail().indexOf("@");
                    Email.setValue(InfoModel.getEmail().substring(0, i));
                    EmailAddress.setValue(InfoModel.getEmail().substring(i + 1));
                }
            }
            @Override
            public void onFailure(Call<MainFragmentInfoModel> call, Throwable t) {
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
                    .enqueue(new Callback<MainFragmentInfoModel>() {
                        @Override
                        public void onResponse(Call<MainFragmentInfoModel> call, Response<MainFragmentInfoModel> response) {
                            //성공
                            if(response.body().getSuccess().equals("true")){
                                UserDateText.setValue("updateSuccess");
                            }
                        }
                        @Override
                        public void onFailure(Call<MainFragmentInfoModel> call, Throwable t) {
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
        retrofitApi.UserDeleteRequest(ID.getValue(), PassWord.getValue(), DBname.getValue()).enqueue(new Callback<MainFragmentInfoModel>() {
            @Override
            public void onResponse(Call<MainFragmentInfoModel> call, Response<MainFragmentInfoModel> response) {
                //성공
                if(response.body().getSuccess().equals("true")){
                    UserDateText.setValue("deleteSuccess");
                }
            }
            @Override
            public void onFailure(Call<MainFragmentInfoModel> call, Throwable t) {
                //실패
            }
        });

    }
    //로그아웃
    public void LogOutRequest() {
        //회원 정보 임시저장 삭체
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = Auto.edit();
        editor.clear();
        editor.commit();
        UserDateText.setValue("LogOutSuccess");

    }
    //회원 탈퇴 성공 -> 자동로그인 정보 deleteimage
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
