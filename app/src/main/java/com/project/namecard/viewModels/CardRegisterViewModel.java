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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.project.namecard.connection.CardRegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class CardRegisterViewModel extends AndroidViewModel {

    //뷰 변수
    public MutableLiveData<String> CardImage = new MutableLiveData<>("");
    public String CardBitmapString = "";
    public Bitmap bitmap = null;
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
    public MutableLiveData<String> Memo = new MutableLiveData<String>("");
    public String DBname;
    //UI 제어 변수
    public MutableLiveData<String> RegisterResult = new MutableLiveData<>("");

    //초기 세팅
    public CardRegisterViewModel(@NonNull Application application) {
        super(application);
        //아이디 값 받기
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ID.setValue(Auto.getString("ID", null));
        DBname = Auto.getString("DBname", null);
    }

    //이메일 주소 스피너 아이템 선택 이벤트
    public void AddressSelectClick(AdapterView<?> parent, View view, int pos, long id) {
        EmailAddress.setValue(parent.getSelectedItem().toString());
        AddressSelect.setValue(pos);
    }

    public void CardRegisterRequest(){
        RegisterResult.setValue("");

        //빈 정보 확인
        if(!("".equals(Name.getValue()) || null == Name.getValue()) &&
                !("".equals(Company.getValue()) || null == Company.getValue()) &&
                !("".equals(Depart.getValue()) || null == Depart.getValue()) &&
                !("".equals(Position.getValue()) || null == Position.getValue()) &&
                !("".equals(CompanyNumber.getValue()) || null == CompanyNumber.getValue()) &&
                !("".equals(PhoneNumber.getValue()) || null == PhoneNumber.getValue()) &&
                !("".equals(Email.getValue()) || null == Email.getValue()) &&
                !("".equals(EmailAddress.getValue()) || null == EmailAddress.getValue()) &&
                !("".equals(FaxNumber.getValue()) || null == FaxNumber.getValue()) &&
                !("".equals(Address.getValue()) || null == Address.getValue()) ) {

            //이메일, 주소 합치기
            String TotalEmail = Email.getValue() + "@" + EmailAddress.getValue();
            String TotalAddress = Address.getValue() + " " + Detailaddress.getValue();

            //이미지 존재시 리사이징, 스트링화
            bitmap = CardImageBitmap.getValue();
            if (bitmap != null) {
                //비트맵 리사이징
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int newWidth = width;
                int newHeight = height;

                while (newWidth >= 800) {
                    newWidth = newWidth * 3 / 4;
                    newHeight = newHeight * 3 / 4;
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                CardImageBitmap.setValue(bitmap);

                //비트맵.toString
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String text = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                CardImage.setValue(text);
            }

            //볼리 통신
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        Boolean success = jsonObject.getBoolean("success");
                        if(success) {
                            RegisterResult.setValue("true");
                        }
                        else{
                            RegisterResult.setValue("false");
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            CardRegisterRequest cardRegisterRequest = new CardRegisterRequest(CardImage.getValue(), ID.getValue(), Owner.getValue(), Name.getValue(), Company.getValue()
                    , Depart.getValue(), Position.getValue(), CompanyNumber.getValue(), PhoneNumber.getValue(), TotalEmail, FaxNumber.getValue(),
                    TotalAddress, Memo.getValue(), DBname, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplication().getBaseContext());
            queue.add(cardRegisterRequest);
        }
        else{
            RegisterResult.setValue("pleaseNoEmpty");
        }
    }

    public void CardImageDeleteClick(){
        CardImageBitmap.setValue(null);
        CardImage.setValue("");
        CardBitmapString = "";
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
    public LiveData<String> GetCardRegisterResult() {
        return RegisterResult;
    }
}
