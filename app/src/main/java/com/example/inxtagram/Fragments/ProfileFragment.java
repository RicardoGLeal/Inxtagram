package com.example.inxtagram.Fragments;

import android.util.Log;

import com.example.inxtagram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/*
Since the content displayed in the home fragment is almost the same as that displayed in the profile fragment,
ProfileFragment inherits from PostsFragment, which is more efficient than repeating code.
 */
public class ProfileFragment extends PostsFragment {

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
