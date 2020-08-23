package com.project.namecard.views;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.namecard.databinding.ActivityNfcExchangeViewBinding;
import com.project.namecard.viewModels.MainFragmentSharedViewModel;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class NFCExchangeView extends AppCompatActivity {

    //바인딩
    private ActivityNfcExchangeViewBinding binding;
    //뷰 모델
    private MainFragmentSharedViewModel viewModel;
    //NFC
    private NfcAdapter nfcAdapter;
    private NdefMessage ndefMessage;
    private String UserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩
        binding = ActivityNfcExchangeViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //뷰 모델
        viewModel = new ViewModelProvider(this).get(MainFragmentSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //데이터 옵저버
        getDataObserver();
    }

    //데이터 옵저버
   private void getDataObserver() {
        //대표카드 반환 NFC Ndef 만들기
        viewModel.getMyRepCardID().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //대표 카드 없을시
                if(s.equals("")){
                }
                //대표 카드 있을시
                else{
                    //유저 정보
                    String Info = viewModel.DBname.getValue()+"/"+s;
                    //ndef 메시지 생성
                    ndefMessage = createNdefMessage(Info);
                    nfcAdapter.setNdefPushMessage(ndefMessage, NFCExchangeView.this);

                }
            }
        });
        //교환 결과 반환
        viewModel.GetExchangeResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("success")){
                    //액티비티 종료후 메인 재실행
                    Intent intent = new Intent(getApplicationContext(), MainView.class);
                    finishAffinity();
                    startActivity(intent);
                }
            }
        });
   }

   //NFC ndef 메시지 생성성
   private NdefMessage createNdefMessage(String content){

       NdefRecord ndefRecord = createTextRecord(content);

       NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ ndefRecord });

       return ndefMessage;
   }
   //String to Ndef
   private NdefRecord createTextRecord(String content){
       try{
           byte[] language;
           language = Locale.getDefault().getLanguage().getBytes("UTF-8");
           final byte[] text = (content).getBytes("UTF-8");
           final int languageSize = language.length;
           final int textLength = text.length;
           final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);
           payload.write((byte) (languageSize & 0x1F));
           payload.write(language,0,languageSize);
           payload.write(text,0,textLength);

           return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT,new byte[0],payload.toByteArray());

       }catch (UnsupportedEncodingException e){
           Log.e("createTextRecord",e.getMessage());
       }
       return null;
   }
   //NFC 카드 읽기
   @Override
   protected void onNewIntent(Intent intent) {
       super.onNewIntent(intent);
       if(intent.hasExtra(NfcAdapter.EXTRA_TAG))
       {
           Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
           //입력 메시지 있을시
           if(parcelables != null && parcelables.length > 0)
           {
               readTextFromMessage((NdefMessage)parcelables[0]);
           }
           //입력 메시지 없을시
           else{
               Toast.makeText(this,"ndef 메시지를 찾을 수 없습니다.",Toast.LENGTH_SHORT).show();
           }
       }
   }
   //ndef 메시지 get
   private void readTextFromMessage(NdefMessage ndefMessage){

       NdefRecord[] ndefRecords = ndefMessage.getRecords();

       if(ndefRecords != null && ndefRecords.length>0){
           NdefRecord ndefRecord = ndefRecords[0];

           //읽은 메시지 set
           UserInfo = getTextFromNdefRecord(ndefRecord);

           //교환 성공시
           if(UserInfo!=null){
               //교환 실시
               //읽은 상댇방 카드 ID set
               String total = UserInfo;
               String UserDB = total.substring(0, total.indexOf("/"));
               String UserID = total.substring(total.indexOf("/")+1);
               //DataSet
               viewModel.UserDB.setValue(UserDB);
               viewModel.UserID.setValue(UserID);
               viewModel.CardExchange();
           }
       }else{
           Toast.makeText(this,"ndef 레코드를 찾을 수 없습니다.",Toast.LENGTH_SHORT).show();
       }
   }
   //ndef to string
    public String getTextFromNdefRecord (NdefRecord ndefRecord)
    {
        String tagContent = null;

        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, 3,
                    payload.length-3,textEncoding);
        }catch (UnsupportedEncodingException e)
        {
            Log.e("getTextFromNdefRecord",e.getMessage(),e);
        }
        return tagContent;
    }
    //교환 어뎁터
    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }
    private void enableForegroundDispatchSystem(){
        Intent intent = new Intent(this, NFCExchangeView.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFilters,null);
    }
}