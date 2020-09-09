package com.example.xinguannews.api;

public interface EpidemicApiThreadListener {
    void onFetchedArticles(final EpidemicApiThread thread);
    void onFetchedEpidemicData(final EpidemicApiThread thread);
}
