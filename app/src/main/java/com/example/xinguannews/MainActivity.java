package com.example.xinguannews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static boolean firstInit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: uncomment the following code when finished
        // Load previously viewed articles on boot up
        if (firstInit) {
            System.out.println("firstInit");
            ViewedArticlesManager.loadViewedArticles(this);  // 载入浏览过的文章
            firstInit = false;
        }
        initBottomNavMenu();
        openFragment(new HomeFragment());
    }

    @SuppressLint("WrongConstant")
    private void initBottomNavMenu() {
        BottomNavigationView bottomNavigationMenu = findViewById(R.id.bottom_nav_menu);

        // 英发 这段代码是用来解决 bug 的， bottom_nav_menu 加了超过了 3 个 item 之后显示就会有问题，字会消失
        // 师叔，我觉得字消失更漂亮。
        //-----------------------------------------------
//        bottomNavigationMenu.setLabelVisibilityMode(0); // 默认动画
//        bottomNavigationMenu.setLabelVisibilityMode(1); // 默认清除动画（显示文字）
        //----------------------------------------------- fix bug

        // 设置导航栏菜单项 item 选中监听
        bottomNavigationMenu.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_bottom_home:
                openFragment(new HomeFragment());
                break;
            case R.id.nav_bottom_data:
                openFragment(new DataFragment());
                break;
            case R.id.nav_bottom_expert:
                break;
            case R.id.nav_bottom_diagram:
                openFragment(new GraphSchemaFragment());
                break;
            case R.id.nav_bottom_downloaded:
                openFragment(new DownloadedFragment());
                break;

        }
        return true;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottom_nav_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
//        ViewedArticlesManager.saveViewedArticles(this);
    }

    // check if device can use internet
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("bing.com");
            // You can replace it with your name
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("onResume");
        ViewedArticlesManager.saveViewedArticles(this);
    }
}



