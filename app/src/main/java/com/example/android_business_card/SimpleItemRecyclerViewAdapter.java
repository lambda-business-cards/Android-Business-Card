package com.example.android_business_card;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final ItemListActivity  mParentActivity;
    private final List<BusinessCard> mValues;
    private final boolean           mTwoPane;

    // S04M03-16 set the position value
    private int lastPosition = -1;


    SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                  List<BusinessCard> items,
                                  boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // S04M03-10 convert id to string to display
        final BusinessCard bc = mValues.get(position);
        holder.mNameView.setText(bc.getStrName());
        holder.mContactView.setText(bc.getStrContactName());

//        S04M03-13 bind data to new views
        holder.mEmailView.setText(bc.getStrEmail());
        holder.mImageView.setImageDrawable(
                holder.mImageView.getContext().getDrawable(
                        DrawableResolver.getDrawableId(
                                bc.getStrName(),
                                bc.getId())));

        holder.itemView.setTag(bc);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                // S04M03-17 update click listener to pass our object
                BusinessCard item = (BusinessCard) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
//                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));  // put object in intent
                    arguments.putParcelable(ItemDetailFragment.ARG_ITEM_ID, item);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent  = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item);  // put object in intent

                    // S04M03-22 add options to make transition appear
//                        Bundle options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext()).toBundle();
                    // S04M03-24 change constructor to allow for shared views
                    Bundle options = ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) view.getContext(),
                            holder.mImageView,
                            ViewCompat.getTransitionName(holder.mImageView)
                    ).toBundle();

                    context.startActivity(intent, options);
                }
            }
        });

        // S04M03-15 call animation method
        setEnterAnimation(holder.parentView, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // S04M03-14 writing a method to set animation
    private void setEnterAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameView;
        final TextView mContactView, mEmailView;
        final ImageView mImageView;
        final View      parentView;

        ViewHolder(View view) {
            super(view);
            mNameView = (TextView) view.findViewById(R.id.text_name);
             mContactView= (TextView) view.findViewById(R.id.text_contact_name);
            mEmailView = view.findViewById(R.id.text_email);
            mImageView = view.findViewById(R.id.image_view);
            parentView = view.findViewById(R.id.parent_view);
        }
    }
}