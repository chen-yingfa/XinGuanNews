package com.example.liuzijia;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.liuzijia.api.EpidemicApiThread;
import com.example.liuzijia.api.EpidemicApiThreadListener;
import com.example.liuzijia.articlelist.CardListFragment;
import com.example.liuzijia.articlelist.CardListFragmentAdapter;
import com.example.liuzijia.query.QueriesRecyclerViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class HomeFragment
        extends Fragment
        implements
        SwipeRefreshLayout.OnRefreshListener,
        EpidemicApiThreadListener,
        CategoryChipListener,
        SearchView.OnQueryTextListener,
        SearchView.OnCloseListener,
        SearchView.OnFocusChangeListener,
        QueriesRecyclerViewAdapter.ItemClickListener {

    private ImageButton buttonEditCategory;
    private CardListFragmentAdapter cardListFragmentAdapter;

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

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    // init methods（同时获得 XML 元素）
    private void init() {
        // 初始化
        // 注：顺序不要变

        // load from SharedPreferences
        System.out.println("loading from " + sharedPrefKeySelectedCategories);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> savedSelectedCategories = sharedPref.getStringSet(sharedPrefKeySelectedCategories, null);
        System.out.println(savedSelectedCategories);
        if (savedSelectedCategories != null) {
            selectedCategories = new LinkedHashSet<>();
            for (String c : savedSelectedCategories) {
                selectedCategories.add(c);
            }
        }

        initCategories();
        initEditCategoryUi();
        initTabLayout();
        initSearchBar();
    }

    private void initTabLayout() {
        View view = getView();
        // Setup tab layout
        cardListFragmentAdapter = new CardListFragmentAdapter(getContext(), getFragmentManager());

        for (String category : selectedCategories) {
            addTab(category);
        }

        tabLayout = view.findViewById(R.id.tab_layout_fragment_home);
        viewPager = view.findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(cardListFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    // 初始化所有类型的标题（中文名称），以及相关成员变量
    private void initCategories() {
        // set type titles
        mapCategoryToTitle.put(categoryAll, categoryTitleAll);
        mapCategoryToTitle.put(categoryNews, categoryTitleNews);
        mapCategoryToTitle.put(categoryPaper, categoryTitlePaper);
        mapCategoryToTitle.put(categoryEvent, categoryTitleEvent);
        categories.add(categoryAll);
        categories.add(categoryNews);
        categories.add(categoryPaper);
        categories.add(categoryEvent);
        if (selectedCategories == null) { // 不要重复初始化
            // 默认有两类：所有文章，新闻
            selectedCategories = new LinkedHashSet<String>(); // 首次赋值
            selectedCategories.add(categoryAll);
            selectedCategories.add(categoryNews);
        }
    }

    // 初始化用于修改类型标签的UI
    private void initEditCategoryUi() {
        View view = getView();
        // get XML elements
        buttonCancelEditCategory = view.findViewById(R.id.button_cancel_edit_category);
        buttonEditCategory = view.findViewById(R.id.button_edit_category);
        bottomSheetEditCategory = view.findViewById(R.id.bottom_sheet_edit_category);
        chipGroupSelectedCategory = view.findViewById(R.id.chip_group_selected_category);
        chipGroupDeselectedCategory = view.findViewById(R.id.chip_group_deselected_category);

        sheetBehavior = BottomSheetBehavior.from(bottomSheetEditCategory);

        initEditCategoryUiListeners();
        genCategoryChips();
    }

    private void initEditCategoryUiListeners() {
        // 点击修改类型
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
        // 退出修改类型
        buttonCancelEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEditTagSheet();
            }
        });

        // 针对 BottomSheet 状态发生变化的监听器
        // 自定子类以将目前的 Activity 作为参数串给监听器
        class CategoryChipBottomSheetCallback extends BottomSheetBehavior.BottomSheetCallback {
            CategoryChipListener listener;

            public CategoryChipBottomSheetCallback(CategoryChipListener listener) {
                this.listener = listener;
            }

            // 状态发生变化
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    listener.onConfirmCategorySelection();
                }
            }

            // 用户划了屏幕
            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        }
        // 连接监听器
        sheetBehavior.setBottomSheetCallback(new CategoryChipBottomSheetCallback(this));
    }

    @Override
    public void onClickCategoryChip(String category) {
        System.out.println("onClickCategoryChip");
        Chip chip = categoryChips.get(category);
        if (selectedCategoryChips.containsValue(chip)) {
            selectedCategoryChips.remove(category);
            chipGroupSelectedCategory.removeView(chip); // NOTE: remove before adding
            chipGroupDeselectedCategory.addView(chip);
        } else {
            selectedCategoryChips.put(category, chip);
            chipGroupDeselectedCategory.removeView(chip); // NOTE: remove before adding
            chipGroupSelectedCategory.addView(chip);
        }
    }

    @Override
    public void onCategorySelectionChanged(String category) {

    }

    @Override
    public void onConfirmCategorySelection() {
        Set<String> curSelectedCategories = selectedCategoryChips.keySet();

        // temporary set of category that have been removed and should be removed from selectedCategorys
        // need this because we can't change the set during iteration
        Set<String> toRm = new HashSet<String>();
        // handle newly removed categories
        for (String category : selectedCategories) {
            if (!curSelectedCategories.contains(category)) {
                System.out.println(category);
                cardListFragmentAdapter.removeFragmentByType(category);
                toRm.add(category);
            }
        }
        for (String s : toRm) selectedCategories.remove(s);

        // handle newly added categories
        for (String category : curSelectedCategories) {
            if (!selectedCategories.contains(category)) {
                System.out.println(category);
                // Added new category => add new tab
                addTab(category);
//                cardListPagerAdapter.getFragmentByType(category).onRefresh();
                selectedCategories.add(category);
            }
        }

        // save selected categories
        System.out.println(selectedCategories);
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(sharedPrefKeySelectedCategories, selectedCategories);
        editor.commit();
    }

    // 根据此类的成员变量 categories, 生成相应的 CategoryChip，并添加到相应 ChipGroup 里。
    // 同时，对其添加一个 onClick 监听器
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
        categoryChips = new LinkedHashMap<>();
        selectedCategoryChips = new LinkedHashMap<>();
        for (String category : categories) {
            Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.layout_category_chip, null, false);
            chip.setText(mapCategoryToTitle.get(category));
            categoryChips.put(category, chip);
            if (selectedCategories.contains(category)) {
                chipGroupSelectedCategory.addView(chip);
                selectedCategoryChips.put(category, chip);
            } else {
                chipGroupDeselectedCategory.addView(chip);
            }

            // set onClickListener
            chip.setOnClickListener(new CategoryChipOnClickListener(category, this));
        }
    }

    private void setCategoryChips(List<String> categorys) {
        for (String category : categorys) {
            Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.layout_category_chip, null, false);
            chip.setText(category);

        }
    }

    private void showEditTagSheet() {
        setFragmentsClickable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void hideEditTagSheet() {
        setFragmentsClickable(true);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void addTab(String category) {
        String title = mapCategoryToTitle.get(category);
        cardListFragmentAdapter.addFragment(new CardListFragment(category, title));
    }

    private void setFragmentsClickable(boolean b) {
        for (CardListFragment fragment : cardListFragmentAdapter.getFragments()) {
            fragment.setClickable(b);
        }
    }

    @Override
    public void onFetchedArticles(EpidemicApiThread thread) {
        for (CardListFragment fragment : cardListFragmentAdapter.getFragments()) {
            fragment.onFetchedArticles(thread);
        }
    }

    @Override
    public void onFetchedEpidemicData(EpidemicApiThread thread) {

    }

    @Override
    public void onFetchedEntity(EpidemicApiThread thread) {}

    @Override
    public void onRefresh() {
        for (CardListFragment fragment : cardListFragmentAdapter.getFragments()) {
            fragment.onRefresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().findViewById(R.id.root_fragment_home).requestFocus();
        loadQuery();
        for (CardListFragment fragment : cardListFragmentAdapter.getFragments()) {
            fragment.notifyDataSetChanged();
        }
    }
    ///////////////////////////////////////////////////////
    //             methods related to querying
    ///////////////////////////////////////////////////////

    // related to querying
    private int maxQueryCnt = 20;
    private SearchView searchBar;
    private List<String> queries = new ArrayList<String>();
    private QueriesRecyclerViewAdapter queriesRecyclerViewAdapter;
    private RecyclerView queriesRecyclerView;

    private boolean querying = false;

    private void initSearchBar() {
        searchBar = getView().findViewById(R.id.search_view);
        loadQueriesFromPref();
//        saveQueryToPref("");

        initQueriesRecyclerView();
        searchBar.setOnQueryTextListener(this);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setIconified(false);

                // show queries recycler view
                showQueriesRecyclerView();
            }
        });

        searchBar.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View arg0) {
                doQuery("");
            }

            @Override
            public void onViewAttachedToWindow(View arg0) {
                // search was opened
            }
        });
    }

    public void initQueriesRecyclerView() {
        queriesRecyclerView = getView().findViewById(R.id.queries_recycler_list);
        hideQueriesRecyclerView();
        queriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        queriesRecyclerViewAdapter = new QueriesRecyclerViewAdapter(queries, this, getContext()); // bind data (list of json_articles) to Adapter
        queriesRecyclerView.setAdapter(queriesRecyclerViewAdapter);                        // bind RecyclerView and Adapter
    }

    private void hideQueriesRecyclerView() {
        System.out.println("hideQueriesRecyclerView");
        searchBar.clearFocus();
        getView().findViewById(R.id.toolbar_fragment_home).requestFocus();
        getView().findViewById(R.id.toolbar_fragment_home).requestFocus();
//        queriesRecyclerView.setEnabled(false);
        queriesRecyclerView.setVisibility(View.GONE);
    }

    private void showQueriesRecyclerView() {
//        queriesRecyclerView.setEnabled(true);
        queriesRecyclerView.setVisibility(View.VISIBLE);
    }

    public void loadQuery() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String query = pref.getString("query", null);
        if (query != null) {
            searchBar.setQuery(query, true);
            searchBar.clearFocus();
        }
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (querying) {
            return true;
        }
        if (query.equals("")) {
            querying = true;
            this.onQueryTextSubmit("");
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("onQueryTextSubmit");
        hideQueriesRecyclerView();
        if (query == null) {
            query = "";
        }
        doQuery(query);
        return true;
    }

    @Override
    public boolean onClose() {
        System.out.println("onClose");
        doQuery("");
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean var) {
        System.out.println("onFocusChange");
        if (var) {
            showQueriesRecyclerView();
        } else {
//            hideQueriesRecyclerView();
        }
    }

    public void addQuery(String query) {
        if (query.equals("")) {
            return;
        }
        if (queries.size() >= maxQueryCnt) {
            queries.remove(queries.size() - 1);
        }
        // check if contains
        for (String q : queries) {
            if (q.equals(query)) {
                return;
            }
        }
        queries.add(0, query);
    }

    public void updateQuery(String query) {
        for (CardListFragment fragment : cardListFragmentAdapter.getFragments()) {
            fragment.updateQuery(query);
        }
    }

    public void doQuery(String query) {
        addQuery(query);
        saveQueriesToPref();
        saveQueryToPref(query);
        hideQueriesRecyclerView();
        searchBar.setQuery(query, false);
        updateQuery(query);
        querying = false;
    }

    public void saveQueryToPref(String query) {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("query", query);
        editor.commit();
    }

    public void saveQueriesToPref(){
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Set<String> set = new TreeSet<>(queries);
        editor.putStringSet("queries", set);
        editor.commit();
    }

    public void loadQueriesFromPref() {
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> set = pref.getStringSet("queries", new HashSet<String>());
        queries.clear();
        for (String s : set) {
            queries.add(s);
        }
    }

    @Override
    public void onItemClick(String text) {
//        System.out.println("onItemClick " + pos);
//        String query = queries.get(pos);
        doQuery(text);
    }
}