package com.wordpress.nikant20.milkdiary.View.UiModule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nikant20 on 11/9/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        public UserViewHolder(View itemView) {
            super(itemView);
        }
    }
}
