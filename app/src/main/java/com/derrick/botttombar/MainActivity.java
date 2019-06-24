package com.derrick.botttombar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.derrick.botttombar.databinding.ActivityMainBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private ActivityMainBinding binding;

    private Toolbar mToolbar;
    private TextView mTitle;
    private LinearLayout mTitleContainer;
    private AppBarLayout mAppBarLayout;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 1.0f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.8f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_main, container, false);

        bindActivity();

        mToolbar.setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);

        //((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        showFragment(new MainFragment());

        binding.navBar.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reduceFloatingButtonFocus();
            }
        });

        binding.navBar.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseFloatingButtonFocus();
            }
        });

        return binding.getRoot();
    }


    private void showFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();
    }


    private void increaseFloatingButtonFocus() {
        showToolBar();
        showFragment(new MainFragment());
        binding.bar.setFabCradleMargin(dpToPx(16));
        binding.fab.setSize(FloatingActionButton.SIZE_NORMAL);
        binding.bar.setFabCradleRoundedCornerRadius(dpToPx(8));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.fab.setElevation(8f);
            binding.navBar.secondFab.setElevation(0f);
        }
        binding.navBar.secondFab.setVisibility(View.INVISIBLE);
        binding.fab.show();
    }

    private void reduceFloatingButtonFocus() {
        hideToolBar();
        showFragment(new GroupFragment());
        binding.bar.setFabCradleMargin(dpToPx(0));
        binding.fab.setSize(FloatingActionButton.SIZE_MINI);
        binding.bar.setFabCradleRoundedCornerRadius(dpToPx(0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.fab.setElevation(0f);
            binding.navBar.secondFab.setElevation(0f);
        }
        binding.fab.hide();
        binding.navBar.secondFab.show();
    }

    private void hideToolBar() {
        binding.mainAppbar.setVisibility(View.GONE);
        binding.walletCard.walletView.setVisibility(View.GONE);
    }

    private void showToolBar() {
        binding.mainAppbar.setVisibility(View.VISIBLE);
        binding.walletCard.walletView.setVisibility(View.VISIBLE);
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void bindActivity() {
        mToolbar = binding.toolbar;
        mTitle = binding.mainTextviewTitle;
        mTitleContainer = binding.contentTitle.mainLinearlayoutTitle;
        mAppBarLayout = binding.mainAppbar;
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                exitReveal();
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {
            if (!mIsTheTitleContainerVisible) {
                enterReveal();
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        Log.d(LOG_TAG, "@Vertical offset percentage::" + percentage);

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }


    void enterReveal() {
        // previously invisible view
        final View myView = binding.walletCard.walletView;
        // get the center for the clipping circle
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = myView.getMeasuredWidth() / 2;
            int cy = myView.getMeasuredHeight() / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    null;

            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);


            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            myView.setVisibility(View.VISIBLE);
        }
    }

    void exitReveal() {
        // previously visible view
        final View myView = binding.walletCard.walletView;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            // get the center for the clipping circle
            int cx = myView.getMeasuredWidth() / 2;
            int cy = myView.getMeasuredHeight() / 2;

            // get the initial radius for the clipping circle
            int initialRadius = myView.getWidth() / 2;

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();
        }
    }


}
