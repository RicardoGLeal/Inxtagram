package com.example.inxtagram.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.inxtagram.Adapters.PostsProfileAdapter;
import com.example.inxtagram.Controllers.ActionsHelper;
import com.example.inxtagram.LoginActivity;
import com.example.inxtagram.MainActivity;
import com.example.inxtagram.Post;
import com.example.inxtagram.R;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/*
Since the content displayed in the home fragment is almost the same as that displayed in the profile fragment,
ProfileFragment inherits from PostsFragment, which is more efficient than repeating code.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    public String photoFileName = "photo.jpg";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private TextView tvUserName;
    private RecyclerView rvPosts;
    protected PostsProfileAdapter adapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private ImageView ivProfile;
    private Button btnLogout, btnCaptureImage;
    private ParseUser user;
    private File photoFile;

    public ProfileFragment(ParseUser user){
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfilePicture();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName = view.findViewById(R.id.tvUsername);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivProfile = view.findViewById(R.id.ivProfileImage);

        loadProfilePicture();

        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();
        swipeContainer = view.findViewById(R.id.swipeContainer);

        tvUserName.setText(user.getUsername());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getContext(), "Log out successful!", Toast.LENGTH_SHORT).show();
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        activity.finish();
                    }
                });
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        //OnClickListener implemented when the user pulls to refresh.
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                allPosts.clear();
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        adapter = new PostsProfileAdapter(getContext(), allPosts);

        //Steps to use the recycler view:
        //0. create layout for one row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        //4. set the layout manager on the recycler view
        rvPosts.setLayoutManager(new GridLayoutManager(getContext(),3));
        queryPosts();
    }

    private void loadProfilePicture() {
        RequestOptions circleProp = new RequestOptions();
        circleProp = circleProp.transform(new CircleCrop());
        ParseFile profilePicture = user.getParseFile("profilePicture");
        Glide.with(getContext())
                .load(profilePicture!=null?profilePicture.getUrl(): R.drawable.profile_image_empty)
                .placeholder(R.drawable.profile_image_empty)
                .apply(circleProp)
                .into(ivProfile);
    }

    /**
     * This function queries the posts.
     */
    protected void queryPosts() {
        //Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //include the user of the post
        query.include(Post.KEY_USER);
        //get only the queries of the user logged in.
        query.whereEqualTo(Post.KEY_USER, user);
        //Limiting the number of posts getting back.
        query.setLimit(20);
        //the items created most recently will come first and the oldest ones will come last.
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        // Retrieve all the posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post: posts) {
                    Log.i(TAG, "Posts: " + post.getDescription());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { //If the image took the picture..
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                //update the image in the database
                user.put("profilePicture", new ParseFile(photoFile));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        loadProfilePicture();
                        Toast.makeText(getContext(), "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
