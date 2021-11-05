package com.sunnni.blogapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sunnni.blogapp.data.model.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    LiveData<List<Post>> getAll();

    @Query("SELECT * FROM post")
    List<Post> getAllByList();

    @Query("SELECT * FROM post WHERE `no`=:pNo")
    Post getPostByNo(int pNo);

    @Insert
    void insertPost(Post post);

    @Query("UPDATE post SET title=:mTitle WHERE `no`=:pNo")
    void updatePostTitle(int pNo, String mTitle);

    @Query("UPDATE post SET content=:mContent WHERE `no`=:pNo")
    void updatePostContent(int pNo, String mContent);

    @Query("UPDATE post SET date=:mDate WHERE `no`=:pNo")
    void updatePostDate(int pNo, String mDate);

    @Delete
    void deletePost(Post post);

    @Query("DELETE FROM post")
    void clearAll();
}
