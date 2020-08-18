package com.project.namecard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.project.namecard.R;
import com.project.namecard.views.CardRegisterView;

public class CardRegisterDialog extends Dialog implements View.OnClickListener {

    //컨텍스트
    private Context context;
    //버튼 변수
    private Button MyCardRegister, NotMineCardRegister;

    public CardRegisterDialog(@NonNull Context context) {
        super(context);

        //컨텍스트
        this.context = context;

        //xml set
        setContentView(R.layout.card_register_dialog);

        //버튼
        MyCardRegister = findViewById(R.id.MyCardRegister);
        NotMineCardRegister = findViewById(R.id.NotMineCardRegister);
        //클릭 이벤트
        MyCardRegister.setOnClickListener(this);
        NotMineCardRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.MyCardRegister:
                Intent intent1 = new Intent(context, CardRegisterView.class);
                intent1.putExtra("Owner", "mine");
                getContext().startActivity(intent1);
                dismiss();
                break;
            case R.id.NotMineCardRegister:
                Intent intent2 = new Intent(context, CardRegisterView.class);
                intent2.putExtra("Owner", "notmine");
                getContext().startActivity(intent2);
                dismiss();
                break;
        }
    }
}
