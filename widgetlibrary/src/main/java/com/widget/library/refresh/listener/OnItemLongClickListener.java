package com.widget.library.refresh.listener;

import android.view.View;

import com.widget.library.refresh.familiarrecyclerview.FamiliarRecyclerView;

public interface OnItemLongClickListener {
    boolean onItemLongClick(FamiliarRecyclerView familiarRecyclerView, View view, int position);
}