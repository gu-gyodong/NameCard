package com.project.namecard.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.namecard.Adapter.NotMineCardRecyclerViewAdapter;
import com.project.namecard.R;
import com.project.namecard.databinding.ActivityMainFragmentCardViewBinding;
import com.project.namecard.models.MainFragmentCardModel;
import com.project.namecard.viewModels.MainFragmentCardViewModel;

import java.util.ArrayList;

public class MainFragmentCardView extends Fragment {

    //뷰
    public View view;
    //바인딩
    private ActivityMainFragmentCardViewBinding binding;
    //뷰 모델
    private MainFragmentCardViewModel viewModel;
    //리사이클러뷰 변수
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_main_fragment_card_view, container, false);
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainFragmentCardViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        //뷰 세팅
        view = binding.getRoot();


        viewModel.StatCardSetting();
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardRegisterView.class);
                startActivity(intent);

            }
        });


//        viewModel.returnBitmap().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
//            @Override
//            public void onChanged(Bitmap bitmap) {
//                binding.MyRepCardImage.setImageBitmap(bitmap);
//            }
//        });


        viewModel.getNotMineCardList().observe(getViewLifecycleOwner(), new Observer<ArrayList<MainFragmentCardModel>>() {
            @Override
            public void onChanged(ArrayList<MainFragmentCardModel> mainFragmentCardModels) {
                RecyclerViewSetting();
            }
        });


        return view;
    }

    //리사이클러뷰 세팅
    public void RecyclerViewSetting(){
        binding.NotMineCardList.setLayoutManager(linearLayoutManager);
        NotMineCardRecyclerViewAdapter notMineCardRecyclerViewAdapter = new NotMineCardRecyclerViewAdapter(viewModel.NotMineCardList);
        binding.NotMineCardList.setAdapter(notMineCardRecyclerViewAdapter);
    }
}