package com.antoniorosario.geniuscodingchallenge.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.antoniorosario.geniuscodingchallenge.R;


public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.fragment_container, createFragment())
                    .commit();
        }
    }
}
