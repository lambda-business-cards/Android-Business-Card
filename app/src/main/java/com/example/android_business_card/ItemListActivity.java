package com.example.android_business_card;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class ItemListActivity extends AppCompatActivity {
    BusinessCardSet bcs;
    Context context;

    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        context=getApplicationContext();
////////////////////Very important for http request///////////////////////
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
////////////////////////////////////////////////////////////////////////////

        bcs=receiveData();
        if(bcs==null) bcs=new BusinessCardSet(context);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bcs.saveAll();
                sendData();
            }
        });


        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        ImageView ivButtonSetting=findViewById(R.id.button_setting);
        ivButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      bcs.saveProfile();
                sendDataToSetting();
            }
        });
    }

    private void sendData(){
        Notification.send(context,"save","sent");
    }



    private void sendDataToSetting(){
        Context context= getApplicationContext();
        Intent intent = new Intent(context, Setting.class);
        intent.putExtra(getResources().getString(R.string.data_profile),bcs);
        startActivityForResult(intent,1);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        viewAdapter = new SimpleItemRecyclerViewAdapter(this, bcs, mTwoPane);
        recyclerView.setAdapter(viewAdapter);

    }


    private boolean handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            return bcs.getAPI(Integer.parseInt(sharedText));
            // Update UI to reflect text being shared
        }
        return false;
    }



    private BusinessCardSet receiveData(){
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else{

            }
        }
        BusinessCardSet bcs=(BusinessCardSet) intent.getParcelableExtra(  "DATA_FROM_SETTING");



        return bcs;
    }
}
