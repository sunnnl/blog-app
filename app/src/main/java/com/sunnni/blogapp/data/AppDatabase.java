package com.sunnni.blogapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sunnni.blogapp.data.dao.CommentDao;
import com.sunnni.blogapp.data.dao.PostDao;
import com.sunnni.blogapp.data.model.Comment;
import com.sunnni.blogapp.data.model.Post;

@Database(
        entities = {
                Post.class,
                Comment.class
        },
        version = 2
)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE = null;
    public abstract PostDao postDao();
    public abstract CommentDao commentDao();

    public static AppDatabase getINSTANCE(Context ctx){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(), AppDatabase.class, "app.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
