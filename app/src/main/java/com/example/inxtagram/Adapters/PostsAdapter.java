package com.example.inxtagram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.inxtagram.Controllers.ActionsHelper;
import com.example.inxtagram.Fragments.PostDetailsFragment;
import com.example.inxtagram.Fragments.ProfileFragment;
import com.example.inxtagram.Post;
import com.example.inxtagram.R;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    /**
     * Clean all elements of the recycler
     */
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvUsername, tvDescription, tvLikesNumber, tvPostDate;
        private ImageView ivImage;
        private ImageView ivProfilePicture;
        private Button btnFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLikesNumber = itemView.findViewById(R.id.tvLikesNumber);
            tvPostDate = itemView.findViewById(R.id.tvPostDate);
            ivProfilePicture = itemView.findViewById(R.id.ivProfileImage);

            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            //Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvPostDate.setText(ActionsHelper.getRelativeTimeAgo(post.getCreatedAt().toString()));
            ParseFile image = post.getImage();

            //condition to check if there is an image attached
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            ParseFile profilePicture = post.getUser().getParseFile("profilePicture");
            RequestOptions circleProp = new RequestOptions();
            circleProp = circleProp.transform(new CircleCrop());
            Glide.with(context)
                        .load(profilePicture!=null?profilePicture.getUrl(): R.drawable.profile_image_empty)
                        .placeholder(R.drawable.profile_image_empty)
                        .apply(circleProp)
                        .into(ivProfilePicture);

            ActionsHelper.getLikesCount(post, tvLikesNumber);
            ActionsHelper.getLiked(post, btnFavorite);

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActionsHelper.LikePost(post, btnFavorite, tvLikesNumber, context);
                }
            });

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToProfile(post);
                }
            });

            ivProfilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToProfile(post);
                }
            });
        }

        /**
         * Function that goes to the profile of the user clicked.
         * @param post post opened
         */
        private void goToProfile(Post post) {
            AppCompatActivity activity = (AppCompatActivity) context;
            Fragment fragment = new ProfileFragment(post.getUser());
            activity.getSupportFragmentManager();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
        }

        /**
         * OnClickListener of each post. It opens the PostDetailsFragment with the details of the post clicked.
         * @param v
         */
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment fragment = new PostDetailsFragment(posts.get(position));
                ((AppCompatActivity) context).getSupportFragmentManager();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        }
    }
}
