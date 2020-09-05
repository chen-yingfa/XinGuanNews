package com.example.xinguannews;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import com.example.xinguannews.article.Article;
import com.example.xinguannews.article.ArticleThread;
import com.example.xinguannews.ui.main.CardListFragment;
import com.example.xinguannews.ui.main.CardListPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ArticleThreadListener {
    private Toolbar toolbar;

    private DrawerLayout mainDrawerLayout;
    private NavigationView navigationView;
    private CardListPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final List<Fragment> mFragmentList = new ArrayList<>();

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

    private List<Article> articles = new ArrayList<Article>();
    private boolean isGettingArticles = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);        // get instance of the widget
        setSupportActionBar(toolbar);

        // setup bottom navigation menu
        BottomNavigationView bottomNavigationMenu = (BottomNavigationView) findViewById(R.id.nav_bottom_menu);


        initTabLayout();
        onRefresh();
    }



    private void initNavMenu() {
//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_home_white_24dp);
        }
        navigationView.setCheckedItem(R.id.nav_menu_item_home); // 默认选中 home
        // 设置菜单项的监听事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mainDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_item_home:
                        // 每个菜单项的点击事件，通过Intent实现点击item简单实现活动页面的跳转。
                        // Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        // 第二个 Main2Activity.class 需要你自己 new 一个 Activity 来做出其他功能页面
                        // startActivity(intent);
                        break;
                    case R.id.nav_menu_item_edit_tags:
                        break;
                    case R.id.nav_menu_item_expert:
                        break;
                    case R.id.nav_menu_item_diagram:
                        break;
                    case R.id.nav_menu_item_downloads:
                        break;
                    default:
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        initNavMenu();
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
    //you bug
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                mainDrawerLayout.openDrawer(GravityCompat.START);  // 打开 open navigation
                break;
            default:

        }
        return true;
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



