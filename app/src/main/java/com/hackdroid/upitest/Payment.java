package com.hackdroid.upitest;

public class Payment {
    /**
     *  payUri.appendQueryParameter("pa" , vpa);
     *         payUri.appendQueryParameter("pn" , "Name")  ;
     *         Long tsLong = System.currentTimeMillis()/1000;
     *         String transaction_ref_id = tsLong.toString();
     *         payUri.appendQueryParameter("tid" , transaction_ref_id) ;
     *         payUri.appendQueryParameter("mc" , "" ) ;
     *         payUri.appendQueryParameter("tr",transaction_ref_id);
     *         payUri.appendQueryParameter("tn", "TCN_DESC");
     *         payUri.appendQueryParameter("am", amount);
     *         payUri.appendQueryParameter("cu", "INR");
     */
    String to , name , tId , tRef , merchantCode ,  tDesc  , amount ,
            CURRENCY = "INR" ;

    public Payment(String to, String name, String tId, String tRef,
                   String merchantCode, String tDesc, String amount) {
        this.to = to;
        this.name = name;
        this.tId = tId;
        this.tRef = tRef;
        this.merchantCode = merchantCode;
        this.tDesc = tDesc;
        this.amount = amount;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String gettRef() {
        return tRef;
    }

    public void settRef(String tRef) {
        this.tRef = tRef;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String gettDesc() {
        return tDesc;
    }

    public void settDesc(String tDesc) {
        this.tDesc = tDesc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCURRENCY() {
        return CURRENCY;
    }
}
