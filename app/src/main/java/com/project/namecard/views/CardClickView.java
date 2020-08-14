package com.project.namecard.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.namecard.R;
import com.project.namecard.databinding.ActivityCardClickViewBinding;
import com.project.namecard.viewModels.CardClickViewModel;
import com.project.namecard.viewModels.LoginViewModel;

import java.security.acl.Owner;
import java.util.ArrayList;

public class CardClickView extends AppCompatActivity {

    //바인딩
    private ActivityCardClickViewBinding binding;
    //뷰 모델
    private CardClickViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivityCardClickViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(CardClickViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //클릭한 카드 ID 가져오기
        Intent intent = getIntent();
        viewModel.ID.setValue(intent.getStringExtra("ID"));
        viewModel.CardID.setValue(intent.getStringExtra("CardID"));
        viewModel.Owner.setValue(intent.getStringExtra("Owner"));

        viewModel.getCardInfo();

        //옵저버
        getObserve();
        //UI 컨트롤
        UIControl();
        //클릭 이벤트
        BtnClickEvent();
    }

    //옵저버
    private void getObserve() {
        //카드 데이터 옵저버
        viewModel.ImageBitmapReturn().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.cardImage.setImageBitmap(bitmap);
            }
        });
        //업데이트 옵저버
        viewModel.ResultReturn().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("UpdateSuccess")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CardClickView.this);
                    builder.setTitle("카드 수정 성공 했습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //액티비티 종료후 메인 재실행
                                    Intent intent = new Intent(CardClickView.this, MainView.class);
                                    finishAffinity();
                                    startActivity(intent);
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("UpdateFail")){
                    Toast.makeText(getApplicationContext(), "카드 수정 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //UI 컨트롤
    public void UIControl() {
        if(viewModel.Owner.getValue().equals("mine")){
            //내 카드일시 수정 버튼 보이게
            binding.CardUpdate.setVisibility(View.VISIBLE);
        }
        else if(viewModel.Owner.getValue().equals("notmine")) {
            //남 카드일시 수정 버튼 안보이게
            binding.CardUpdate.setVisibility(View.GONE);
        }
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
    //클릭 이벤트
    public void BtnClickEvent() {
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.CompanyCall:
                        //권한 요청
                        RequestPermission();
                        //권한 확인
                        int permission1 = ContextCompat.checkSelfPermission(CardClickView.this, Manifest.permission.CALL_PHONE);
                        //권한 확인시 실행
                        if(permission1 != PackageManager.PERMISSION_DENIED) {
                            String ChangeNumber = viewModel.CompanyNumber.getValue().replaceAll("-","");
                            Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ChangeNumber));
                            startActivity(intent1);
                        }
                        break;
                    case R.id.PhoneCall:
                        //권한 요청
                        RequestPermission();
                        //권한 확인
                        int permission2 = ContextCompat.checkSelfPermission(CardClickView.this, Manifest.permission.CALL_PHONE);
                        //권한 확인시 실행
                        if(permission2 != PackageManager.PERMISSION_DENIED) {
                            String ChangeNumber = viewModel.PhoneNumber.getValue().replaceAll("-","");
                            Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ChangeNumber));
                            startActivity(intent2);
                        }
                        break;
                    case R.id.PhoneMessage:
                        //권한 요청
                        RequestPermission();
                        //권한 확인
                        int permission3 = ContextCompat.checkSelfPermission(CardClickView.this, Manifest.permission.CALL_PHONE);
                        //권한 확인시 실행
                        if(permission3 != PackageManager.PERMISSION_DENIED) {
                            String ChangeNumber = viewModel.PhoneNumber.getValue().replaceAll("-","");
                            Intent intent3 = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+ChangeNumber));
                            startActivity(intent3);
                        }
                        break;
                    case R.id.EmailPost:
                        //권한 요청
                        RequestPermission();
                        //권한 확인
                        int permission4 = ContextCompat.checkSelfPermission(CardClickView.this, Manifest.permission.CALL_PHONE);
                        //권한 확인시 실행
                        if(permission4 != PackageManager.PERMISSION_DENIED) {
                            Intent intent4 = new Intent(Intent.ACTION_SEND);
                            intent4.putExtra(Intent.EXTRA_EMAIL, new String[]{viewModel.Email.getValue()});
                            intent4.setType("message/rfc822");
                            startActivity(intent4);
                        }
                        break;
                    case R.id.CardUpdate:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CardClickView.this);
                        builder1.setTitle("카드 수정 하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        binding.name.setEnabled(true);
                                        binding.company.setEnabled(true);
                                        binding.depart.setEnabled(true);
                                        binding.position.setEnabled(true);
                                        binding.companyNumber.setEnabled(true);
                                        binding.phoneNumber.setEnabled(true);
                                        binding.email.setEnabled(true);
                                        binding.faxNumber.setEnabled(true);
                                        binding.address.setEnabled(true);
                                        binding.memo.setEnabled(true);
                                        binding.ButtonLayout.setVisibility(View.GONE);
                                        binding.UpdateClear.setVisibility(View.VISIBLE);
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        AlertDialog alertDialog1 = builder1.create();
                        alertDialog1.show();
                        break;
                    case R.id.UpdateClear:
                        viewModel.CardUpdateEvent();
                        break;
                    case R.id.CardDelete:
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(CardClickView.this);
                        builder2.setTitle("정말 카드 삭제 하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewModel.CardDeleteEvent();
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        AlertDialog alertDialog2 = builder2.create();
                        alertDialog2.show();
                        break;
                }
            }
        };
        binding.CompanyCall.setOnClickListener(listener);
        binding.PhoneCall.setOnClickListener(listener);
        binding.PhoneMessage.setOnClickListener(listener);
        binding.EmailPost.setOnClickListener(listener);
        binding.CardUpdate.setOnClickListener(listener);
        binding.UpdateClear.setOnClickListener(listener);
    }
    //백버튼 이벤트
    @Override
    public void onBackPressed() {
        if (binding.UpdateClear.getVisibility() == View.VISIBLE){
            viewModel.getCardInfo();
            binding.name.setEnabled(false);
            binding.company.setEnabled(false);
            binding.depart.setEnabled(false);
            binding.position.setEnabled(false);
            binding.companyNumber.setEnabled(false);
            binding.phoneNumber.setEnabled(false);
            binding.email.setEnabled(false);
            binding.faxNumber.setEnabled(false);
            binding.address.setEnabled(false);
            binding.memo.setEnabled(false);
            binding.ButtonLayout.setVisibility(View.VISIBLE);
            binding.UpdateClear.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
        }
    }


}