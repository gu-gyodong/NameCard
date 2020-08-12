package com.project.namecard.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.namecard.R;
import com.project.namecard.databinding.ActivityMainViewBinding;
import com.project.namecard.viewModels.MainViewModel;

import java.util.ArrayList;

public class MainView extends AppCompatActivity {

    //바인딩
    private ActivityMainViewBinding binding;
    //뷰 모델
    private MainViewModel viewModel;
    //프레그먼트 변수
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MainFragmentCardView mainFragmentCardView = new MainFragmentCardView();;
    private MainFragmentExchangeView mainFragmentExchangeView = new MainFragmentExchangeView();
    private MainFragmentInfoView mainFragmentInfoView = new MainFragmentInfoView();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivityMainViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //권한 요청
        RequestPermission();
        //프레그먼트 화면 변경
        SetFragmentView();



        //나중에 상단 메뉴로 옮겨야함
        {
            binding.userInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainView.this, UserInfoView.class);
                    startActivity(intent);
                }
            });
            binding.LogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainView.this);
                    builder.setTitle("로그 아웃 하겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences Auto = getSharedPreferences("user", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = Auto.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(MainView.this, LoginView.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }

    //권한 요청
    private void RequestPermission() {
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
    //프레그먼트 화면 설정
    private void SetFrag(int n){
        //프레그먼트 전환 설정
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        //프레그먼트 전환
        switch (n){
            case 0:
                fragmentTransaction.replace(R.id.mainFrame, mainFragmentCardView).commit();
                mainFragmentCardView.setArguments(bundle);
                break;
            case 1:
                fragmentTransaction.replace(R.id.mainFrame, mainFragmentExchangeView).commit();
                mainFragmentExchangeView.setArguments(bundle);
                break;
            case 2:
                fragmentTransaction.replace(R.id.mainFrame, mainFragmentInfoView).commit();
                mainFragmentInfoView.setArguments(bundle);
                break;
        }
    }
    //프레그먼트 화면 변경
    private void SetFragmentView() {
        SetFrag(0);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.Card:
                        SetFrag(0);
                        break;
                    case R.id.Exchange:
                        SetFrag(1);
                        break;
                    case R.id.Info:
                        SetFrag(2);
                        break;
                }
                return false;
            }
        });
    }
}