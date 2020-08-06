package com.project.namecard.views;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.namecard.R;
import com.project.namecard.databinding.ActivityUserInfoViewBinding;
import com.project.namecard.viewModels.UserInfoViewModel;

import java.util.Calendar;

public class UserInfoView extends AppCompatActivity {

    //바인딩
    private ActivityUserInfoViewBinding binding;
    //뷰 모델
    private UserInfoViewModel viewModel;
    //데이트픽커 변수
    private Calendar cal = Calendar.getInstance();
    private int y = cal.get(Calendar.YEAR), m = cal.get(Calendar.MONTH)+1, d = cal.get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivityUserInfoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //기본 유저 정보 세팅
        viewModel.GetUserInfo();
        //옵저버
        GetStateObserve();
    }

    //옵저버
    public void GetStateObserve() {
        //UI상태 변환 옵저버
        viewModel.GetUIStateText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("state1")){
                    binding.id.setEnabled(false);
                    binding.password.setEnabled(false);
                    binding.password.setVisibility(View.GONE);
                    binding.name.setEnabled(false);
                    binding.birth.setEnabled(false);
                    binding.email.setEnabled(false);
                    binding.emailaddress.setEnabled(false);
                    binding.BirthSelect.setVisibility(View.INVISIBLE);
                    binding.state1.setVisibility(View.VISIBLE);
                    binding.state2.setVisibility(View.GONE);
                }
                else if(s.equals("state2")){
                    binding.id.setEnabled(true);
                    binding.password.setEnabled(true);
                    binding.password.setVisibility(View.VISIBLE);
                    binding.password.setInputType(InputType.TYPE_CLASS_TEXT);
                    binding.name.setEnabled(true);
                    binding.birth.setEnabled(true);
                    binding.email.setEnabled(true);
                    binding.emailaddress.setEnabled(true);
                    binding.BirthSelect.setVisibility(View.VISIBLE);
                    binding.state1.setVisibility(View.GONE);
                    binding.state2.setVisibility(View.VISIBLE);
                }
            }
        });

        //유저 정보 확인 옵저버
        viewModel.GetUPDateText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("pleaseNoBlank")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoView.this);
                    builder.setTitle("빈칸을 확인해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("updateSuccess")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoView.this);
                    builder.setTitle("회원 정보를 수정 하였습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    viewModel.GetUserInfo();
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("deleteSuccess")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoView.this);
                    builder.setTitle("회원 탈퇴 하였습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //자동로그인 정보 delete
                                    viewModel.UserDeleteSuccess();
                                    //앱 재실행 로그인 화면
                                    Intent intent = new Intent(UserInfoView.this, LoginView.class);
                                    finishAffinity();
                                    startActivity(intent);
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

    }

    //생일 선택
    public void BirthSelectImgClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerMode, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year; m = month + 1; d = dayOfMonth;
                viewModel.Birth.setValue(y + "-" + m + "-" + d);
            }
        }, y,m-1, d);
        datePickerDialog.show();
    }

    //회원 수정 선택
    public void BtnUserUpdateClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoView.this);
        final EditText et = new EditText(UserInfoView.this);
        builder.setTitle("본인확인")
                .setMessage("비밀 번호를 입력해주세요")
                .setView(et)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = et.getText().toString();
                        if(s.equals(viewModel.PassWord.getValue())){
                            //UI 상태 변경
                            viewModel.UIStateText.setValue("state2");
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"비밀 번호 불일치", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //회원 삭제 선택
    public void BtnUSerDeleteClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoView.this);
        builder.setTitle("정말 회원 탈퇴 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.BtnUSerDeleteRequest();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}