package com.project.namecard.viewModels;

import android.view.View;
import android.widget.AdapterView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.namecard.connection.RetrofitApi;
import com.project.namecard.models.SignUpModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpViewModel extends ViewModel {

    //뷰 변수
    public MutableLiveData<String> ID = new MutableLiveData<>();
    public MutableLiveData<String> PassWord = new MutableLiveData<>();
    public MutableLiveData<String> PassWordCheck = new MutableLiveData<>();
    public MutableLiveData<String> Name = new MutableLiveData<>();
    public MutableLiveData<String> Birth = new MutableLiveData<>("");
    public MutableLiveData<String> Email = new MutableLiveData<>("");
    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<Integer> AddressSelect = new MutableLiveData<>(0);
    //UI 제어 변수
    public MutableLiveData<String> IDCheck = new MutableLiveData<>("");
    public MutableLiveData<String> SignUpCheck = new MutableLiveData<>("");
    //레트로핏
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
    private RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

    //아이디 중복 확인
    public void IDCheckBtnClick() {
        //아이디 빈값
        if("".equals(ID.getValue()) || null == ID.getValue()){
            IDCheck.setValue("empty");
        }
        //아이디 값 존재
        else{
            retrofitApi.IDCheckRequest(ID.getValue()).enqueue(new Callback<SignUpModel>() {
                @Override
                public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                    //성공
                    if(response.body().getSuccess().equals("true")){
                        //아이디 사용가
                        IDCheck.setValue("true");
                    }
                    else {
                        //아이디 사용 불가능
                        IDCheck.setValue("false");
                    }
                }
                @Override
                public void onFailure(Call<SignUpModel> call, Throwable t) {
                    //실패
                }
            });
        }
    }

    //이메일 주소 스피너 아이템 선택 이벤트
    public void AddressSelectClick(AdapterView<?> parent, View view, int pos, long id) {
        EmailAddress.setValue(parent.getSelectedItem().toString());
        AddressSelect.setValue(pos);
    }

    //회원가입 버튼 클릭
    public void SignUpBtnClick() {
        SignUpCheck.setValue("");
        //아이디 체크 확인
        if(IDCheck.getValue().equals("true")){
            //비밀 번호 일치 확인
            if(!("".equals(PassWord.getValue()) || null == PassWordCheck.getValue()) &&
                    !("".equals(PassWordCheck.getValue()) || null == PassWordCheck.getValue())
                    && PassWord.getValue().equals(PassWordCheck.getValue())){
                //회원가입 정보 빈값 확인
                if(!("".equals(Name.getValue()) || null == Name.getValue()) &&
                        !("".equals(Birth.getValue()) || null == Birth.getValue()) &&
                        !("".equals(Email.getValue()) || null == Email.getValue()) &&
                        !("".equals(EmailAddress.getValue()) || null == EmailAddress.getValue())){
                    //전체 이메일, 생성할 DB명
                    String TotalEmail =  Email.getValue() + "@" + EmailAddress.getValue();
                    String DBname = "BC_user_" + ID.getValue();
                    //회원가입 요청 시작
                    retrofitApi.SignUpRequest(ID.getValue(), PassWord.getValue(),
                            Name.getValue(), Birth.getValue(), TotalEmail, DBname)
                            .enqueue(new Callback<SignUpModel>() {
                                @Override
                                public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                                    //성공
                                    if(response.body().getSuccess().equals("true")){
                                        SignUpCheck.setValue("true");
                                    }
                                    else{
                                        SignUpCheck.setValue("false");
                                    }
                                }
                                @Override
                                public void onFailure(Call<SignUpModel> call, Throwable t) {
                                    //실패
                                }
                            });
                }
                //회원가입 정보 빈값 존재
                else {
                    SignUpCheck.setValue("PleaseNoEmpty");
                }
            }
            //비밀번호 불일치
            else{
                SignUpCheck.setValue("PleasePassWordCheck");
            }
        }
        //아이디 체크 미확인
        else{
            IDCheck.setValue("please");
        }
    }

    //이메일 주소선택 값 반환
    public LiveData<Integer> GetSelectResult() {
        return AddressSelect;
    }

    //아이디 체크 값 반환
    public LiveData<String> GetIDCheckResult() {
        return IDCheck;
    }

    //로그인 체크 값 반환
    public LiveData<String> GetSignUpCheckResult() {
        return SignUpCheck;
    }

}
