package com.sunnni.blogapp.data.dao;

import android.os.AsyncTask;

import com.sunnni.blogapp.data.model.Comment;

import static com.sunnni.blogapp.Code.APP_DB_COMMAND_INSERT;

public class CommentDaoAsyncTask extends AsyncTask<Comment, Void, Void> {
    private CommentDao mCommentDao;
    private String mCommand;

    public CommentDaoAsyncTask(CommentDao commentDao, String command){
        this.mCommentDao = commentDao;
        this.mCommand = command;
    }

    @Override
    protected Void doInBackground(Comment... comments) {
        if (mCommand.equals(APP_DB_COMMAND_INSERT)) {
            mCommentDao.insertComment(comments[0]);
        }
        return null;
    }
}
