package com.example.xinguannews;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.example.xinguannews.article.Article;
import com.example.xinguannews.article.ArticleApiAdapter;
import com.example.xinguannews.article.ArticleThread;
import com.example.xinguannews.ui.main.ArticleFragment;
import com.example.xinguannews.ui.main.SectionsPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ArticleThreadListener {
    private androidx.appcompat.widget.Toolbar toolbar;

    private DrawerLayout mainDrawerLayout;
    private NavigationView navigationView;
    private SectionsPagerAdapter sectionsPagerAdapter;
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
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_design);

        View v = navigationView.getHeaderView(0);
        CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.icon_image);
        setSupportActionBar(toolbar);

        initTabLayout();
//        getAllArticles();
        onRefresh();
    }

    @Override
    protected void onStart() {


        //=============================================以下部分为 nav 调用部分
        super.onStart();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_home);
        }
        navigationView.setCheckedItem(R.id.nav_one);//设置第一个默认选中
        navigationView.setNavigationItemSelectedListener(new  NavigationView.OnNavigationItemSelectedListener() {
            //设置菜单项的监听事件
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mainDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_one:
                        //每个菜单项的点击事件，通过Intent实现点击item简单实现活动页面的跳转。
                        /*Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        //第二个Main2Activity.class需要你自己new一个 Activity来做出其他功能页面
                        startActivity(intent);*/
                        break;
                    case R.id.nav_exit:

                        break;
                    default:
                }
                return true;
            }
        });
   // ==============================================以上部分为 nav drawerlayout部分功能实现
    }

    private void initTabLayout() {
        // Setup tab layout
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        sectionsPagerAdapter.addFragment(new ArticleFragment(typeAll), pageTitleAll);
        sectionsPagerAdapter.addFragment(new ArticleFragment(typeNews), pageTitleNews);
        sectionsPagerAdapter.addFragment(new ArticleFragment(typePaper), pageTitlePaper);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void getAllArticles() {
        ArticleApiAdapter articleApiAdapter = new ArticleApiAdapter(this, articles);
        articleApiAdapter.getArticles("all", 5, 20);
        isGettingArticles = true;
    }

    public void onGotArticles(List<Article> articles) {
        System.out.println("onGotArticles");
        System.out.println(articles);
        System.out.println(Thread.currentThread());
        isGettingArticles = false;
        this.articles = articles;
        for (Article article : articles) {
            addArticle(article);
        }
    }

    public void addArticle(Article article) {
        sectionsPagerAdapter.addArticle(article);
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
        for (ArticleFragment fragment : sectionsPagerAdapter.getFragments()) {
            fragment.onThreadFinish(thread);
        }
    }

    @Override
    public void onRefresh() {
        for (ArticleFragment fragment : sectionsPagerAdapter.getFragments()) {
            fragment.onRefresh();
        }
    }
}



