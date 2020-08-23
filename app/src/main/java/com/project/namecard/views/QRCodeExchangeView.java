package com.project.namecard.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.namecard.R;
import com.project.namecard.databinding.ActivityNfcExchangeViewBinding;
import com.project.namecard.databinding.ActivityQrCodeExchangeViewBinding;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

public class QRCodeExchangeView extends AppCompatActivity {

    //바인딩
    private ActivityQrCodeExchangeViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivityQrCodeExchangeViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainFragmentSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //QR코드 리더
        new IntentIntegrator(this).initiateScan();
    }

    //QR코드 교환 결과 result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result !=null){
            //취소시
            if(result.getContents()==null){
                Toast.makeText(this,"취소되었습니다.",Toast.LENGTH_LONG).show();
                finish();
            }
            //교환 값 존재
            else {
                //읽은 상댇방 카드 ID set
                String total = result.getContents();
                String UserDB = total.substring(0, total.indexOf("/"));
                String UserID = total.substring(total.indexOf("/")+1);
                //Data Set
                viewModel.UserDB.setValue(UserDB);
                viewModel.UserID.setValue(UserID);
                viewModel.CardExchange();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}