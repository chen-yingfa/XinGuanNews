package com.example.xinguannews;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import android.view.View;
import com.example.xinguannews.ui.main.SectionsPagerAdapter;


public class MainActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        ArticleApiAdapter articleApiAdapter = new ArticleApiAdapter();
        articleApiAdapter.getArticles();

        setContentView(R.layout.activity_main);
        myToolbar = findViewById(R.id.toolbar);                             //get instance of the widget
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_design);

        View v = navigationView.getHeaderView(0);
        CircleImageView circleImageView =(CircleImageView) v.findViewById(R.id.icon_image);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        list = new ArrayList<>();


       // setSupportActionBar(myToolbar);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //  myToolbar.setLogo(R.drawable.people1);
        myToolbar.setTitle("XINGUANNEWS");

        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //通过HomeAsUp来让导航按钮显示出来
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置Indicator来添加一个点击图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_home);
        }
        navigationView.setCheckedItem(R.id.nav_one); //设置第一个默认选中
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            //设置菜单项的监听事件
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_one:
                        //每个菜单项的点击事件，通过Intent实现点击item简单实现活动页面的跳转。
                        Intent intent = new Intent(MainActivity.this, test2.class);
                        //第二个Main2Activity.class需要你自己new一个 Activity来做出其他功能页面
                        startActivity(intent);
                        break;
                    case R.id.nav_two:
                        Toast.makeText(MainActivity.this, "page1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_three:
                        Toast.makeText(MainActivity.this, "page2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_four:
                        Toast.makeText(MainActivity.this, "page3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_exit:
                    default:
                }
                return true;
            }
        });

        list.add("热点");
        list.add("推荐");
        list.add("Paper");
        list.add("News");
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            //得到当前页的标题，也就是设置当前页面显示的标题是tabLayout对应标题


            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return list.get(position);
            }
            @Override
            public Fragment getItem(int position) {
                NewsFragment newsFragment = new NewsFragment();
                //判断所选的标题，进行传值显示
                Bundle bundle = new Bundle();
                if (list.get(position).equals("热点")){
                    bundle.putString("name","top");
                }else if (list.get(position).equals("推荐")){
                    bundle.putString("name","2222");
                }else if (list.get(position).equals("Paper")){
                    bundle.putString("name","33333");
                }
                newsFragment.setArguments(bundle);
                return newsFragment;
            }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            NewsFragment newsFragment = (NewsFragment)  super.instantiateItem(container, position);

            return newsFragment;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    });
        tabLayout.setupWithViewPager(viewPager);
}

    @Override
    //you bug
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);  // 打开 open navigation
                break;
            default:

        }
        return true;
    }
}

