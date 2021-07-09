package com.example.inxtagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_LIKED_BY = "likedBy";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public int getLikesNumber() {
        List<String> list = getList(KEY_LIKED_BY);
        if (list != null)
            return list.size();
        else
            return 0;
    }

    public void addLike(String id) {
        List<String> list = getList(KEY_LIKED_BY);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(id);
        setLikedBy(list);
    }

    public boolean isLiked() {
        List<String> list = getList(KEY_LIKED_BY);
        return list!=null && list.contains(ParseUser.getCurrentUser().getObjectId());
    }

    private void setLikedBy(List<String> list) {
        put(KEY_LIKED_BY, list);
    }

    public void removeLike(String id){
        List<String> list = getList(KEY_LIKED_BY);
        if(list==null)
            list = new ArrayList<>();
        list.remove(id);
        setLikedBy(list);
    }
}
