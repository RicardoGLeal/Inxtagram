package com.example.inxtagram.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.inxtagram.Controllers.ActionsHelper;
import com.example.inxtagram.Post;
import com.example.inxtagram.R;
import com.parse.ParseFile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetailsFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetailsFragment extends Fragment {
    private Post post;
    private TextView tvUsername, tvDescription, tvLikesNumber, tvPostDate;
    private ImageView ivImage;
    private ImageView ivProfilePicture;
    private Button btnFavorite;

    //Constructor that receives a post.
    public PostDetailsFragment(Post post) {
        this.post = post;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvPostDate = view.findViewById(R.id.tvPostDate);
        ivImage = view.findViewById(R.id.ivImage);
        tvLikesNumber = view.findViewById(R.id.tvLikesNumber);
        ivProfilePicture = view.findViewById(R.id.ivProfileImage);
        btnFavorite = view.findViewById(R.id.btnFavorite);

        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvPostDate.setText(ActionsHelper.getRelativeTimeAgo(post.getCreatedAt().toString()));

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(getContext()).load(image.getUrl()).into(ivImage);
        }

        ParseFile profilePicture = post.getUser().getParseFile("profilePicture");
        RequestOptions circleProp = new RequestOptions();
        circleProp = circleProp.transform(new CircleCrop());

        Glide.with(getContext())
                .load(profilePicture!=null?profilePicture.getUrl(): R.drawable.profile_image_empty)
                .placeholder(R.drawable.profile_image_empty)
                .apply(circleProp)
                .into(ivProfilePicture);

        ActionsHelper.getLikesCount(post, tvLikesNumber);
        ActionsHelper.getLiked(post, btnFavorite);

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionsHelper.LikePost(post, btnFavorite, tvLikesNumber, getContext());
            }
        });
    }
}