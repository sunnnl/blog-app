package com.sunnni.blogapp.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.sunnni.blogapp.data.AppDatabase;
import com.sunnni.blogapp.data.PreferenceManager;
import com.sunnni.blogapp.R;
import com.sunnni.blogapp.databinding.ActivityScrollingBinding;
import com.sunnni.blogapp.data.model.Post;
import com.sunnni.blogapp.ui.adapter.PostAdapter;

import java.util.List;

import static com.sunnni.blogapp.Code.INTENT_KEY_POST_NO;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityScrollingBinding binding;
    private ActivityResultLauncher<Intent> resultLauncher;
    private AppDatabase appDB;

    private PostAdapter postAdapter;
    private List<Post> postList;

    class SetListRunnable implements Runnable {
        @Override
        public void run() {
            try {
                postList = appDB.postDao().getAllByList();
                postAdapter = new PostAdapter(postList, mOnClickListener);
                binding.contentScrolling.rvPost.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (postList.isEmpty()) {
                            binding.contentScrolling.tvNoPost.setVisibility(View.VISIBLE);
                            binding.contentScrolling.rvPost.setVisibility(View.GONE);
                        } else {
                            binding.contentScrolling.tvNoPost.setVisibility(View.GONE);
                            binding.contentScrolling.rvPost.setVisibility(View.VISIBLE);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    class UpdateListRunnable implements Runnable {
        @Override
        public void run() {
            try {
                postList = appDB.postDao().getAllByList();
                postAdapter.setPosts(postList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postAdapter.notifyDataSetChanged();
                        if (postAdapter.getItemCount() > 0) {
                            binding.contentScrolling.tvNoPost.setVisibility(View.GONE);
                            binding.contentScrolling.rvPost.setVisibility(View.VISIBLE);
                        } else {
                            binding.contentScrolling.tvNoPost.setVisibility(View.VISIBLE);
                            binding.contentScrolling.rvPost.setVisibility(View.GONE);
                        }
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

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        setUi();

        appDB = AppDatabase.getINSTANCE(this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.contentScrolling.rvPost.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(getApplicationContext()).getOrientation());
        binding.contentScrolling.rvPost.addItemDecoration(dividerItemDecoration);

        SetListRunnable setListRunnable = new SetListRunnable();
        Thread t = new Thread(setListRunnable);
        t.start();

        // activity result launcher 정의
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        UpdateListRunnable updateListRunnable = new UpdateListRunnable();
                        Thread thread = new Thread(updateListRunnable);
                        thread.start();
                        setUi();
                    }
                });

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
            resultLauncher.launch(new Intent(getApplicationContext(), PostingActivity.class));
        });

        // 처음 실행 시, 블로그 이름 & 닉네임 설정으로 이동
        if (PreferenceManager.getFirstExec(getApplicationContext())) {
            resultLauncher.launch(new Intent(getApplicationContext(), SettingsActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            resultLauncher.launch(new Intent(getApplicationContext(), SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Post post = (Post) view.getTag();
            if (post == null) {
                return;
            }
            Intent intent = new Intent(getApplicationContext(), PostActivity.class);
            intent.putExtra(INTENT_KEY_POST_NO, post.no);
            resultLauncher.launch(intent);
        }
    };

    private void setUi() {
        String blogName = PreferenceManager.getBlogName(getApplicationContext());
        binding.toolbarLayout.setTitle(blogName);
    }
}