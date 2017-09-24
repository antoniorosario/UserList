package com.antoniorosario.geniuscodingchallenge.ui.userlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antoniorosario.geniuscodingchallenge.R;
import com.antoniorosario.geniuscodingchallenge.data.UserDataSource;
import com.antoniorosario.geniuscodingchallenge.models.User;
import com.antoniorosario.geniuscodingchallenge.ui.SimpleItemDecoration;
import com.antoniorosario.geniuscodingchallenge.ui.touchhelper.OnStartDragListener;
import com.antoniorosario.geniuscodingchallenge.ui.touchhelper.SimpleItemTouchHelperCallback;
import com.antoniorosario.geniuscodingchallenge.ui.userdetail.UserDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserListFragment extends Fragment implements OnStartDragListener, UserDeletedListener {

    @BindView(R.id.user_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_view) TextView emptyView;

    private UserAdapter userAdapter;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleItemDecoration(getActivity()));
        updateUI();

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(userAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_user_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_user:
                User user = new User();
                UserDataSource.get(getActivity()).addUser(user);
                Intent intent = UserDetailActivity
                        .newIntent(getActivity(), user.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateUI() {
        UserDataSource userDataSource = UserDataSource.get(getActivity());
        List<User> users = userDataSource.getUsers();
        checkForEmptyState(users);
        if (userAdapter == null) {
            userAdapter = new UserAdapter(users, getActivity(), this);
            recyclerView.setAdapter(userAdapter);
        } else {
            userAdapter.notifyDataSetChanged();
        }
    }

    private void checkForEmptyState(List<User> users) {
        if (users == null || users.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);

        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);

    }

    @Override
    public void onUserDeleted() {
        updateUI();
    }
}
