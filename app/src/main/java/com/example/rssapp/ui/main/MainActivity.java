package com.example.rssapp.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.rssapp.R;
import com.example.rssapp.ui.main.adapter.ViewPagerMainAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private Toolbar tbMain;
    private ViewPager2 vpMain;
    private TabLayout tlMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mappingView();
        setUpToolbar();
        setUpTabLayoutWithViewPager();
    }

    private void mappingView() {
        tbMain = (Toolbar) findViewById(R.id.tbMain);
        vpMain = findViewById(R.id.vpMain);
        tlMain = findViewById(R.id.tlMain);
    }

    private void setUpToolbar() {
        setSupportActionBar(tbMain);
    }

    public void setToolbarTitle(String title){
        tbMain.setTitle(title);
    }

    private void setUpTabLayoutWithViewPager() {
        FragmentStateAdapter viewPagerAdapter = new ViewPagerMainAdapter(getSupportFragmentManager(), getLifecycle());
        vpMain.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tlMain, vpMain, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Home");
                    tab.setIcon(R.drawable.ic_home);
                    break;
                default:
                    tab.setText("Saved");
                    tab.setIcon(R.drawable.ic_saved);
                    break;
            }
        }
        ).attach();
    }
}