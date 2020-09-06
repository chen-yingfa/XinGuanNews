package com.example.xinguannews;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import com.example.xinguannews.article.ArticleThread;
import com.example.xinguannews.ui.main.CardListFragment;
import com.example.xinguannews.ui.main.CardListPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ArticleThreadListener {
    private View linearLayoutMain;
    private Toolbar toolbar;
    private ImageButton buttonEditTag;
    private CardListPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // types
    private final String typeAll = "all";
    private final String typeNews = "news";
    private final String typeEvent = "event";
    private final String typePaper = "paper";
    private final String typePoints = "points";

    // tab pages
    private List<String> pageTitleList;
    private final String pageTitleAll = "所有文章";
    private final String pageTitleNews = "新闻";
    private final String pageTitlePaper = "学术论文";
    private final String toolbarTitle = "新冠疫情新闻";

    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottomSheetEditTag;
    private ImageButton buttonCancelEditTag;
    private View darkener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);        // get instance of the widget
        setSupportActionBar(toolbar);
        linearLayoutMain = findViewById(R.id.linear_layout_main);

        // setup bottom navigation menu
        BottomNavigationView bottomNavigationMenu = (BottomNavigationView) findViewById(R.id.nav_bottom_menu);

        initEditTagUi();
        initTabLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        initNavMenu();
    }

    private void initEditTagUi() {
        // get XML elements
        darkener = findViewById(R.id.darkener);
        buttonCancelEditTag = findViewById(R.id.button_cancel_edit_tag);
        buttonEditTag = findViewById(R.id.button_edit_tag);
        bottomSheetEditTag = findViewById(R.id.bottom_sheet_edit_tag);

        sheetBehavior = BottomSheetBehavior.from(bottomSheetEditTag);
        darkener.setVisibility(View.GONE);
        initEditTagUiListeners();

        // The View with the BottomSheetBehavior
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
    }

    public void initEditTagUiListeners() {
        // click event for show/dismiss bottom sheet
        buttonEditTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    showEditTagSheet();
                } else {
                    hideEditTagSheet();
                }
            }
        });
        buttonCancelEditTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("buttonCancelEditTag clicked");
                hideEditTagSheet();
            }
        });
    }

    private void showEditTagSheet() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        darken();
    }

    private void hideEditTagSheet() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        undarken();
    }

    private void darken() {
        darkener.setVisibility(View.VISIBLE);
        linearLayoutMain.setClickable(false);
    }

    private void undarken() {
        darkener.setVisibility(View.GONE);
        linearLayoutMain.setClickable(true);
    }

    private void initTabLayout() {
        // Setup tab layout
        sectionsPagerAdapter = new CardListPagerAdapter(this, getSupportFragmentManager());

        sectionsPagerAdapter.addFragment(new CardListFragment(typeAll), pageTitleAll);
        sectionsPagerAdapter.addFragment(new CardListFragment(typeNews), pageTitleNews);
        sectionsPagerAdapter.addFragment(new CardListFragment(typePaper), pageTitlePaper);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initBottomNavMenu() {

    }

    @Override
    public void onThreadFinish(ArticleThread thread) {
        for (CardListFragment fragment : sectionsPagerAdapter.getFragments()) {
            fragment.onThreadFinish(thread);
        }
    }

    @Override
    public void onRefresh() {
        for (CardListFragment fragment : sectionsPagerAdapter.getFragments()) {
            fragment.onRefresh();
        }
    }
}



