package com.example.xinguannews.articlelist;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.xinguannews.article.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainFragmentAdapter extends FragmentStatePagerAdapter {
    private List<CardListFragment> fragments = new ArrayList<>();
    private final Context context;

    public MainFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    // 添加一个 Fragment
    public void addFragment(CardListFragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    public void removeFragment(CardListFragment fragment) {

        fragments.remove(fragment);
        notifyDataSetChanged();
    }

    public void removeFragmentByTitle(String title) {
        CardListFragment f = getFragmentByTitle(title);
        if (f != null) removeFragment(f);
    }

    public void removeFragmentByType(String type) {
        CardListFragment f = getFragmentByType(type);
        if (f != null) removeFragment(f);
    }

    // This is called when notifyDataSetChanged() is called
    @Override
    public int getItemPosition(Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
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
        return fragments.get(position).title;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public List<CardListFragment> getFragments() {
        return fragments;
    }

    public CardListFragment getFragmentByType(String type) {
        for (CardListFragment f : fragments) {
            if (f.type == type) return f;
        }
        return null;
    }

    public CardListFragment getFragmentByTitle(String title) {
        for (CardListFragment f : fragments) {
            if (f.title == title) return f;
        }
        return null;
    }
}