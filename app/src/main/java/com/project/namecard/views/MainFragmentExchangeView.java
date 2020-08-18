package com.project.namecard.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.project.namecard.R;
import com.project.namecard.databinding.ActivityMainFragmentExchangeViewBinding;
import com.project.namecard.databinding.ActivityMainFragmentInfoViewBinding;
import com.project.namecard.dialog.CardRegisterDialog;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

public class MainFragmentExchangeView extends Fragment {

    //뷰
    View view;
    //바인딩
    private ActivityMainFragmentExchangeViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;
    //내 대표 카드 ID
    private String MyRepCardID;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_main_fragment_exchange_view, container, false);
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainFragmentSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        //뷰 세팅
        view = binding.getRoot();

        //데이터 옵저버
        getDataObserver();

        //클릭 이벤트
        BtnClickEvent();

        return view;
    }

    //데이터 옵저버
    private void getDataObserver() {
        viewModel.getMyRepCardID().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("")){
                    //대표 카드 없을시
                    binding.QRCodeImageText.setVisibility(View.VISIBLE);
                    binding.QRCodeImage.setVisibility(View.GONE);
                    //이미지 삭제
                    binding.QRCodeImage.setImageBitmap(null);
                }
                else {
                    //대표 카드 있을시
                    binding.QRCodeImageText.setVisibility(View.GONE);
                    binding.QRCodeImage.setVisibility(View.VISIBLE);
                    //QR코드 세팅
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try{
                        //대표카드 아이디 + DB 명
                        BitMatrix bitMatrix = multiFormatWriter.encode(viewModel.DBname.getValue()+"/"+s, BarcodeFormat.QR_CODE, 600, 600);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        //이미지 세팅
                        binding.QRCodeImage.setImageBitmap(bitmap);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //클릭 이벤트


    private void BtnClickEvent() {
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.QRCodeScan:
                        Intent intent = new Intent(getActivity(), QRCodeExchangeView.class);
                        startActivity(intent);
                        break;
                    case R.id.NFCExchange:
                        break;
                }
            }
        };
        binding.QRCodeScan.setOnClickListener(listener);
        binding.NFCExchange.setOnClickListener(listener);
    }

}