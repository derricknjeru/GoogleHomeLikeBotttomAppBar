package com.derrick.botttombar;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.derrick.botttombar.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        binding.bottomBar.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reduceFloatingButtonFocus();
            }
        });

        binding.bottomBar.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseFloatingButtonFocus();
            }
        });

    }

    private void increaseFloatingButtonFocus() {
        binding.bottomBar.bar.setFabCradleMargin(dpToPx(16));
        binding.bottomBar.fab.setSize(FloatingActionButton.SIZE_NORMAL);
        binding.bottomBar.bar.setFabCradleRoundedCornerRadius(dpToPx(8));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.bottomBar.fab.setElevation(8f);
            binding.bottomBar.secondFab.setElevation(0f);
        }
        binding.bottomBar.secondFab.setVisibility(View.INVISIBLE);
        binding.bottomBar.fab.show();
    }

    private void reduceFloatingButtonFocus() {
        binding.bottomBar.bar.setFabCradleMargin(dpToPx(0));
        binding.bottomBar.fab.setSize(FloatingActionButton.SIZE_MINI);
        binding.bottomBar.bar.setFabCradleRoundedCornerRadius(dpToPx(0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.bottomBar.fab.setElevation(0f);
            binding.bottomBar.secondFab.setElevation(0f);
        }
        binding.bottomBar.fab.hide();
        binding.bottomBar.secondFab.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
