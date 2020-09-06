package com.example.xinguannews;

import android.view.View;

public interface CategoryChipListener {
    void onClickCategoryChip(String category);
    void onCategorySelectionChanged(String category);
    void onConfirmCategorySelection();
}
