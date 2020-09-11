package com.example.liuzijia.api;

public interface EpidemicApiThreadListener {
    void onFetchedArticles(final EpidemicApiThread thread);
    void onFetchedEpidemicData(final EpidemicApiThread thread);
    void onFetchedEntity(final EpidemicApiThread thread);
}
