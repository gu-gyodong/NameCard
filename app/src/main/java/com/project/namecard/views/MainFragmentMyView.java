package com.project.namecard.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.namecard.R;
import com.project.namecard.databinding.ActivityMainFragmentMyViewBinding;
import com.project.namecard.viewModels.MainFragmentMyViewModel;
import com.project.namecard.viewModels.MainViewModel;

public class MainFragmentMyView extends Fragment {

    //뷰
    public View view;
    //바인딩
    private ActivityMainFragmentMyViewBinding binding;
    //뷰 모델
    private MainFragmentMyViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_main_fragment_my_view, container, false);
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainFragmentMyViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        //뷰 세팅
        view = binding.getRoot();

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardRegisterView.class);
                startActivity(intent);

            }
        });


        return view;
    }
}