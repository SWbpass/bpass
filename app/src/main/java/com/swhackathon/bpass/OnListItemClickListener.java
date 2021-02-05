package com.swhackathon.bpass;

import android.view.View;

public interface OnListItemClickListener {
    public void onItemClick(ListPersonAdapter.ViewHolder holder, View view, int position);
}
