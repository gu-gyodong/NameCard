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
import com.project.namecard.connection.MyCardListRequest;
import com.project.namecard.models.CardListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCardListViewModel extends AndroidViewModel {

    //디비 변수
    public MutableLiveData<String> ID = new MutableLiveData<>();
    public MutableLiveData<String> DBname = new MutableLiveData<>();
    //뷰 모델
    public MutableLiveData<ArrayList<CardListModel>> MyCardList = new MutableLiveData<>();//교환한 카드 전체 리스트
    public ArrayList<CardListModel> MyCard = new ArrayList<>();
    //이미지 세팅 변수
    private String CardID, Name, Company, CardImage, Position, PhoneNumber, CompanyNumber, Address, Email, Owner;
    private Bitmap CardImageBitmap = null, bitmap = null, imageBitmap = null;

    public MyCardListViewModel(@NonNull Application application) {
        super(application);

        //회원정보 받아오기
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ID.setValue(Auto.getString("ID", null));
        DBname.setValue(Auto.getString("DBname", null));

        setMyCardList();
    }

    private void setMyCardList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //결과 값 받기
                try {
                    //결과 값 받기
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    MyCard.clear();
                    MyCardList.setValue(null);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        // 카드 정보 받기
                        CardID = item.getString("CardID");
                        Owner = item.getString("Owner");
                        Name = item.getString("Name");
                        Company = item.getString("Company");
                        CardImage = item.getString("CardImage");
                        Position = item.getString("Position");
                        PhoneNumber = item.getString("PhoneNumber");
                        CompanyNumber = item.getString("CompanyNumber");
                        Address = item.getString("Address");
                        Email = item.getString("Email");

                        //카드이미지 null
                        if (CardImage.equals("")) {
                            DrawImage();
                            CardImageBitmap = imageBitmap;
                        }
                        //카드 이미지 not null
                        else {
                            //카드 이미지 string -> bitmap
                            byte[] imageBytes = Base64.decode(item.getString("CardImage"), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            //비트맵 대입
                            CardImageBitmap = bitmap;
                        }

                        MyCard.add(new CardListModel(Name, Company, CardImageBitmap, CardID, ID.getValue(), Owner));

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                //내카드 리스트 -> 초기화 후 라이브데이터 set
                MyCardList.setValue(MyCard);
            }
        };
        MyCardListRequest myCardListRequest = new MyCardListRequest(DBname.getValue(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication().getBaseContext());
        queue.add(myCardListRequest);
    }
    //이미지 그리기
    private void DrawImage(){
        //이메일 ID Address 분리
        String EmailID, EmailAddress;
        EmailID = Email.substring(0, Email.indexOf("@"));
        EmailAddress = "@" + Email.substring(Email.indexOf("@")+1, Email.length());
        //Get 베이스 이미지 Bitmap
        bitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.card_base_image);
        //새비트맵 생성
        imageBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //캔버스 생성
        Canvas canvas = new Canvas(imageBitmap);
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
        canvas.drawText(Name,200,550, textSize350);
        canvas.drawText(Position,250, 870, textSize200);
        canvas.drawText(PhoneNumber,1950, 530, textSize180);
        canvas.drawText(CompanyNumber, 1950, 770, textSize180);
        canvas.drawText(EmailID, 2000, 955, textSize140);
        canvas.drawText(EmailAddress, 2200, 1100, textSize140);
        canvas.drawText(Company,200, 1630, textSize250);
        canvas.drawText(Address,200, 1800, textSize140);
    }
    //내 카드 리스트 반환
    public LiveData<ArrayList<CardListModel>> getMyCardList() {
        return MyCardList;
    }
}
