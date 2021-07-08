package com.example.inxtagram.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.inxtagram.Post;
import com.example.inxtagram.Adapters.PostsAdapter;
import com.example.inxtagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/*
Since the content displayed in the home fragment is almost the same as that displayed in the profile fragment,
ProfileFragment inherits from PostsFragment, which is more efficient than repeating code.
 */
public class ProfileFragment extends PostsFragment {

    public static final String TAG = "ProfileFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private ImageView ivProfile;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfile = view.findViewById(R.id.ivProfileImage);

        RequestOptions circleProp = new RequestOptions();
        circleProp = circleProp.transform(new CircleCrop());
        Glide.with(getContext())
                .load("https://st.depositphotos.com/1779253/5140/v/600/depositphotos_51405259-stock-illustration-male-avatar-profile-picture-use.jpg")
                .apply(circleProp)
                .into(ivProfile);

        rvPosts = view.findViewById(R.id.rvPosts);
        allPosts = new ArrayList<>();
        swipeContainer = view.findViewById(R.id.swipeContainer);
        /**
         * OnClickListener implemented when the user pulls to refresh.
         */
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

        adapter = new PostsAdapter(getContext(), allPosts);

        //Steps to use the recycler view:
        //0. create layout for one row in the list
        //1. create the adapter
        //2. create the data source
        //3. set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        //4. set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
    }


    @Override
    protected void queryPosts() {
        //Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        //include the user of the post
        query.include(Post.KEY_USER);
        //get only the queries of the user logged in.
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
}
