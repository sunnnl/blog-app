package com.sunnni.blogapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.sunnni.blogapp.data.model.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment")
    LiveData<List<Comment>> getAll();

    @Query("SELECT * FROM comment WHERE `postNo`=:pNo ORDER BY id")
    List<Comment> getAllByList(int pNo);

    @Insert
    void insertComment(Comment comment);
}
