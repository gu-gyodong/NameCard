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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.namecard.R;
import com.project.namecard.adapter.NotMineCardRecyclerViewAdapter;
import com.project.namecard.databinding.ActivityMainFragmentCardViewBinding;
import com.project.namecard.models.MainFragmentCardModel;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

import java.util.ArrayList;

public class MainFragmentCardView extends Fragment {

    //뷰
    public View view;
    //바인딩
    private ActivityMainFragmentCardViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //데이터 바인딩
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_main_fragment_card_view, container, false);
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainFragmentSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        //뷰 세팅
        view = binding.getRoot();
        //리사이클러뷰 메니저 세팅
        binding.NotMineCardList.setLayoutManager(new LinearLayoutManager(getContext()));

        //버튼 클릭 이벤트
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardRegisterView.class);
                startActivity(intent);
            }
        });

        //카드 데이터 옵저버
        getDataObserve();
        //클릭 이벤트
        BtnClickEvent();

        return view;
    }

    //카드 데이터 옵저버
    private void getDataObserve(){
        //대표카드 옵저버
        viewModel.getMyRepCard().observe(getViewLifecycleOwner(), new Observer<MainFragmentCardModel>() {
            @Override
            public void onChanged(MainFragmentCardModel mainFragmentCardModel) {
                //태표카드 이미지 세팅
                binding.MyRepCardImage.setImageBitmap(mainFragmentCardModel.getCardImage());
            }
        });
        //교환 카드 리스트 옵저버
        viewModel.getNotMineCardList().observe(getViewLifecycleOwner(), new Observer<ArrayList<MainFragmentCardModel>>() {
            @Override
            public void onChanged(ArrayList<MainFragmentCardModel> mainFragmentCardModels) {
                //교환 카드 리스트 이미지 세팅
                NotMineCardRecyclerViewAdapter notMineCardRecyclerViewAdapter = new NotMineCardRecyclerViewAdapter((ArrayList<MainFragmentCardModel>) mainFragmentCardModels);
                binding.NotMineCardList.setAdapter(notMineCardRecyclerViewAdapter);
            }
        });
    }
    //클릭 이벤트
    private void BtnClickEvent() {
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.MyRepCardImage:
                        MyRepCardClick();
                        break;
                }
            }
        };
        binding.MyRepCardImage.setOnClickListener(listener);
    }
    //내 대표 카드 클릭
    private void MyRepCardClick() {
        Intent intent = new Intent(getContext(), CardClickView.class);
        intent.putExtra("ID", viewModel.ID.getValue());
        intent.putExtra("CardID", viewModel.mainFragmentCardRepository.MyRepCard.getValue().getCardID());
        intent.putExtra("Owner", "mine");
        startActivity(intent);
    }
}