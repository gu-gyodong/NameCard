package com.project.namecard.views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.namecard.R;
import com.project.namecard.adapter.CardRecyclerViewAdapter;
import com.project.namecard.databinding.ActivityMyCardListViewBinding;
import com.project.namecard.models.CardListModel;
import com.project.namecard.viewModels.MyCardListViewModel;

import java.util.ArrayList;

public class MyCardListView extends AppCompatActivity {

    //바인딩
    private ActivityMyCardListViewBinding binding;
    //뷰모델
    private MyCardListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card_list_view);

        //데이터 바인딩
        binding = ActivityMyCardListViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MyCardListViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        //리사이클러뷰 메니저 세팅
        binding.MyCardList.setLayoutManager(new LinearLayoutManager(this));

        //카드 데이터 옵저버
        getDataObserve();
    }

    private void getDataObserve() {
        viewModel.getMyCardList().observe(this, new Observer<ArrayList<CardListModel>>() {
            @Override
            public void onChanged(ArrayList<CardListModel> repCardSelectModels) {
                if(repCardSelectModels != null){
                    binding.MyCardList.setVisibility(View.VISIBLE);
                    binding.MyCardListText.setVisibility(View.GONE);
                    //교환 카드 리스트 이미지 세팅
                    CardRecyclerViewAdapter cardRecyclerViewAdapter = new CardRecyclerViewAdapter(repCardSelectModels);
                    binding.MyCardList.setAdapter(cardRecyclerViewAdapter);
                }
                else {
                    binding.MyCardList.setVisibility(View.GONE);
                    binding.MyCardListText.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}