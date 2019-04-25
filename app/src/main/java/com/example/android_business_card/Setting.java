package com.example.android_business_card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Setting extends AppCompatActivity {
    BusinessCardDAO bcd;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context=getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ReceiveData();//set data on bcd
        setView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void ReceiveData(){
        bcd=(BusinessCardDAO)getIntent().getParcelableExtra(getResources().getString(R.string.data_profile));

    }


    public void setView(){
        ConstraintLayout cl=findViewById(R.id.cl_setting);
        Guideline gl=findViewById(R.id.guidelineVertical);
        String[] strTitle={"Nickname","Your Name","Username","Password","ImageURL","test","!~Personal","address","phone","e-mail","websiteURL","!~Business","business name","address","phone","e-mail","fax","websiteURL"};
        setConstraintLayout(strTitle);
    }


    @SuppressLint("ResourceType")
    public void setConstraintLayout(String[] strItems){
        int iIndex=1000;
        int iSizeText=20;
        ConstraintLayout cl=findViewById(R.id.cl_setting);
        Guideline gl=findViewById(R.id.guidelineVertical);


        @SuppressLint("ResourceType") int iBase=gl.getId()+iIndex;
        int iIDobove=0;
        ConstraintSet cs=new ConstraintSet();
        for(int i=0;i< strItems.length;i++){
          TextView tv= new TextView(context);

            if(strItems[i].contains("!~")){
                String  strTemp=strItems[i].replace("!~","");
                tv.setId(iBase+2*i);
                tv.setText(strTemp);
                tv.setTextSize(iSizeText);
                CheckBox cb=new CheckBox(context);
                cb.setId(iBase+2*i+1);

                cl.addView(tv);
                cl.addView(cb);

                cs.constrainHeight(cb.getId(),ConstraintSet.WRAP_CONTENT);
                cs.constrainDefaultWidth(cb.getId(),ConstraintSet.WRAP_CONTENT);
                cs.constrainHeight(tv.getId(),ConstraintSet.WRAP_CONTENT);
                cs.constrainDefaultWidth(tv.getId(),ConstraintSet.WRAP_CONTENT);
                cs.connect(tv.getId(),ConstraintSet.BASELINE,cb.getId(),ConstraintSet.BASELINE);
                cs.connect(tv.getId(),ConstraintSet.END,cb.getId(),ConstraintSet.START);
                cs.connect(cb.getId(),ConstraintSet.START,gl.getId(),ConstraintSet.START);
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
                cs.connect(iv.getId(),ConstraintSet.START,gl.getId(),ConstraintSet.START);
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
                cs.connect(et.getId(),ConstraintSet.START,gl.getId(),ConstraintSet.START);
                if(i==0){
                    cs.connect(et.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
                }else{
                    cs.connect(et.getId(),ConstraintSet.TOP,iIDobove,ConstraintSet.BOTTOM);
                }
                iIDobove=et.getId();
            }
            cs.applyTo(cl);
        }

    }

}
