package com.sunnni.blogapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunnni.blogapp.R;
import com.sunnni.blogapp.data.model.Comment;
import com.sunnni.blogapp.databinding.RvItemCommentBinding;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments = new ArrayList<>();

    public CommentAdapter(List<Comment> comments) {
        this.comments.addAll(comments);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.rv_item_comment, parent, false);

        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.binding.tvCommentWriter.setText(comment.writer);
        holder.binding.tvCommentContent.setText(comment.content);
        holder.binding.tvCommentDate.setText(comment.date);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        RvItemCommentBinding binding;
        public CommentViewHolder(View itemView){
            super(itemView);
            binding = RvItemCommentBinding.bind(itemView);
        }
    }
}
