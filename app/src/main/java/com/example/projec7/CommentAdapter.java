package com.example.projec7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList;
    private OnDeleteClickListener onDeleteClickListener;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.commentText.setText(comment.getCommentText());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 삭제 버튼 클릭 시 onDeleteClickListener를 통해 해당 위치(position)을 전달
                int adapterPosition = holder.getAdapterPosition();
                if (onDeleteClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClickListener.onDeleteClick(adapterPosition);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        ImageButton deleteButton;

        // ViewHolder 생성자에서 onDeleteClickListener 초기화는 삭제함.

        // ViewHolder 생성자에서 onDeleteClickListener를 받도록 추가
        ViewHolder(View itemView) {
            super(itemView);

            commentText = itemView.findViewById(R.id.commentText);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            // DeleteButton을 클릭했을 때 처리할 로직을 여기에 추가합니다.
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}