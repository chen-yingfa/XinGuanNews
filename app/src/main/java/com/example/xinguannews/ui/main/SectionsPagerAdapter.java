package com.example.xinguannews.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.xinguannews.article.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<String> tabTitles = new ArrayList<>();
    private List<ArticleFragment> fragments = new ArrayList<>();
    private final Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    // 在所有 Fragment 中添加一个给定文章
    public void addArticle(Article article) {
        System.out.println("addArticle in SectionsPagerAdapter");
        for (ArticleFragment f : fragments) {
//            System.out.println("articleFragment.addArticleCard(article)");
            f.addArticleCard(article);
        }
    }

    // 添加一个 Fragment
    public void addFragment(ArticleFragment fragment, String title) {
        fragments.add(fragment);
        tabTitles.add(title);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return fragments.size();
    }

    public List<ArticleFragment> getFragments() {
        return fragments;
    }
}