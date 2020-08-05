package com.project.namecard.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.namecard.R;
import com.project.namecard.databinding.ActivitySignUpViewBinding;
import com.project.namecard.viewModels.SignUpViewModel;

import java.util.Calendar;

public class SignUpView extends AppCompatActivity {

    //바인딩
    private ActivitySignUpViewBinding binding;
    //뷰 모델
    private SignUpViewModel viewModel;

    //데이트픽커 변수
    private Calendar cal = Calendar.getInstance();
    private int y = cal.get(Calendar.YEAR), m = cal.get(Calendar.MONTH)+1, d = cal.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivitySignUpViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //회원 가입 정보 옵저버
        UserInfoObserve();
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

    //회원 가입 정보 옵저버
    public void UserInfoObserve() {
        //이메일 주소 선택 옵저버
        viewModel.GetSelectResult().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer == 0 ) {
                    //초기 상태
                    binding.emailaddress.setEnabled(false);
                    binding.emailaddress.setText(null);
                    binding.emailaddress.setHint("선택 하세요");
                }
                else if(integer == 6) {
                    //직접 입력 선택
                    binding.emailaddress.setVisibility(View.VISIBLE);
                    binding.emailaddress.setEnabled(true);
                    binding.emailaddress.setText("");
                    binding.emailaddress.setHint("");
                }
                else {
                    //기본 세팅 선택
                    binding.emailaddress.setEnabled(true);
                    binding.emailaddress.setHint("");
                }
            }
        });
        //아이디 체크 옵저버
        viewModel.GetIDCheckResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("true")){
                    //아이디 사용 가능 다이얼로그
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpView.this);
                    builder.setTitle("사용 가능한 아이디입니다").setMessage("사용 하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //아이디 사용시 변경 불가능
                                    binding.id.setEnabled(false);
                                    binding.IDCheck.setEnabled(false);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    viewModel.IDCheck.setValue("");
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("false")) {
                    //아이디 사용 불가능시
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpView.this);
                    builder.setTitle("이미 사용중인 아이디 입니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("empty")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpView.this);
                    builder.setTitle("아이디를 입력 하세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("please")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpView.this);
                    builder.setTitle("아이디 중복 확인 해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        //비밀번호 체크 옵저버
        viewModel.GetSignUpCheckResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("PleasePassWordCheck")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpView.this);
                    builder.setTitle("비밀번호가 일치하지 않습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("PleaseNoEmpty")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpView.this);
                    builder.setTitle("빈칸 없이 입력하세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(s.equals("true")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpView.this);
                    builder.setTitle("회원 가입 성공 \n 로그인 해주세요")
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpView.this);
                    builder.setTitle("로그인 실패 \n 네트워크 또는 스마트폰 상태를 확인해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    //취소 버튼 클릭
    public void CancelBtnClick(View view) {
        finish();
    }
}