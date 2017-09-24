package com.antoniorosario.geniuscodingchallenge.ui.userdetail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.antoniorosario.geniuscodingchallenge.R;
import com.antoniorosario.geniuscodingchallenge.data.UserDataSource;
import com.antoniorosario.geniuscodingchallenge.models.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class UserDetailFragment extends Fragment {

    private static final String ARG_USER_ID = "user_id";
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100;

    @BindView(R.id.user_detail_name) EditText userNameEditText;
    @BindView(R.id.user_detail_bio) EditText userBioEditText;
    @BindView(R.id.user_detail_avatar) ImageView userAvatarImageView;

    private User user;
    private File photoFile;

    public static UserDetailFragment newInstance(UUID userID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ID, userID);

        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID userID = (UUID) getArguments().getSerializable(ARG_USER_ID);
        user = UserDataSource.get(getActivity()).getUser(userID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        ButterKnife.bind(this, view);

        userNameEditText.setText(user.getName());
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userBioEditText.setText(user.getBio());
        userBioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setBio(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @OnClick(R.id.user_detail_avatar)
    public void onAvatarClicked() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);

            }
        }
        EasyImage.openChooserWithGallery(getActivity(), "Choose source", 0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted

                } else {
                    Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "onActivityResult", Toast.LENGTH_SHORT).show();

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                Toast.makeText(getActivity(), "onImagePickedError", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFile);
                Toast.makeText(getActivity(), "onImagePicked" + (imageFile == null), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                Toast.makeText(getActivity(), "onCanceled", Toast.LENGTH_SHORT).show();

                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(File returnedPhoto) {
        if (returnedPhoto == null || !returnedPhoto.exists()) {
            Toast.makeText(getActivity(), "onPhotosReturned if " + (photoFile == null), Toast.LENGTH_SHORT).show();

            userAvatarImageView.setImageDrawable(null);
        } else {
            Toast.makeText(getActivity(), "onPhotosReturned else", Toast.LENGTH_SHORT).show();

            Picasso.with(getActivity())
                    .load(returnedPhoto)
                    .fit()
                    .centerCrop()
                    .into(userAvatarImageView);

        }
    }

    @Override
    public void onDestroy() {
        EasyImage.clearConfiguration(getActivity());
        super.onDestroy();
    }
}
