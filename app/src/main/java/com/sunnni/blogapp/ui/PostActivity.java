package com.sunnni.blogapp.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.sunnni.blogapp.data.AppDatabase;
import com.sunnni.blogapp.data.dao.PostDaoAsyncTask;
import com.sunnni.blogapp.data.PreferenceManager;
import com.sunnni.blogapp.data.model.Post;
import com.sunnni.blogapp.ui.base.BaseActivity;
import com.sunnni.blogapp.R;
import com.sunnni.blogapp.databinding.ActivityPostBinding;

import static com.sunnni.blogapp.Code.APP_DB_COMMAND_DELETE;
import static com.sunnni.blogapp.Code.INTENT_KEY_POST_NO;

public class PostActivity extends BaseActivity<ActivityPostBinding> {

    private static final String TAG = PostActivity.class.getSimpleName();

    private ActivityResultLauncher<Intent> resultLauncher;
    private AppDatabase appDB;
    private int postNo;
    private Post post;

    class SetDataRunnable implements Runnable {
        @Override
        public void run() {
            try {
                post = appDB.postDao().getPostByNo(postNo);
                runOnUiThread(() -> {
                    binding.tvPostTitle.setText(post.title);
                    binding.tvPostDate.setText(post.date);
                    binding.tvPostContent.setText(post.content);
                });
            }
            catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseView(getView(R.layout.activity_post));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appDB = AppDatabase.getINSTANCE(this);

        Intent intent = getIntent();
        if (intent != null){
            postNo = intent.getIntExtra(INTENT_KEY_POST_NO, -1);
            if (postNo == -1){
                finish();
            }
            SetDataRunnable setDataRunnable = new SetDataRunnable();
            Thread t = new Thread(setDataRunnable);
            t.start();

            String user = PreferenceManager.getUserName(getApplicationContext());
            binding.tvPostWirter.setText(user);
        }

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK){
                        SetDataRunnable setDataRunnable = new SetDataRunnable();
                        Thread t = new Thread(setDataRunnable);
                        t.start();
                    }
                });

        setListener();
    }

    @Override
    protected void BaseView(@NonNull View view) {
        setContentView(view, ActivityPostBinding.bind(view));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    public void setListener(){
        binding.tvToComment.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
            intent.putExtra(INTENT_KEY_POST_NO, postNo);
            resultLauncher.launch(intent);
        });
        binding.tvToEdit.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PostingActivity.class);
            intent.putExtra(INTENT_KEY_POST_NO, postNo);
            resultLauncher.launch(intent);
        });
        binding.tvToDelete.setOnClickListener(view -> {
            showDeletePopUp();
        });
    }

    private void showDeletePopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.txt_check_delete_post);
            builder.setPositiveButton(R.string.word_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new PostDaoAsyncTask(appDB.postDao(), APP_DB_COMMAND_DELETE, postNo, null).execute();
                    setResult(RESULT_OK);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.word_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });

        builder.show();
    }
}