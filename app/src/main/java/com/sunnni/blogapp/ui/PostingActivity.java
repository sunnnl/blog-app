package com.sunnni.blogapp.ui;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.sunnni.blogapp.data.AppDatabase;
import com.sunnni.blogapp.data.dao.PostDaoAsyncTask;
import com.sunnni.blogapp.ui.base.BaseActivity;
import com.sunnni.blogapp.R;
import com.sunnni.blogapp.databinding.ActivityPostingBinding;
import com.sunnni.blogapp.data.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sunnni.blogapp.Code.APP_DB_COMMAND_INSERT;
import static com.sunnni.blogapp.Code.APP_DB_COMMAND_UPDATE;
import static com.sunnni.blogapp.Code.INTENT_KEY_POST_NO;

public class PostingActivity extends BaseActivity<ActivityPostingBinding> {

    private static final String TAG = PostingActivity.class.getSimpleName();

    private AppDatabase appDB;
    private int postNo;
    private Post post;

    private boolean titleFlag, contentFlag, isUpdate;

    class SetDataRunnable implements Runnable {
        @Override
        public void run() {
            try {
                post = appDB.postDao().getPostByNo(postNo);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.edtPostTitle.setText(post.title);
                        binding.edtPostContent.setText(post.content);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseView(getView(R.layout.activity_posting));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appDB = AppDatabase.getINSTANCE(this);

        Intent intent = getIntent();
        if (intent != null) {
            postNo = intent.getIntExtra(INTENT_KEY_POST_NO, -1);
            if (postNo == -1) {
                titleFlag = false;
                contentFlag = false;
                isUpdate = false;
            } else {
                titleFlag = true;
                contentFlag = true;
                isUpdate = true;

                SetDataRunnable setDataRunnable = new SetDataRunnable();
                Thread t = new Thread(setDataRunnable);
                t.start();
            }
        }
        setListener();
    }

    @Override
    protected void BaseView(@NonNull View view) {
        setContentView(view, ActivityPostingBinding.bind(view));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setListener() {
        binding.edtPostTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String cnt = charSequence.toString();
                titleFlag = cnt.length() > 0;
                if (titleFlag && contentFlag) {
                    binding.btnRegisterPost.setEnabled(true);
                } else {
                    binding.btnRegisterPost.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.edtPostContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String cnt = charSequence.toString();
                contentFlag = cnt.length() > 0;
                if (titleFlag && contentFlag) {
                    binding.btnRegisterPost.setEnabled(true);
                } else {
                    binding.btnRegisterPost.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.btnRegisterPost.setOnClickListener(view -> {

            String mTitle = binding.edtPostTitle.getText().toString();
            String mContent = binding.edtPostContent.getText().toString();

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String mDate = sdf.format(date);

            if (!isUpdate) {
                new PostDaoAsyncTask(appDB.postDao(), APP_DB_COMMAND_INSERT, 0, null)
                        .execute(new Post(mDate, mTitle, mContent));
            } else {
                new PostDaoAsyncTask(appDB.postDao(), APP_DB_COMMAND_UPDATE, postNo, new Post(mDate, mTitle, mContent)).execute();
            }

            setResult(RESULT_OK);
            finish();
        });
    }
}