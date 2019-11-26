package com.hackdroid.upitest;

public interface PaymentStatusListener {
    void onTransactionCompleted(TransactionDetails transactionDetails);
    void onTransactionSuccess();
    void onTransactionSubmitted();
    void onTransactionFailed();
    void onTransactionCancelled();
}
