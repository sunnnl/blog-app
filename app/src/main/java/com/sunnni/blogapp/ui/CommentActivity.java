package com.sunnni.blogapp.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.sunnni.blogapp.data.AppDatabase;
import com.sunnni.blogapp.data.PreferenceManager;
import com.sunnni.blogapp.data.dao.CommentDaoAsyncTask;
import com.sunnni.blogapp.data.model.Comment;
import com.sunnni.blogapp.ui.adapter.CommentAdapter;
import com.sunnni.blogapp.ui.base.BaseActivity;
import com.sunnni.blogapp.R;
import com.sunnni.blogapp.databinding.ActivityCommentBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.sunnni.blogapp.Code.APP_DB_COMMAND_INSERT;
import static com.sunnni.blogapp.Code.INTENT_KEY_POST_NO;

public class CommentActivity extends BaseActivity<ActivityCommentBinding> {

    private static final String TAG = CommentActivity.class.getSimpleName();

    private AppDatabase appDB;

    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private int postNo;

    class GetCommentListRunnable implements Runnable {
        @Override
        public void run() {
            try {
                commentList = appDB.commentDao().getAllByList(postNo);
                commentAdapter = new CommentAdapter(commentList);
                binding.rvComment.setAdapter(commentAdapter);
                commentAdapter.notifyDataSetChanged();

                if (commentList.isEmpty()) {
                    binding.tvNoComment.setVisibility(View.VISIBLE);
                    binding.rvComment.setVisibility(View.GONE);
                } else {
                    binding.tvNoComment.setVisibility(View.GONE);
                    binding.rvComment.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseView(getView(R.layout.activity_comment));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appDB = AppDatabase.getINSTANCE(this);
//        appDB.commentDao().getAll().observe(this, dataList -> {
//            binding.tvTest.setText(dataList.toString());
//        });

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.rvComment.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(getApplicationContext()).getOrientation());
        binding.rvComment.addItemDecoration(dividerItemDecoration);

        Intent intent = getIntent();
        if (intent != null) {
            postNo = intent.getIntExtra(INTENT_KEY_POST_NO, -1);
            if (postNo == -1) {
                finish();
            }

            GetCommentListRunnable getCommentListRunnable = new GetCommentListRunnable();
            Thread t = new Thread(getCommentListRunnable);
            t.start();
        }

        setListener();
    }

    @Override
    protected void BaseView(@NonNull View view) {
        setContentView(view, ActivityCommentBinding.bind(view));
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
        binding.btnRegisterComment.setOnClickListener(view -> {
            String mContent = binding.edtComment.getText().toString();
            String mWriter = PreferenceManager.getUserName(getApplicationContext());

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String mDate = sdf.format(date);

            Comment comment = new Comment(mDate, mWriter, mContent, postNo);

            if (!mContent.equals("")) {
                new CommentDaoAsyncTask(appDB.commentDao(), APP_DB_COMMAND_INSERT)
                        .execute(comment);
                if (commentAdapter != null) {
                    commentAdapter.addComment(comment);
                    commentAdapter.notifyDataSetChanged();
                    if (commentAdapter.getItemCount() > 0) {
                        binding.tvNoComment.setVisibility(View.GONE);
                        binding.rvComment.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvNoComment.setVisibility(View.VISIBLE);
                        binding.rvComment.setVisibility(View.GONE);
                    }
                }
            }
            binding.edtComment.getText().clear();
        });
    }
}