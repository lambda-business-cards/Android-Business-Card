package com.example.android_business_card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



public class ActivityNote extends AppCompatActivity {

        BusinessCardSet bcd;
        Context context;
        int iBase;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_note);
            context=getApplicationContext();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ReceiveData();//set data on bcd
            setView();
            findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveProfile();
                    Notification.send(context,"save setting","sent");
                }
            });

        }
        public void saveProfile(){
            bcd.saveProfile();
        }

        public void ReceiveData(){
            bcd=(BusinessCardSet)getIntent().getParcelableExtra(getResources().getString(R.string.data_profile));

        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            sendData();
        }



        private String[] getStringProfile(){
            String[] strUser=new String[22];
            EditText et;


            for(int i=0;i<21;i++){

                if(i==3||i==11){
                    strUser[i]=Boolean.toString(((CheckBox)findViewById(iBase+2*i+1)).isChecked());
                }else{
                    et=findViewById(iBase+2*i+1);
                    strUser[i]=et.getText().toString();
                }


            }

            return strUser;
        }

        private void sendData(){
            String[] strResult=getStringProfile();
            bcd.setStringUser(strResult);
            Intent intent = new Intent(context, ItemListActivity.class);
            intent.putExtra("DATA_FROM_SETTING", bcd);
            startActivity(intent);
        }

        public void setView(){
            ConstraintLayout cl=findViewById(R.id.cl_setting);
            Guideline gl=findViewById(R.id.guidelineVertical);
            String[] strTitle={"Your Name","Username","Password","!~Personal","ImageURL","address","phone","e-mail","websiteURL","fax","QR code URL","!~Business","business name","ImageURL","title","address","phone","e-mail","fax","websiteURL","QR code URL"};
            setConstraintLayout(strTitle);
        }


        @SuppressLint("ResourceType")
        public void setConstraintLayout(String[] strItems){

            int iIndex=10000;
            int iSizeText=20;
            ConstraintLayout cl=findViewById(R.id.cl_setting);
            Guideline glv=findViewById(R.id.guidelineVertical);
            Guideline glh=findViewById(R.id.guidelineHorizontal);

            String[] straBC=bcd.getStringUser();


            iBase=glv.getId()+iIndex;
            int iIDobove=0;

            ConstraintSet cs=new ConstraintSet();
            for(int i=0;i< strItems.length;i++){
                TextView tv= new TextView(context);

                if(strItems[i].contains("!~")){

                    String  strTemp=strItems[i].replace("!~","");
                    Boolean bChecked=Boolean.valueOf(straBC[i]);
                    if(bChecked){
                        tv.setTextColor(Color.BLACK);
                    }else{
                        tv.setTextColor(Color.LTGRAY);
                    }
                    tv.setId(iBase+2*i);
                    tv.setText(strTemp);
                    tv.setTextSize(iSizeText);
                    CheckBox cb=new CheckBox(context);
                    cb.setId(iBase+2*i+1);
                    cb.setChecked(bChecked);

                    cl.addView(tv);
                    cl.addView(cb);

                    cs.constrainHeight(cb.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainDefaultWidth(cb.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainHeight(tv.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainDefaultWidth(tv.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.connect(tv.getId(),ConstraintSet.BASELINE,cb.getId(),ConstraintSet.BASELINE);
                    cs.connect(tv.getId(),ConstraintSet.END,cb.getId(),ConstraintSet.START);
                    cs.connect(cb.getId(),ConstraintSet.START,glv.getId(),ConstraintSet.START);
                    if(i==0){
                        cs.connect(cb.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
                    }else{
                        cs.connect(cb.getId(),ConstraintSet.TOP,iIDobove,ConstraintSet.BOTTOM);

                    }
                    iIDobove=cb.getId();
                }else if(strItems[i].contains("%~")){
                    String strTemp=strItems[i].replace("%~","");
                    tv.setId(iBase+2*i);
                    tv.setText(strTemp);
                    tv.setTextSize(iSizeText);
                    ImageView iv=new ImageView(context);

                    iv.setId(iBase+2*i+1);

                    cl.addView(tv);
                    cl.addView(iv);

                    cs.constrainHeight(iv.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainDefaultWidth(iv.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainHeight(tv.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainDefaultWidth(tv.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.connect(tv.getId(),ConstraintSet.BASELINE,iv.getId(),ConstraintSet.BASELINE);
                    cs.connect(tv.getId(),ConstraintSet.END,iv.getId(),ConstraintSet.START);
                    cs.connect(iv.getId(),ConstraintSet.START,glv.getId(),ConstraintSet.START);
                    if(i==0){
                        cs.connect(iv.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
                    }else{
                        cs.connect(iv.getId(),ConstraintSet.TOP,iIDobove,ConstraintSet.BOTTOM);
                    }
                    iIDobove=iv.getId();
                }else{
                    tv.setId(iBase+2*i);
                    tv.setText(strItems[i]);
                    tv.setTextSize(iSizeText);
                    EditText et=new EditText(context);
                    et.setText(straBC[i]);
                    et.setSingleLine(true);
                    et.setId(iBase+2*i+1);
                    et.setHint(strItems[i]);
                    et.setTextSize(iSizeText);
                    cl.addView(tv);
                    cl.addView(et);

                    cs.constrainHeight(et.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainDefaultWidth(et.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainHeight(tv.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.constrainDefaultWidth(tv.getId(),ConstraintSet.WRAP_CONTENT);
                    cs.connect(tv.getId(),ConstraintSet.BASELINE,et.getId(),ConstraintSet.BASELINE);
                    cs.connect(tv.getId(),ConstraintSet.END,et.getId(),ConstraintSet.START);
                    cs.connect(et.getId(),ConstraintSet.START,glv.getId(),ConstraintSet.START);
                    if(i==0){
                        cs.connect(et.getId(),ConstraintSet.TOP,glh.getId(),ConstraintSet.BOTTOM);
                    }else{
                        cs.connect(et.getId(),ConstraintSet.TOP,iIDobove,ConstraintSet.BOTTOM);
                    }
                    iIDobove=et.getId();
                }
                cs.applyTo(cl);
            }

        }

    }
