package com.antoniorosario.geniuscodingchallenge.ui.userlist;

import android.support.v4.app.Fragment;

import com.antoniorosario.geniuscodingchallenge.ui.SingleFragmentActivity;

public class UserListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new UserListFragment();
    }
}
