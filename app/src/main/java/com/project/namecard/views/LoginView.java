package com.project.namecard.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.namecard.databinding.ActivityLoginViewBinding;
import com.project.namecard.models.LoginModel;
import com.project.namecard.viewModels.LoginViewModel;

public class LoginView extends AppCompatActivity {

    //바인딩
    private ActivityLoginViewBinding binding;
    //뷰 모델
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivityLoginViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //키보드 처리
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //로그인 결과 옵저버
        LoginSuccessObserve();
    }
    //로그인 결과 옵저버
    public void LoginSuccessObserve(){
        viewModel.getLoginResult().observe(this, new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                if(loginModel.getSuccess().equals("true")){
                    Intent intent = new Intent(LoginView.this, MainView.class);
                    startActivity(intent);
                    finish();
                }
                else if(loginModel.getSuccess().equals("false")){
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SignUpBtnClick(View view) {
        Intent intent = new Intent(this, SignUpView.class);
        startActivity(intent);
    }
}