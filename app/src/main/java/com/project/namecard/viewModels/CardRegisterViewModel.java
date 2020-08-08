package com.project.namecard.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.namecard.Interface.RetrofitApi;
import com.project.namecard.models.CardRegisterModel;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardRegisterViewModel extends AndroidViewModel {

    //뷰 변수
    public MutableLiveData<String> CardImage = new MutableLiveData<String>("");
    public MutableLiveData<Bitmap> CardImageBitmap = new MutableLiveData<>(null);
    public MutableLiveData<String> ID = new MutableLiveData<String>();
    public MutableLiveData<String> Owner = new MutableLiveData<String>();
    public MutableLiveData<String> Name = new MutableLiveData<String>();
    public MutableLiveData<String> Company = new MutableLiveData<String>();
    public MutableLiveData<String> Depart = new MutableLiveData<String>();
    public MutableLiveData<String> Position = new MutableLiveData<String>();
    public MutableLiveData<String> CompanyNumber = new MutableLiveData<String>();
    public MutableLiveData<String> PhoneNumber = new MutableLiveData<String>();
    public MutableLiveData<String> Email = new MutableLiveData<String>();
    public MutableLiveData<String> EmailAddress = new MutableLiveData<String>();
    public MutableLiveData<Integer> AddressSelect = new MutableLiveData<>(0);
    public MutableLiveData<String> FaxNumber = new MutableLiveData<String>();
    public MutableLiveData<String> Address = new MutableLiveData<String>();
    public MutableLiveData<String> Detailaddress = new MutableLiveData<String>();
    public MutableLiveData<String> Memo = new MutableLiveData<String>(null);
    String DBname = "BD_user_";
    //UI 제어 변수
    public MutableLiveData<String> RegisterResult = new MutableLiveData<>("");
    //레트로핏
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);


    //초기 세팅
    public CardRegisterViewModel(@NonNull Application application) {
        super(application);
        //아이디 값 받기
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ID.setValue(Auto.getString("ID", null));
        //DBname 생성
        DBname += ID.getValue();
    }

    //이메일 주소 스피너 아이템 선택 이벤트
    public void AddressSelectClick(AdapterView<?> parent, View view, int pos, long id) {
        EmailAddress.setValue(parent.getSelectedItem().toString());
        AddressSelect.setValue(pos);
    }

    public void CardRegistorRequest(){
        RegisterResult.setValue("");
        if(!("".equals(Name.getValue()) || null == Name.getValue()) &&
                !("".equals(Company.getValue()) || null == Company.getValue()) &&
                !("".equals(Depart.getValue()) || null == Depart.getValue()) &&
                !("".equals(Position.getValue()) || null == Position.getValue()) &&
                !("".equals(CompanyNumber.getValue()) || null == CompanyNumber.getValue()) &&
                !("".equals(PhoneNumber.getValue()) || null == PhoneNumber.getValue()) &&
                !("".equals(Email.getValue()) || null == Email.getValue()) &&
                !("".equals(EmailAddress.getValue()) || null == EmailAddress.getValue()) &&
                !("".equals(FaxNumber.getValue()) || null == FaxNumber.getValue()) &&
                !("".equals(Address.getValue()) || null == Address.getValue()) ){

            //카드 이미지 리사이징

            retrofitApi.CardRegisetRequeset(ID.getValue(), "mine", CardImage.getValue(), Name.getValue(),
                    Company.getValue(), Depart.getValue(), Position.getValue(), CompanyNumber.getValue(), PhoneNumber.getValue(),
                    Email.getValue(), FaxNumber.getValue(), Address.getValue(), Memo.getValue(), DBname).enqueue(new Callback<CardRegisterModel>() {
                @Override
                public void onResponse(Call<CardRegisterModel> call, Response<CardRegisterModel> response) {
                    //성공
                    if(response.body()!=null){
                        if(response.body().getSuccess().equals("true")){
                            RegisterResult.setValue("success");
                        }
                        else{
                            RegisterResult.setValue("false");
                        }
                    }
                    else {
                        RegisterResult.setValue("false");
                    }
                }

                @Override
                public void onFailure(Call<CardRegisterModel> call, Throwable t) {
                    //실패
                }
            });
        }
        else {
            RegisterResult.setValue("pleaseNoEmpty");
        }
    }

    //이미지 리사이즈
    public void ImageResize() {
        int width = CardImageBitmap.getValue().getWidth();
        int height = CardImageBitmap.getValue().getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;
        int maxResolution = 800;
        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }
        CardImageBitmap.setValue(Bitmap.createScaledBitmap(CardImageBitmap.getValue(), newWidth, newHeight, true));
    }
    //Bitmap -> Byte (DB저장 위해)
    public void BitmaptoString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CardImageBitmap.getValue().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        CardImage.setValue(Base64.encodeToString(imageBytes, Base64.DEFAULT));
    }



    //이메일 주소선택 값 반환
    public LiveData<Integer> GetSelectResult() {
        return AddressSelect;
    }

    //카드 이미지 비트맵 반환
    public LiveData<Bitmap> GetCardImageBitmap(){
        return CardImageBitmap;
    }

    //카드 등록 결과 반환
    public LiveData<String> GetCardResterResult() {
        return RegisterResult;
    }
}
