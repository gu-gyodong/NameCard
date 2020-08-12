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
import com.project.namecard.Connection.MainFragmentCardRequest;
import com.project.namecard.R;
import com.project.namecard.models.MainFragmentCardModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentCardViewModel extends AndroidViewModel {
    //뷰 변수
    public MutableLiveData<String> ID = new MutableLiveData<>();
    public MutableLiveData<MainFragmentCardModel> MyRepCard = new MutableLiveData<>();//내 대표 카드
    public MutableLiveData<ArrayList<MainFragmentCardModel>> NotMineCardList = new MutableLiveData<>();//교환한 카드 전체 리스트
    public ArrayList<MainFragmentCardModel> NotMineCard = new ArrayList<>();
    //디비 변수
    public String DBname;

    //이미지 세팅 변수
    private String CardID, Name, Company, CardImage, Position, PhoneNumber, CompanyNumber, Address, Email, Owner;
    private Bitmap CardImageBitmap = null, bitmap = null, imageBitmap = null;



    //초기값 세팅
    public MainFragmentCardViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences Auto = getApplication().getSharedPreferences("user", Activity.MODE_PRIVATE);
        ID.setValue(Auto.getString("ID", null));
        DBname = Auto.getString("DBname", null);

        StatCardSetting();
    }

    //카드 리스트 초기 세팅
    public void StatCardSetting(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    //결과 값 받기
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for(int i=0 ; i<jsonArray.length() ; i++){
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

                        /////////카드 세팅
                        //내 대표카드
                        if(item.getString("Owner").equals("mine") && item.getString("Rep").equals("rep")){
                            //이미지 값 받기
                            String imageText = item.getString("CardImage");
                            //이미지 값 null
                            if(imageText.equals("")){
                                //카드 정보에 따른 비트맵 이미지 생성
                                DrawImage();
                                CardImageBitmap = imageBitmap;
                            }
                            //이미지 not null
                            else {
                                //카드 이미지 string -> bitmap
                                byte[] imageBytes = Base64.decode(item.getString("CardImage"), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.length);
                                //비트맵 대입
                                CardImageBitmap = bitmap;
                            }
                            //데이터 set
                            MyRepCard.setValue(new MainFragmentCardModel(Name, Company, CardImageBitmap, CardID, ID.getValue(), Owner));
                        }
                        //교환한 카드들 -> 내카드 아닌것들
                        else if(item.getString("Owner").equals("notmine")){
                            //카드 이미지 string -> bitmap
                            byte[] imageBytes = Base64.decode(item.getString("CardImage"), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.length);
                            //데이터 set
                            NotMineCard.add(new MainFragmentCardModel(Name, Company, CardImageBitmap, CardID, ID.getValue(), Owner));
                        }
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                //교환한 카드 리스트 -> 라이브데이터 set
                NotMineCardList.setValue(NotMineCard);
            }
        };
        MainFragmentCardRequest mainFragmentCardRequest = new MainFragmentCardRequest(DBname, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication().getBaseContext());
        queue.add(mainFragmentCardRequest);
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

//    public LiveData<Bitmap> returnBitmap(){
//        return CardImageBitmap;
//    }

    public LiveData<ArrayList<MainFragmentCardModel>> getNotMineCardList(){
        return NotMineCardList;
    }
}
