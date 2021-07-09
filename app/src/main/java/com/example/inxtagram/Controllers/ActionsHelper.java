package com.example.inxtagram.Controllers;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inxtagram.Post;
import com.example.inxtagram.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ActionsHelper {
    public static final String TAG = "ActionsHelper";

    public static void LikePost(Post post, Button btnFavorite, TextView tvLikesNumber, Context context) {
        ParseUser user = ParseUser.getCurrentUser();
        if (!post.isLiked())
        {
            post.addLike(user.getObjectId());
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error while saving, e");
                        Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "Post save was successful!");
                    btnFavorite.setBackgroundResource(R.drawable.ufi_heart_active);
                    tvLikesNumber.setText(String.valueOf(post.getLikesNumber()));
                }
            });
        }
        else {
            post.removeLike(user.getObjectId());
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error while saving, e");
                        Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "Post save was successful!");
                    btnFavorite.setBackgroundResource(R.drawable.ufi_heart);
                    tvLikesNumber.setText(String.valueOf(post.getLikesNumber()));
                }
            });
        }
    }

    public static void getLiked(Post post, Button btnFavorite) {
        if (post.isLiked())
            btnFavorite.setBackgroundResource(R.drawable.ufi_heart_active);
        else
            btnFavorite.setBackgroundResource(R.drawable.ufi_heart);
    }

    public static void getLikesCount(Post post, TextView tvLikesNumber) {
        tvLikesNumber.setText(String.valueOf(post.getLikesNumber()));
    }

    public static String getRelativeTimeAgo(String rawDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
