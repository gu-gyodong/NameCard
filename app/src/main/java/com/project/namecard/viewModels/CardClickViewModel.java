package com.project.namecard.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.project.namecard.R;
import com.project.namecard.connection.CardClickViewRequest;
import com.project.namecard.connection.CardUpdateRequest;
import com.project.namecard.connection.RetrofitApi;
import com.project.namecard.models.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CardClickViewModel extends AndroidViewModel {

    //카드 변수
    public MutableLiveData<String> ID = new MutableLiveData<>();
    public MutableLiveData<String> CardID = new MutableLiveData<>();
    public MutableLiveData<String> Owner = new MutableLiveData<>();
    public MutableLiveData<String> DBname = new MutableLiveData<>();
    //뷰 변수
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
    public MutableLiveData<String> CardImageText = new MutableLiveData<>();
    //결과 변수
    public MutableLiveData<String> result = new MutableLiveData<>("");
    //이미지 변수
    private Bitmap bitmap = null;
    public MutableLiveData<Bitmap> imageBitmap = new MutableLiveData<>();
    //레트로핏
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

    public CardClickViewModel(@NonNull Application application) {
        super(application);
        //DBname get
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        DBname.setValue(Auto.getString("DBname", null));
    }
    //카드 정보 get
    public void getCardInfo() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    if(success){
                        //카드 정보 확인시 데이터 Set
                        Name.setValue(jsonObject.getString("Name"));
                        Company.setValue(jsonObject.getString("Company"));
                        Depart.setValue(jsonObject.getString("Depart"));
                        Position.setValue(jsonObject.getString("Position"));
                        CompanyNumber.setValue(jsonObject.getString("CompanyNumber"));
                        PhoneNumber.setValue(jsonObject.getString("PhoneNumber"));
                        Email.setValue(jsonObject.getString("Email"));
                        FaxNumber.setValue(jsonObject.getString("FaxNumber"));
                        Address.setValue(jsonObject.getString("Address"));
                        Memo.setValue(jsonObject.getString("Memo"));
                        CardImageText.setValue(jsonObject.getString("CardImage"));

                        //카드 이미지 유무 확인
                        if(CardImageText.getValue().equals("")){
                            DrawImage();
                        }
                        else{
                            //이미지 String -> Bitmap
                            byte[] imageBytes = Base64.decode(CardImageText.getValue(), Base64.DEFAULT);
                            imageBitmap.setValue(BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.length));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        CardClickViewRequest cardClickViewRequest = new CardClickViewRequest(CardID.getValue(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication().getBaseContext());
        queue.add(cardClickViewRequest);
    }
    //카드 업데이트 클릭
    public void CardUpdateEvent() {
        result.setValue("");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    if(success){
                        result.setValue("UpdateSuccess");
                    }
                    else{
                        result.setValue("UpdateFail");
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        CardUpdateRequest cardUpdateRequest = new CardUpdateRequest(CardID.getValue(), Name.getValue(), Company.getValue(), Depart.getValue(),
                Position.getValue(), CompanyNumber.getValue(), PhoneNumber.getValue(), Email.getValue(),FaxNumber.getValue(),
                Address.getValue(), Memo.getValue() ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication().getBaseContext());
        queue.add(cardUpdateRequest);
    }
    //카드 삭제 클릭
    public void CardDeleteEvent() {
        if(Owner.getValue().equals("mine")){
            retrofitApi.CardDeleteMineRequest(CardID.getValue(), DBname.getValue()).enqueue(new Callback<ResultModel>() {
                @Override
                public void onResponse(Call<ResultModel> call, retrofit2.Response<ResultModel> response) {
                    //성공
                    if(response.body().getSuccess().equals("true")){
                        result.setValue("DeleteSuccess");
                    }
                }
                @Override
                public void onFailure(Call<ResultModel> call, Throwable t) {
                    //실패
                }
            });
        }
        else if( Owner.getValue().equals("notmine")){
            retrofitApi.CardDeleteNotMineRequest(CardID.getValue(), DBname.getValue()).enqueue(new Callback<ResultModel>() {
                @Override
                public void onResponse(Call<ResultModel> call, retrofit2.Response<ResultModel> response) {
                    //성공
                    if(response.body().getSuccess().equals("true")){
                        result.setValue("DeleteSuccess");
                    }
                }
                @Override
                public void onFailure(Call<ResultModel> call, Throwable t) {
                    //실패
                }
            });
        }
    }
    //이미지 그리기
    private void DrawImage(){
        //이메일 ID Address 분리
        String EmailID, EmailAddress;
        EmailID = Email.getValue().substring(0, Email.getValue().indexOf("@"));
        EmailAddress = "@" + Email.getValue().substring(Email.getValue().indexOf("@")+1, Email.getValue().length());
        //Get 베이스 이미지 Bitmap
        bitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.card_base_image);
        //새비트맵 생성
        imageBitmap.setValue(Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888));
        //캔버스 생성
        Canvas canvas = new Canvas(imageBitmap.getValue());
        //캔버스에 베이스 이미지 그리기
        canvas.drawBitmap(bitmap,0,0,null);
        //Text Size 설정
        Paint textSize350 = new Paint();
        Paint textSize250 = new Paint();
        Paint textSize200 = new Paint();
        Paint textSize180 = new Paint();
        Paint textSize140 = new Paint();
        textSize350.setTextSize(350);
        textSize250.setTextSize(250);
        textSize200.setTextSize(200);
        textSize180.setTextSize(180);
        textSize140.setTextSize(140);
        //Text 위치 조정
        canvas.drawText(Name.getValue(),200,550, textSize350);
        canvas.drawText(Position.getValue(),250, 870, textSize200);
        canvas.drawText(PhoneNumber.getValue(),1950, 530, textSize180);
        canvas.drawText(CompanyNumber.getValue(), 1950, 770, textSize180);
        canvas.drawText(EmailID, 2000, 955, textSize140);
        canvas.drawText(EmailAddress, 2200, 1100, textSize140);
        canvas.drawText(Company.getValue(),200, 1630, textSize250);
        canvas.drawText(Address.getValue(),200, 1800, textSize140);
    }
    //대표 카드 변경
    public void ChangeRepCard() {
        retrofitApi.ChangeRepCardRequest(CardID.getValue(), DBname.getValue()).enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, retrofit2.Response<ResultModel> response) {
                //성공
                if(response.body().getSuccess().equals("true")){
                    result.setValue("RepCardChangeSuccess");
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                //실패
            }
        });
    }
    //이미지 return
    public LiveData<Bitmap> ImageBitmapReturn() {
        return imageBitmap;
    }
    //결과 반환
    public LiveData<String> ResultReturn() {
        return result;
    }
}
