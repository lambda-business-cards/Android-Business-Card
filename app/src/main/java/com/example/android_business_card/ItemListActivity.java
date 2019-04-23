package com.example.android_business_card;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
// S04M03-6 replace all references to dummycontent with out model object
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ArrayList<BusinessCard>        swApiObjects;
    private SimpleItemRecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);


        swApiObjects = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    // S04M03-8 pull out fields from recyclerview construction and call our method
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        viewAdapter = new SimpleItemRecyclerViewAdapter(this, swApiObjects, mTwoPane);
        recyclerView.setAdapter(viewAdapter);
        getData();
    }
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BusinessCard person    = null;
                int         counter   = 1;
                int         failCount = 0;
                do {
                    person = BusinessCardDAO.getPerson(counter++);
                    if (person != null) {
                        swApiObjects.add(person);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewAdapter.notifyItemChanged(swApiObjects.size() - 1);
                            }
                        });
                        failCount = 0;
                    } else {
                        ++failCount;
                    }
                } while (person != null || failCount < 2);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                BusinessCard starship    = null;
                int         counter   = 1;
                int         failCount = 0;
                do {
                    starship = BusinessCardDAO.getStarship(counter++);
                    if (starship != null) {
                        swApiObjects.add(starship);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewAdapter.notifyItemChanged(swApiObjects.size() - 1);
                            }
                        });
                        failCount = 0;
                    } else {
                        ++failCount;
                    }
                } while (starship != null || failCount < 2);
            }
        }).start();
    }


    public static class SimpleItemRecyclerViewAdapter
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
            final BusinessCard swApiObject = mValues.get(position);
            holder.mIdView.setText(String.valueOf(swApiObject.getId()));
            holder.mNameView.setText(swApiObject.getName());

//        S04M03-13 bind data to new views
            holder.mCategoryView.setText(swApiObject.getCategory());
            holder.mImageView.setImageDrawable(
                    holder.mImageView.getContext().getDrawable(
                            DrawableResolver.getDrawableId(
                                    swApiObject.getCategory(),
                                    swApiObject.getId())));

            holder.itemView.setTag(swApiObject);
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
                        arguments.putSerializable(ItemDetailFragment.ARG_ITEM_ID, item);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.item_detail_container, fragment)
                                       .commit();
                    } else {
                        Context context = view.getContext();
                        Intent  intent  = new Intent(context, ItemDetailActivity.class);
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

        //        S04M03-12 add new views to viewholder
        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mNameView, mCategoryView;
            final ImageView mImageView;
            final View      parentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mNameView = (TextView) view.findViewById(R.id.name);
                mCategoryView = view.findViewById(R.id.category);
                mImageView = view.findViewById(R.id.image_view);
                parentView = view.findViewById(R.id.parent_view);
            }
        }
    }
}
