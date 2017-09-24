package com.antoniorosario.geniuscodingchallenge.ui.userdetail;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.antoniorosario.geniuscodingchallenge.R;
import com.antoniorosario.geniuscodingchallenge.data.UserDataSource;
import com.antoniorosario.geniuscodingchallenge.models.User;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailActivity extends AppCompatActivity {

    private static final String EXTRA_USER_ID = "user_id";

    @BindView(R.id.view_pager) ViewPager viewPager;
    private List<User> users;

    public static Intent newIntent(Context packageContext, UUID userID) {
        Intent intent = new Intent(packageContext, UserDetailActivity.class);
        intent.putExtra(EXTRA_USER_ID, userID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pager);

        ButterKnife.bind(this);
        UUID userID = (UUID) getIntent()
                .getSerializableExtra(EXTRA_USER_ID);


        users = UserDataSource.get(this).getUsers();
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                User user = users.get(position);
                return UserDetailFragment.newInstance(user.getId());
            }

            @Override
            public int getCount() {
                return users.size();
            }
        });

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userID)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
