package com.project.namecard.adapter;

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
import com.project.namecard.views.CardClickView;

import java.util.ArrayList;
import java.util.List;

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

    //리스트 대입
    private ArrayList<MainFragmentCardModel> Cardlist;
    public NotMineCardRecyclerViewAdapter(ArrayList<MainFragmentCardModel> Cardlist){
        this.Cardlist = Cardlist;
    }

    //뷰 연결
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
        holder.recyclerName.setText(Cardlist.get(i).getName());
        holder.recyclerCompany.setText(Cardlist.get(i).getCompany());
        holder.recyclerImage.setImageBitmap(Cardlist.get(i).getCardImage());
        //뷰 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //리사이클러뷰 선택식 보낼 CardID
                CardID = Cardlist.get(i).getCardID();
                ID = Cardlist.get(i).getID();
                Owner = Cardlist.get(i).getOwner();
                Intent intent = new Intent(v.getContext(), CardClickView.class);
                intent.putExtra("ID", ID);
                intent.putExtra("CardID", CardID);
                intent.putExtra("Owner", Owner);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Cardlist.size();
    }
}
