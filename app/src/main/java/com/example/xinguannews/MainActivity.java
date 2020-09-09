package com.example.xinguannews;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import com.example.xinguannews.ui.main.MainFragmentAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static boolean initted = false;

    private View linearLayoutMain;
    private Toolbar toolbar;
    private View DisappearView;
    private View DisappearView2;
    private View DisappearView3;

    private ImageButton buttonEditCategory;
    private MainFragmentAdapter cardListPagerAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    // types
    private final String categoryAll = "all";
    private final String categoryNews = "news";
    private final String categoryEvent = "event";
    private final String categoryPaper = "paper";

    private LinkedHashSet<String> categories = new LinkedHashSet<>();
    private LinkedHashSet<String> selectedCategories;

    // type titles（每个类型对应的中文标题）
    private final String categoryTitleAll = "所有文章";
    private final String categoryTitleNews = "新闻";
    private final String categoryTitlePaper = "学术论文";
    private final String categoryTitleEvent = "事件";
    private final String toolbarTitle = "新冠疫情新闻";
    private final Map<String, String> mapCategoryToTitle = new HashMap<>();

    // Shared Preferences key
    private final String sharedPrefKeySelectedCategories = "Selected Categories";

    // related to edit category UI
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottomSheetEditCategory;
    private ImageButton buttonCancelEditCategory;
    private ChipGroup chipGroupSelectedCategory;
    private ChipGroup chipGroupDeselectedCategory;
    LinkedHashMap<String, Chip> categoryChips;
    LinkedHashMap<String, Chip> selectedCategoryChips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutMain = findViewById(R.id.linear_layout_main);
        initBottomNavMenu();
        openFragment(new HomeFragment());
    }

    @SuppressLint("WrongConstant")
    private void initBottomNavMenu() {
        BottomNavigationView bottomNavigationMenu = findViewById(R.id.bottom_nav_menu);

        // 英发 这段代码是用来解决bug的， bottom_nav_menu 加了超过了3个item之后显示就会有问题,字会消失
        //--------------------------------------------
//        bottomNavigationMenu.setLabelVisibilityMode(0);  //默认动画
//        bottomNavigationMenu.setLabelVisibilityMode(1); //默认清除动画（显示文字）
        //---------------------------------------------------fix bug

        // 这里指定默认选中item （即一开始进入哪一个页面）
        bottomNavigationMenu.getMenu().getItem(2).setChecked(true);
        // the next it the listener

        final DataFragment Data_Fragment = new DataFragment();
        final HomeFragment Home_Fragment = new HomeFragment();
        Data_Fragment.setMessage("图谱");
        Home_Fragment.setMessage("主页");

        //设置导航栏菜单项Item选中监听
        bottomNavigationMenu.setOnNavigationItemSelectedListener(this);
    }





    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_bottom_home:
                openFragment(new HomeFragment());
                break;
            case R.id.nav_bottom_data:
//                        DisappearView = findViewById(R.id.linear_layout_sub1);
//                        DisappearView2 = findViewById(R.id.linear_layout_sub2);
//                        DisappearView3 = findViewById(R.id.view_pager);
//                        DisappearView3.setVisibility(View.GONE);
//
//                        DisappearView.setVisibility(View.GONE);
//                        DisappearView2.setVisibility(View.GONE);
                openFragment(new DataFragment());

//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, Data_Fragment).commitNow();
                break;
            case R.id.nav_bottom_expert:
                break;
            case R.id.nav_bottom_diagram:
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
}



