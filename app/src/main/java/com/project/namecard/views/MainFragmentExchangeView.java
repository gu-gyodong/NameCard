package com.project.namecard.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.namecard.R;
import com.project.namecard.databinding.ActivityMainFragmentExchangeViewBinding;
import com.project.namecard.databinding.ActivityMainFragmentInfoViewBinding;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

public class MainFragmentExchangeView extends Fragment {

    //뷰
    View view;
    //바인딩
    private ActivityMainFragmentExchangeViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;

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


        return view;
    }
}