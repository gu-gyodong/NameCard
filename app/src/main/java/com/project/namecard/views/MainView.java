package com.project.namecard.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.namecard.R;
import com.project.namecard.databinding.ActivityMainViewBinding;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

import java.util.ArrayList;

public class MainView extends AppCompatActivity {

    //바인딩
    private ActivityMainViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;
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
        viewModel = new ViewModelProvider(this).get(MainFragmentSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //권한 요청
        RequestPermission();
        //프레그먼트 화면 변경
        SetFragmentView();
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

    //메인 두번 눌러 종료
    private long time = 0;
    @Override
    public void onBackPressed() {
            if(System.currentTimeMillis()-time>=2000){
                time=System.currentTimeMillis();
                Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
            }else if(System.currentTimeMillis()-time<2000){
                ActivityCompat.finishAffinity(this);
            }
    }
}