package com.sunnni.blogapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnni.blogapp.R;
import com.sunnni.blogapp.databinding.RvItemPostBinding;
import com.sunnni.blogapp.data.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts = new ArrayList<>();
    private View.OnClickListener onClickListener;

    public PostAdapter(List<Post> posts, View.OnClickListener onClickListener) {
        this.posts.addAll(posts);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.rv_item_post, parent, false);

        PostViewHolder viewHolder = new PostViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.binding.tvDate.setText(post.date);
        holder.binding.tvTitle.setText(post.title);
        holder.binding.tvContents.setText(post.content);

        holder.itemView.setTag(post);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> posts){
        this.posts.clear();
        this.posts.addAll(posts);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        RvItemPostBinding binding;

        public PostViewHolder(View itemView) {
            super(itemView);
            binding = RvItemPostBinding.bind(itemView);
        }
    }
}
