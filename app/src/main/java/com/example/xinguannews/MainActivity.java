package com.example.xinguannews;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.example.xinguannews.article.ArticleThread;
import com.example.xinguannews.ui.main.CardListFragment;
import com.example.xinguannews.ui.main.CardListPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, ArticleThreadListener, CategoryChipListener {
    private View linearLayoutMain;
    private Toolbar toolbar;
    private ImageButton buttonEditCategory;
    private CardListPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // types
    private final String categoryAll = "all";
    private final String categoryNews = "news";
    private final String categoryEvent = "event";
    private final String categoryPaper = "paper";

    private LinkedHashSet<String> categorys = new LinkedHashSet<>();
    private LinkedHashSet<String> selectedCategorys;

    // type titles（每个类型对应的中文标题）
    private final String categoryTitleAll = "所有文章";
    private final String categoryTitleNews = "新闻";
    private final String categoryTitlePaper = "学术论文";
    private final String categoryTitleEvent = "事件";
    private final String toolbarTitle = "新冠疫情新闻";
    private final Map<String, String> mapCategoryToTitle = new HashMap<>();

    // related to edit category UI
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottomSheetEditCategory;
    private ImageButton buttonCancelEditCategory;
    private View darkener;
    private ChipGroup chipGroupSelectedCategory;
    private ChipGroup chipGroupDeselectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);        // get instance of the widget
        setSupportActionBar(toolbar);
        linearLayoutMain = findViewById(R.id.linear_layout_main);

        // 初始化
        // 注：顺序不要变
        initCategorys();
        initEditCategoryUi();
        initTabLayout();
        initBottomNavMenu();
    }

    // init methods（同时获得 XML 元素）

    private void initTabLayout() {
        // Setup tab layout
        sectionsPagerAdapter = new CardListPagerAdapter(this, getSupportFragmentManager());

        for (String type : selectedCategorys) {
            String title = mapCategoryToTitle.get(type);
            sectionsPagerAdapter.addFragment(new CardListFragment(type), title);
        }
//        sectionsPagerAdapter.addFragment(new CardListFragment(typeAll), typeTitleAll);
//        sectionsPagerAdapter.addFragment(new CardListFragment(typeNews), typeTitleNews);
//        sectionsPagerAdapter.addFragment(new CardListFragment(typePaper), typeTitlePaper);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initBottomNavMenu() {
        BottomNavigationView bottomNavigationMenu = findViewById(R.id.nav_bottom_menu);
    }

    // 初始化所有类型的标题（中文名称），以及相关成员变量
    private void initCategorys() {
        // set type titles
        mapCategoryToTitle.put(categoryAll, categoryTitleAll);
        mapCategoryToTitle.put(categoryNews, categoryTitleNews);
        mapCategoryToTitle.put(categoryPaper, categoryTitlePaper);
        mapCategoryToTitle.put(categoryEvent, categoryTitleEvent);
        categorys.add(categoryAll);
        categorys.add(categoryNews);
        categorys.add(categoryPaper);
        categorys.add(categoryEvent);
        if (selectedCategorys == null) {
            // 默认有两类：所有文章，新闻
            selectedCategorys = new LinkedHashSet<String>(); // 首次赋值
            selectedCategorys.add(categoryAll);
            selectedCategorys.add(categoryNews);
        }
    }

    private void initEditCategoryUi() {
        // get XML elements
        darkener = findViewById(R.id.darkener);
        buttonCancelEditCategory = findViewById(R.id.button_cancel_edit_category);
        buttonEditCategory = findViewById(R.id.button_edit_category);
        bottomSheetEditCategory = findViewById(R.id.bottom_sheet_edit_category);
        chipGroupSelectedCategory = findViewById(R.id.chip_group_selected_category);
        chipGroupDeselectedCategory = findViewById(R.id.chip_group_deselected_category);

        sheetBehavior = BottomSheetBehavior.from(bottomSheetEditCategory);
        darkener.setVisibility(View.GONE);

        initEditCategoryUiListeners();
        genCategoryChips();

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

    private void initEditCategoryUiListeners() {
        // click event for show/dismiss bottom sheet
        buttonEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    showEditTagSheet();
                } else {
                    hideEditTagSheet();
                }
            }
        });
        buttonCancelEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("buttonCancelEditTag clicked");
                hideEditTagSheet();
            }
        });
    }

    public void onClickCategoryChip(String category) {
        if (selectedCategorys.contains(category)) {

        } else {

        }
    }

    private void genCategoryChips() {
        class CategoryChipOnClickListener implements View.OnClickListener {
            private String category;
            private CategoryChipListener listener;
            public CategoryChipOnClickListener(String category, CategoryChipListener listener) {
                this.category = category;
                this.listener = listener;
            }

            @Override
            public void onClick(View view) {
                listener.onClickCategoryChip(category);
            }
        }
        LinkedHashSet<Chip> chips = new LinkedHashSet<>();
        for (String category : categorys) {
            Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.layout_category_chip, null, false);
            chip.setText(mapCategoryToTitle.get(category));
            chips.add(chip);
            if (selectedCategorys.contains(category)) {
                chipGroupSelectedCategory.addView(chip);
            } else {
                chipGroupDeselectedCategory.addView(chip);
            }
        }
    }

    private void setCategoryChips(List<String> categorys) {
        for (String category : categorys) {
            Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.layout_category_chip, null, false);
            chip.setText(category);

        }
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
//        darkener.setVisibility(View.VISIBLE);
        linearLayoutMain.setClickable(false);
    }

    private void undarken() {
        darkener.setVisibility(View.GONE);
        linearLayoutMain.setClickable(true);
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



