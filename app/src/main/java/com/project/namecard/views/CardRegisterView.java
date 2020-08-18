package com.project.namecard.views;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.namecard.R;
import com.project.namecard.connection.CardAddressRequest;
import com.project.namecard.databinding.ActivityCardRegisterViewBinding;
import com.project.namecard.viewModels.CardRegisterViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;

public class CardRegisterView extends AppCompatActivity {

    //바인딩
    private ActivityCardRegisterViewBinding binding;
    //뷰 모델
    private CardRegisterViewModel viewModel;
    //이미지 추가 다이얼로그 변수
    LayoutInflater imageMenuInflater;
    View imageMenuLayout;
    Dialog imageMenuDialog;
    ImageView ImageAdd, ImageDelete;
    Button ImageMenuCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivityCardRegisterViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(CardRegisterViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //카드 주인 Get
        Intent intent = getIntent();
        viewModel.Owner.setValue(intent.getStringExtra("Owner"));

        //카등 등록 정보 옵저버
        CardRegisterDataObserve();
    }

    //카등 등록 정보 옵저버
    private void CardRegisterDataObserve() {
        //이메일 주소 선택 옵저버
        viewModel.GetSelectResult().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer == 0 ) {
                    //초기 상태
                    binding.emailAddress.setEnabled(false);
                    binding.emailAddress.setText(null);
                    binding.emailAddress.setHint("선택 하세요");
                }
                else if(integer == 6) {
                    //직접 입력 선택
                    binding.emailAddress.setVisibility(View.VISIBLE);
                    binding.emailAddressSpinner.setVisibility(View.GONE);
                    binding.emailAddress.setEnabled(true);
                    binding.emailAddress.setText("");
                    binding.emailAddress.setHint("");
                }
                else {
                    //기본 세팅 선택
                    binding.emailAddress.setEnabled(true);
                    binding.emailAddress.setHint("");
                }
            }
        });
        //카드 이미지 옵저버
        viewModel.GetCardImageBitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if(bitmap == null){
                    binding.cardImage.setVisibility(View.GONE);
                    binding.cardImageText.setVisibility(View.VISIBLE);
                }
                else{
                    binding.cardImage.setVisibility(View.VISIBLE);
                    binding.cardImageText.setVisibility(View.GONE);
                    binding.cardImage.setImageBitmap(bitmap);
                }
            }
        });
        //카드등록 결과 옵저버
        viewModel.GetCardRegisterResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("pleaseNoEmpty")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardRegisterView.this);
                    builder.setTitle("빈칸이 있습니다\n다시한번 확인해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("true")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardRegisterView.this);
                    builder.setTitle("카드 등록에 성공 했습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("false")){
                    Toast.makeText(getApplicationContext(),"카드등록 실패 했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //주소 선택 이벤트
    public void addressSelectClickEvent(View view) {
        Intent intent = new Intent(CardRegisterView.this, CardAddressRequest.class);
                startActivityForResult(intent, 200);
    }

    //카드 이미지 메뉴
    public void CardImageMenuClickEvent(View view) {

        //권한 요청
        RequestPermission();

        //다이얼로그 레이아웃 설정
        imageMenuInflater = LayoutInflater.from(CardRegisterView.this);
        imageMenuLayout = imageMenuInflater.inflate(R.layout.activity_card_image_menu, null);
        //다이얼로그 show
        imageMenuDialog = new Dialog(CardRegisterView.this);
        imageMenuDialog.setContentView(imageMenuLayout);
        imageMenuDialog.show();
        //다이얼로그 이미지, 버튼
        ImageAdd = imageMenuLayout.findViewById(R.id.ImageAdd);
        ImageDelete = imageMenuLayout.findViewById(R.id.ImageDelete);
        ImageMenuCancel = imageMenuLayout.findViewById(R.id.ImageMenuCancel);

        //이미지 Add 이벤트
        ImageAdd.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cropper 라이브러리
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(CardRegisterView.this);
            }
        });
        //이미지 Delete 이벤트
        ImageDelete.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.CardImageDeleteClick();
                imageMenuDialog.dismiss();
            }
        });
        //다이얼로그 캔슬
        ImageMenuCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageMenuDialog.dismiss();
            }
        });
    }

    //권한 요청
    public void RequestPermission() {
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ArrayList<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    //엑티비티 결과값 받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //주소 받기
        if(requestCode == 200 && resultCode == 201){
            String addressText = data.getExtras().getString("addressText");
            binding.address.setText(addressText);
        }
        //크로퍼 이미지 가져오기
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //이미지 URI 가져오기
                Uri uri = result.getUri();
                try {
                    //URI 에서 Bitmap Get
                    viewModel.CardImageBitmap.setValue(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageMenuDialog.dismiss();
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                imageMenuDialog.dismiss();
            }
        }
    }

    //취소 버튼 클릭
    public void CancelBtnClick(View view) {
        finish();
    }
}