package com.hackdroid.upitest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements PaymentStatusListener {
    String amount = "1.00" ;
    String vpa = "9815963210@paytm" ;
    Uri.Builder payUri = new Uri.Builder() ;
    RecyclerView recyclerView ;
    List<ResolveInfo> list ;
    Adapter adapter ;
    Singleton singleton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singleton = Singleton.getInstance();
        payUri.scheme("upi").authority("pay") ;
        payUri.appendQueryParameter("pa" , vpa);
        payUri.appendQueryParameter("pn" , "Name")  ;
        Long tsLong = System.currentTimeMillis()/1000;
        String transaction_ref_id = tsLong.toString();
        payUri.appendQueryParameter("tid" , transaction_ref_id) ;
        payUri.appendQueryParameter("mc" , "" ) ;
        payUri.appendQueryParameter("tr",transaction_ref_id);
        payUri.appendQueryParameter("tn", "TRansaction_details");
        payUri.appendQueryParameter("am", amount);
        payUri.appendQueryParameter("cu", "INR");
        Uri uri = payUri.build();
      //  intent.setData(Uri.parse(UPI));\/
//        String UPI = getUPIString(vpa ,"" , ""  , "" ,
//                "" ,"DETLA" , amount , "INR" ,"" ) ;
//        Uri uri = Uri.parse(UPI)  ;
        recyclerView = findViewById(R.id.mRecyclerView) ;
        Intent intent = new Intent(Intent.ACTION_VIEW)  ;
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager())!=null){
            List<ResolveInfo> intentList = getPackageManager().queryIntentActivities(intent , 0);
            showApp(intentList , intent , getApplicationContext()) ;
            Log.d("IN" , ""+intentList)  ;
        }else{
            Log.d("IN" , "error")  ;

        }

    }

    private void showApp(List<ResolveInfo> intentList, Intent intent, Context applicationContext) {
        recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
        adapter = new Adapter(intentList , applicationContext , intent , this);
        recyclerView.setAdapter(adapter);
    }
    private TransactionDetails getTransaction(String response){
        Map<String , String> map = getQueryString(response) ;
        String transactionId = map.get("txnId");
        String responseCode = map.get("responseCode");
        String approvalRefNo = map.get("ApprovalRefNo");
        String status = map.get("Status");
        String transactionRefId = map.get("txnRef");
        try {


        }catch ( Exception e){
            e.printStackTrace();
        }
        return new TransactionDetails(transactionId, responseCode,
                approvalRefNo, status, transactionRefId);
    }


    private Map<String, String> getQueryString(String response) {
        String[] params = response.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
           try {
               String name = param.split("=")[0];
               String value = param.split("=")[1];
               map.put(name, value);
           }catch (Exception e){
               e.printStackTrace();
            }
        }
        return map;
    }
    private String getUPIString(String payeeAddress, String payeeName, String payeeMCC, String trxnID, String trxnRefId,
                                String trxnNote, String payeeAmount, String currencyCode, String refUrl) {
        String UPI = "upi://pay?pa=" + payeeAddress + "&pn=" + payeeName
                + "&mc=" + payeeMCC + "&tid=" + trxnID + "&tr=" + trxnRefId
                + "&tn=" + trxnNote + "&am=" + payeeAmount + "&cu=" + currencyCode
                + "&refUrl=" + refUrl;
        return UPI.replace(" ", "+");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400){
            if (data !=null){
                String response = data.getStringExtra("response");
                Log.d("REsponse" , response);
                if (response == null ){
                    callbackTransactionCancelled();
                    Log.d("TAG ::", "Response is null");
                }else{
                    TransactionDetails transactionDetails = getTransaction(response) ;
                    callbackTransactionComplete(transactionDetails);
                    try {
                        if (transactionDetails.getStatus().toLowerCase() == "success"){
                            callbackTransactionSuccess();
                        } else if (transactionDetails.getStatus().toLowerCase().equals("submitted")) {
                            callbackTransactionSubmitted();
                        } else {
                            callbackTransactionFailed();
                        }
                    }catch (Exception e){
                        callbackTransactionCancelled();
                        callbackTransactionFailed();
                    }
                }
               // TransactionDetails transactionDetails = getTransaction(response);

            }else{
                Toast.makeText(getApplicationContext() , "Error no data found" , Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Intent Data is null. User cancelled");
                callbackTransactionCancelled();
            }
//            finish();
        }
    }

    private void callbackTransactionFailed() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionFailed();
//            Toast.makeText(getApplicationContext() , "Faiedf"   , Toast.LENGTH_SHORT).show();
        }
    }

    private void callbackTransactionSubmitted() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionSubmitted();
        }
    }

    private void callbackTransactionSuccess() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionSuccess();
        }
        
    }

    private void callbackTransactionComplete(TransactionDetails transactionDetails) {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionCompleted(transactionDetails);
        }
    }

    private void callbackTransactionCancelled() {
        if (isListenerRegistered()) {
            singleton.getListener().onTransactionCancelled();
        }
    }

    private boolean isListenerRegistered() {
        return (Singleton.getInstance().isListenerRegistered());
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {

    }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {
//        Toast.makeText(getApplicationContext() , "Failed" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionFailed() {
        Toast.makeText(getApplicationContext() , "Failed" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {

    }
}
