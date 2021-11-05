package com.sunnni.blogapp.data.dao;

import android.os.AsyncTask;

import com.sunnni.blogapp.data.model.Post;

import static com.sunnni.blogapp.Code.APP_DB_COMMAND_CLEAR;
import static com.sunnni.blogapp.Code.APP_DB_COMMAND_DELETE;
import static com.sunnni.blogapp.Code.APP_DB_COMMAND_INSERT;
import static com.sunnni.blogapp.Code.APP_DB_COMMAND_UPDATE;

public class PostDaoAsyncTask extends AsyncTask<Post, Void, Void> {
    private PostDao mPostDao;
    private String mType;
    private int mNo;
    private Post mPost;

    public PostDaoAsyncTask(PostDao postDao, String type, int no, Post post) {
        this.mPostDao = postDao;
        this.mType = type;
        this.mNo = no;
        this.mPost = post;
    }

    @Override
    protected Void doInBackground(Post... dataModels) {
        if (mType.equals(APP_DB_COMMAND_INSERT)) {
            mPostDao.insertPost(dataModels[0]);
        } else if (mType.equals(APP_DB_COMMAND_UPDATE)) {
            if (mPostDao.getPostByNo(mNo) != null) {
                mPostDao.updatePostTitle(mNo, mPost.title);
                mPostDao.updatePostContent(mNo, mPost.content);
                mPostDao.updatePostDate(mNo, mPost.date);
            }
        } else if (mType.equals(APP_DB_COMMAND_DELETE)) {
            if (mPostDao.getPostByNo(mNo) != null) {
                mPostDao.deletePost(mPostDao.getPostByNo(mNo));
            }
        } else if (mType.equals(APP_DB_COMMAND_CLEAR)) {
            mPostDao.clearAll();
        }
        return null;
    }
}