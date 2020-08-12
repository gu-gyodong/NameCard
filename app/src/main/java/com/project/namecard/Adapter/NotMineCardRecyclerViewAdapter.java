package com.project.namecard.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.project.namecard.R;
import com.project.namecard.models.MainFragmentCardModel;

import java.util.ArrayList;

public class NotMineCardRecyclerViewAdapter extends RecyclerView.Adapter<NotMineCardRecyclerViewAdapter.MyViewHolder> {

    private String CardID, ID, Owner;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView recyclerImage;
        TextView recyclerName, recyclerCompany;
        //뷰 연결
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerImage = itemView.findViewById(R.id.recyclerImage);
            recyclerName = itemView.findViewById(R.id.recyclerName);
            recyclerCompany = itemView.findViewById(R.id.recyclerCompany);
        }
    }
    private LiveData<ArrayList<MainFragmentCardModel>> notMineCardList;
    public NotMineCardRecyclerViewAdapter(LiveData<ArrayList<MainFragmentCardModel>> notMineCardList){
        this.notMineCardList = notMineCardList;
    }
    @NonNull
    @Override
    public NotMineCardRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.not_mine_card_recyclerview_adapter, viewGroup, false);
        return new MyViewHolder(v);
    }
    //리사이클러뷰 대입
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        //뷰 세팅
        holder.recyclerName.setText(notMineCardList.getValue().get(i).getName());
        holder.recyclerCompany.setText(notMineCardList.getValue().get(i).getCompany());
        holder.recyclerImage.setImageBitmap(notMineCardList.getValue().get(i).getCardImage());
        //뷰 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //리사이클러뷰 선택식 보낼 CardID
                CardID = notMineCardList.getValue().get(i).getCardID();
                ID = notMineCardList.getValue().get(i).getID();
                Owner = notMineCardList.getValue().get(i).getOwner();
////                Intent intent = new Intent(v.getContext(), CardClickView.class);
//                intent.putExtra("ID", ID);
//                intent.putExtra("CardID", CardID);
//                intent.putExtra("Owner", Owner);
//                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return notMineCardList.getValue().size();
    }
}
