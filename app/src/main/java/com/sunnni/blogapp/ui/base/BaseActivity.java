package com.sunnni.blogapp.ui.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<T> extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    /************** ViewBinding Default Method & Variable ***************/
    public T binding = null;
    public Activity mActivity;
    public Context mContext;
    public View root;

    protected abstract void BaseView(@NonNull View view);

    protected void setContentView(@NonNull View view, @NonNull T binding) {
        super.setContentView(view);
        this.binding = binding;
        mActivity = this;
        mContext = this;
        this.root = view;
    }
    protected View getView(@LayoutRes int layoutId) {
        return (this.getLayoutInflater().inflate(layoutId,null));
    }
    protected View getRoot() {
        return this.root;
    }

    protected void setCustomTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //커스텀 사용
            getSupportActionBar().setDisplayShowTitleEnabled(false); //title 제거
        }
    }
}