package com.example.android_business_card;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
// S04M03-11 remove all references to dummy content
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the mItem ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private BusinessCard mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = (BusinessCard) getArguments().getParcelable(ARG_ITEM_ID);
            Activity                activity     = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getStrName());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        rootView.getLayoutParams().height=1200;
        rootView.getLayoutParams().width=1200;
        TextView tv=new TextView(getContext());
        tv.setText("test");
        Bitmap myBitmap = null;

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ImageView iv= rootView.findViewById(R.id.item_detail);
            //iv.setImageDrawable(rootView.getContext().getDrawable( mItem.getDrawableResolver.getDrawableId(

              //      mItem.getStrContactName(),


                //    mItem.getId())));
            iv.getLayoutParams().width=1200;
            iv.getLayoutParams().height=1200;
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageBitmap(NetworkAdapter.getBitmapFromUrl(mItem.getStrQRcodeURL()));
        }
        return rootView;
    }

    private String url;
    private ImageView imageView;




}
