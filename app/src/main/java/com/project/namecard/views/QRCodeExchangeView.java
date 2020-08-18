package com.project.namecard.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.namecard.databinding.ActivityQRCodeExchangeViewBinding;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

public class QRCodeExchangeView extends AppCompatActivity {

    //바인딩
    private ActivityQRCodeExchangeViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivityQRCodeExchangeViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainFragmentSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //QR코드 스캐너
        new IntentIntegrator(this).initiateScan();

        getDataObserver();
    }

    private void getDataObserver() {
        viewModel.GetExchangeResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("success")){
                    //액티비티 종료후 메인 재실행
                    Intent intent = new Intent(getApplicationContext(), MainView.class);
                    finishAffinity();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result !=null){
            if(result.getContents()==null){
                Toast.makeText(this,"취소되었습니다.",Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Toast.makeText(this,"결과 : "+ result.getContents() ,Toast.LENGTH_LONG).show();
                //읽은 상댇방 카드 ID set
                String total = result.getContents();
                String UserDB = total.substring(0, total.indexOf("/"));
                String UserID = total.substring(total.indexOf("/")+1);
                //DataSet
                viewModel.UserDB.setValue(UserDB);
                viewModel.UserID.setValue(UserID);
                viewModel.QRCodeExchange();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}