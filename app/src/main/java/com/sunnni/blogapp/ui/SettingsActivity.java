package com.sunnni.blogapp.ui;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sunnni.blogapp.data.PreferenceManager;
import com.sunnni.blogapp.ui.base.BaseActivity;
import com.sunnni.blogapp.R;
import com.sunnni.blogapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseView(getView(R.layout.activity_settings));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setListener();
        setUi();
    }

    @Override
    protected void BaseView(@NonNull View view) {
        setContentView(view, ActivitySettingsBinding.bind(view));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setListener(){
        binding.btnDone.setOnClickListener(view -> {
            String blogName = binding.edtBlogTitle.getText().toString();
            String userName = binding.edtUserName.getText().toString();
            if (blogName.equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.hint_input_blog_name), Toast.LENGTH_SHORT).show();
            } else if (userName.equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.hint_input_user_name), Toast.LENGTH_SHORT).show();
            } else {
                PreferenceManager.setBlogName(getApplicationContext(), blogName);
                PreferenceManager.setUserName(getApplicationContext(), userName);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void setUi(){
        if (PreferenceManager.getFirstExec(getApplicationContext())) {
            PreferenceManager.setFirstExecFalse(getApplicationContext());
            binding.tvWelcome.setVisibility(View.VISIBLE);
        }

        String curBlogName = PreferenceManager.getBlogName(getApplicationContext());
        String curUserName = PreferenceManager.getUserName(getApplicationContext());
        binding.edtBlogTitle.setText(curBlogName);
        binding.edtUserName.setText(curUserName);
    }
}