package com.antoniorosario.geniuscodingchallenge.ui.userlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.antoniorosario.geniuscodingchallenge.R;
import com.antoniorosario.geniuscodingchallenge.data.UserDataSource;
import com.antoniorosario.geniuscodingchallenge.models.User;
import com.antoniorosario.geniuscodingchallenge.ui.touchhelper.ItemTouchHelperAdapter;
import com.antoniorosario.geniuscodingchallenge.ui.userdetail.UserDetailActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private UserDeletedListener userDeletedListener;
    private List<User> users;
    private User currentUser;


    public UserAdapter(List<User> users, Context context, UserDeletedListener userDeletedListener) {
        this.users = users;
        this.context = context;
        this.userDeletedListener = userDeletedListener;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        currentUser = users.get(position);
        holder.bindUser(currentUser);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(users, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        UserDataSource userDataSource = UserDataSource.get(context);
        userDataSource.deleteUser(currentUser);
        notifyItemRemoved(position);
        userDeletedListener.onUserDeleted();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.user_avatar_list) ImageView userAvatarImageView;
        @BindView(R.id.user_bio_list) TextView userBioTextView;
        @BindView(R.id.user_name_list) TextView userNameTextView;

        private User user;
        private Context context;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindUser(User user) {
            this.user = user;
            String userBio = user.getBio();
            String userName = user.getName();
            // Use databinding and its null coalescing operator
            if (userBio != null) {
                userBioTextView.setText(user.getBio());
            } else {
                userBioTextView.setText(R.string.null_bio_string);
            }
            if (userName != null) {
                userNameTextView.setText(user.getName());
            } else {
                userNameTextView.setText(R.string.null_user_name_string);
            }
            userAvatarImageView.setImageResource(user.getAvatarImageResID());
        }

        @Override
        public void onClick(View v) {
            Intent intent = UserDetailActivity.newIntent(context, user.getId());
            context.startActivity(intent);
        }
    }
}


