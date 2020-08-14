package com.project.namecard.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.namecard.R;
import com.project.namecard.databinding.ActivityMainFragmentInfoViewBinding;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

import java.util.Calendar;

public class MainFragmentInfoView extends Fragment {

    //뷰
    View view;
    //바인딩
    private ActivityMainFragmentInfoViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;

    //데이트픽커 변수
    private Calendar cal = Calendar.getInstance();
    private int y = cal.get(Calendar.YEAR), m = cal.get(Calendar.MONTH)+1, d = cal.get(Calendar.DAY_OF_MONTH);


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_main_fragment_info_view, container, false);
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainFragmentSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        //뷰 세팅
        view = binding.getRoot();

        //유저 정보 get
        viewModel.GetUserInfo();
        //옵저버
        GetStateObserve();
        //클릭 이벤트
        BtnClickEvent();

        return view;
    }

    //옵저버
    public void GetStateObserve() {
        //UI상태 변환 옵저버
        viewModel.GetUIStateText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("state1")) {
                    binding.id.setEnabled(false);
                    binding.password.setEnabled(false);
                    binding.password.setVisibility(View.GONE);
                    binding.PassWordText.setVisibility(View.GONE);
                    binding.name.setEnabled(false);
                    binding.birth.setEnabled(false);
                    binding.email.setEnabled(false);
                    binding.emailaddress.setEnabled(false);
                    binding.BirthSelect.setVisibility(View.INVISIBLE);
                    binding.state1.setVisibility(View.VISIBLE);
                    binding.state2.setVisibility(View.GONE);
                } else if (s.equals("state2")) {
                    binding.id.setEnabled(true);
                    binding.password.setEnabled(true);
                    binding.password.setVisibility(View.VISIBLE);
                    binding.PassWordText.setVisibility(View.VISIBLE);
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
        viewModel.GetUPDateText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("pleaseNoBlank")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("빈칸을 확인해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if (s.equals("updateSuccess")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("회원 정보를 수정 하였습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    viewModel.GetUserInfo();
                                    viewModel.UIStateText.setValue("state1");
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if (s.equals("deleteSuccess")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("회원 탈퇴 하였습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //자동로그인 정보 deleteimage
                                    viewModel.UserDeleteSuccess();
                                    //앱 재실행 로그인 화면
                                    Intent intent = new Intent(getContext(), LoginView.class);
                                    getActivity().finishAffinity();
                                    startActivity(intent);
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if (s.equals("LogOutSuccess")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("로그 아웃 하였습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //자동로그인 정보 deleteimage
                                    viewModel.UserDeleteSuccess();
                                    //앱 재실행 로그인 화면
                                    Intent intent = new Intent(getContext(), LoginView.class);
                                    getActivity().finishAffinity();
                                    startActivity(intent);
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

    }
    //클릭 이벤트
    private void BtnClickEvent() {
        final View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case  R.id.BtnUserUpdate:
                        BtnUserUpdateClick();
                        break;
                    case  R.id.BirthSelect:
                        BirthSelectImgClick();
                        break;
                    case  R.id.BtnUserDelete:
                        BtnUSerDeleteClick();
                        break;
                }
            }
        };
        binding.BtnUserUpdate.setOnClickListener(listener);
        binding.BirthSelect.setOnClickListener(listener);
        binding.BtnUserDelete.setOnClickListener(listener);
    }
    //회원 수정 선택
    public void BtnUserUpdateClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText et = new EditText(getContext());
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
                            Toast.makeText(getContext(),"비밀 번호 불일치", Toast.LENGTH_SHORT).show();
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
    //생일 선택
    public void BirthSelectImgClick() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePickerMode, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year; m = month + 1; d = dayOfMonth;
                viewModel.Birth.setValue(y + "-" + m + "-" + d);
            }
        }, y,m-1, d);
        datePickerDialog.show();
    }
    //회원 삭제 선택
    public void BtnUSerDeleteClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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