package com.example.android_business_card;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

// S04M03-3 Add Model Object
public class BusinessCard implements Parcelable {
    private int id;
    boolean isChecked;
    private String strName;         //Business=company name /private=person's name
    private String strContactName;  //private=person's name /Business=company name
    private String strTitle;
    private String strAddress;
    private String strPhone;
    private String strEmail;
    private String strWebURL;
    private String strFax;
    private String strImageURL;
    private String strQRcodeURL;


    public BusinessCard(int id, boolean isChecked, String strName, String strContactName, String strTitle, String strAddress, String strPhone, String strEmail, String strWebURL, String strFax, String strImageURL, String strQRcodeURL) {
        this.id = id;
        this.isChecked = isChecked;
        this.strName = strName;
        this.strContactName = strContactName;
        this.strTitle = strTitle;
        this.strAddress = strAddress;
        this.strPhone = strPhone;
        this.strEmail = strEmail;
        this.strWebURL = strWebURL;
        this.strFax = strFax;
        this.strImageURL = strImageURL;
        this.strQRcodeURL = strQRcodeURL;
    }



    public BusinessCard(JSONObject json) {
        try{
            this.id = json.getInt("id");
           // this.isChecked = json.getBoolean()isChecked;
            this.strName = json.getString("business_name");
            this.strContactName = json.getString("contact_name");
            this.strTitle= json.getString("title");
            this.strAddress= json.getString("address");
            this.strPhone= json.getString("phone");
            this.strEmail= json.getString("email");
            this.strWebURL= json.getString( "web_url");
            this.strFax= json.getString("fax");
            this.strImageURL= json.getString("img_url");
            this.strQRcodeURL= json.getString("qr_url");
        }catch (Exception e){
            e.getMessage();
        }

    }


    public BusinessCard(int id, String strName) {
        this.id = id;
        this.strName = strName;
        this.strTitle="personal";
        this.isChecked=true;
        this.strAddress="Far far away, galaxy";
        this.strPhone="911";
        this.strEmail="abc@def.ghi";
        this.strWebURL="https://swapi.co/api/people/";
        this.strFax="1-234-567-8901";
        this.strImageURL="https://swapi.co/api/people/"+Integer.toString(id)+"/";
        this.strQRcodeURL="2";
    }


    protected BusinessCard(Parcel in) {
        id = in.readInt();
        isChecked = in.readByte() != 0;
        strName = in.readString();
        strContactName = in.readString();
        strTitle=in.readString();
        strAddress = in.readString();
        strPhone = in.readString();
        strEmail = in.readString();
        strWebURL = in.readString();
        strFax = in.readString();
        strImageURL = in.readString();
        strQRcodeURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(strName);
        dest.writeString(strContactName);
        dest.writeString(strTitle);
        dest.writeString(strAddress);
        dest.writeString(strPhone);
        dest.writeString(strEmail);
        dest.writeString(strWebURL);
        dest.writeString(strFax);
        dest.writeString(strImageURL);
        dest.writeString(strQRcodeURL);
    }

    public void setStringSet(String[] strSet){
        int i=0;
        id = Integer.parseInt(strSet[i++]);
        isChecked = Boolean.valueOf(strSet[i++]);
        strName =strSet[i++];
        strContactName=strSet[i++];
        strTitle=strSet[i++];
        strAddress=strSet[i++];
        strPhone=strSet[i++];
        strEmail=strSet[i++];
        strWebURL=strSet[i++];
        strFax=strSet[i++];
        strImageURL=strSet[i++];
        strQRcodeURL=strSet[i++];


    }

    public String[] getStringSet(){
        String[] strSet=new String[12];
        int i=0;
        strSet[i++]=Integer.toString(id);
        strSet[i++]=Boolean.toString(isChecked);
        strSet[i++]=strName;
        strSet[i++]=strContactName;
        strSet[i++]=strTitle;
        strSet[i++]=strAddress;
        strSet[i++]=strPhone;
        strSet[i++]=strEmail;
        strSet[i++]=strWebURL;
        strSet[i++]=strFax;
        strSet[i++]=strImageURL;
        strSet[i++]=strQRcodeURL; //12
        return strSet;
    }
    public void delete(){
        this.strName = "";
        this.strTitle= "";
        this.isChecked=true;
        this.strAddress= "";
        this.strPhone= "";
        this.strEmail= "";
        this.strWebURL= "";
        this.strFax= "";
        this.strImageURL= "";
        this.strQRcodeURL= "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BusinessCard> CREATOR = new Creator<BusinessCard>() {
        @Override
        public BusinessCard createFromParcel(Parcel in) {
            return new BusinessCard(in);
        }

        @Override
        public BusinessCard[] newArray(int size) {
            return new BusinessCard[size];
        }
    };


    public String getStrContactName() {
        return strContactName;
    }

    public void setStrContactName(String strContactName) {
        this.strContactName = strContactName;
    }

    public int getId() {
        return id;
    }
    public String getStrId() {
        return Integer.toString(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    public String getStrPhone() {
        return strPhone;
    }

    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrWebURL() {
        return strWebURL;
    }

    public void setStrWebURL(String strWebURL) {
        this.strWebURL = strWebURL;
    }

    public String getStrFax() {
        return strFax;
    }

    public void setStrFax(String strFax) {
        this.strFax = strFax;
    }

    public String getStrImageURL() {
        return strImageURL;
    }

    public void setStrImageURL(String strImageURL) {
        this.strImageURL = strImageURL;
    }

    public String getStrQRcodeURL() {
        return strQRcodeURL;
    }

    public void setStrQRcodeURL(String strQRcodeURL) {
        this.strQRcodeURL = strQRcodeURL;
    }


    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }
}
