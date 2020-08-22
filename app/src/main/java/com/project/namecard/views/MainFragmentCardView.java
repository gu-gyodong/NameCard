package com.project.namecard.views;

import android.app.Dialog;
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
import com.project.namecard.adapter.CardRecyclerViewAdapter;
import com.project.namecard.databinding.ActivityMainFragmentCardViewBinding;
import com.project.namecard.dialog.CardRegisterDialog;
import com.project.namecard.models.CardListModel;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

import java.util.ArrayList;

public class MainFragmentCardView extends Fragment {

    //뷰
    public View view;
    //바인딩
    private ActivityMainFragmentCardViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;
    //다이얼로그
    private CardRegisterDialog dialog;

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

        //카드 데이터 옵저버
        getDataObserve();
        //클릭 이벤트
        BtnClickEvent();

        return view;
    }

    //카드 데이터 옵저버
    private void getDataObserve(){
        //대표카드 옵저버
        viewModel.getMyRepCard().observe(getViewLifecycleOwner(), new Observer<CardListModel>() {
            @Override
            public void onChanged(CardListModel cardListModel) {
                //태표카드 이미지 세팅
                if(cardListModel == null){
                    binding.MyRepCardImageText.setVisibility(View.VISIBLE);
                    binding.MyRepCardImage.setVisibility(View.GONE);
                }
                else {
                    binding.MyRepCardImageText.setVisibility(View.GONE);
                    binding.MyRepCardImage.setVisibility(View.VISIBLE);
                    binding.MyRepCardImage.setImageBitmap(cardListModel.getCardImage());
                }
            }
        });
        //교환 카드 리스트 옵저버
        viewModel.getNotMineCardList().observe(getViewLifecycleOwner(), new Observer<ArrayList<CardListModel>>() {
            @Override
            public void onChanged(ArrayList<CardListModel> cardListModels) {
                //교환 카드 리스트 이미지 세팅
                CardRecyclerViewAdapter cardRecyclerViewAdapter = new CardRecyclerViewAdapter((ArrayList<CardListModel>) cardListModels);
                binding.NotMineCardList.setAdapter(cardRecyclerViewAdapter);
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
                    case R.id.RepCardSelect:
                        RepCardSelectClick();
                        break;
                    case R.id.CardRegister:
                        dialog = new CardRegisterDialog(getContext());
//                        dialog.setCancelable(true);
                        dialog.show();
                        break;
                }
            }
        };
        binding.MyRepCardImage.setOnClickListener(listener);
        binding.RepCardSelect.setOnClickListener(listener);
        binding.CardRegister.setOnClickListener(listener);
    }
    //내 대표 카드 클릭
    private void MyRepCardClick() {
        Intent intent = new Intent(getContext(), CardClickView.class);
        intent.putExtra("ID", viewModel.ID.getValue());
        intent.putExtra("CardID", viewModel.mainFragmentCardRepository.MyRepCard.getValue().getCardID());
        intent.putExtra("Owner", "mine");
        intent.putExtra("NotList", "true");
        startActivity(intent);
    }
    //내 대표카드 설정
    private void RepCardSelectClick() {
        Intent intent = new Intent(getContext(), MyCardListView.class);
        startActivity(intent);
    }
}