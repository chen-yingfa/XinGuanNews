package com.example.xinguannews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = findViewById(R.id.toolbar);                             //get instance of the widget
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_design);


       // setSupportActionBar(myToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myToolbar.setLogo(R.drawable.people1);
        myToolbar.setTitle("主标题");
        myToolbar.setSubtitle("副标题");
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null){
            //通过HomeAsUp来让导航按钮显示出来
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置Indicator来添加一个点击图标
            actionBar.setHomeAsUpIndicator(R.drawable.people1);
        }
        navigationView.setCheckedItem(R.id.nav_one); //设置第一个默认选中
        navigationView.setNavigationItemSelectedListener(new  NavigationView.OnNavigationItemSelectedListener() {
            //设置菜单项的监听事件
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_one:
                        //每个菜单项的点击事件，通过Intent实现点击item简单实现活动页面的跳转。
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
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
                    default:
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    //you bug
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.drawable.people1:
                mDrawerLayout.openDrawer(GravityCompat.START);  // 打开 open navigation
                break;
            default:

        }
        return true;
    }
}
