package com.example.projec7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class PostDetailFragment extends BottomSheetDialogFragment {

    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private EditText commentEditText;
    private Button postCommentButton;

    private List<Comment> commentList;
    private int currentPostId = -1;
    private DBHelper dbHelper;

    private ImageButton DeleteButton;

    public PostDetailFragment() {
        // Required empty public constructor
    }

    public static PostDetailFragment newInstance(int postId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putInt("post_id", postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPostId = getArguments().getInt("post_id", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        commentRecyclerView = view.findViewById(R.id.commentRecyclerView);
        commentEditText = view.findViewById(R.id.CommentText);
        postCommentButton = view.findViewById(R.id.submitButton);
        currentPostId = getCurrentPostId();
        commentList = getCommentsForPost(currentPostId);
        dbHelper = new DBHelper(getContext());

        // Get comments for the current post
        commentList = dbHelper.getCommentsForPost(currentPostId);
        commentAdapter = new CommentAdapter(commentList);

        // Set up RecyclerView
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentRecyclerView.setAdapter(commentAdapter);

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCommentText = commentEditText.getText().toString();

                dbHelper.insertComment(currentPostId, newCommentText);

                // Refresh the comment list
                commentList.clear();
                commentList.addAll(dbHelper.getCommentsForPost(currentPostId));

                // Notify the adapter that the data set has changed
                commentAdapter.notifyDataSetChanged();

                // Clear the EditText after posting the comment
                commentEditText.setText("");
            }
        });


        return view;
    }



    private void deleteComment(int position) {
        // 삭제 로직을 여기에 추가
        // position은 삭제할 아이템의 위치입니다.
        // commentList에서 해당 위치의 아이템을 삭제하고, notifyDataSetChanged()를 호출하여 UI 갱신 등을 수행합니다.
        Comment comment = commentList.get(position);
        dbHelper.deleteComment(comment.getId());  // 이 메서드를 구현해야 합니다.
        commentList.remove(position);
        commentAdapter.notifyItemRemoved(position);
    }

    private List<Comment> getCommentsForPost(int postId) {
        // 여기에서 postId에 해당하는 댓글 데이터를 가져오는 로직을 추가하십시오.
        // 이 예시에서는 더미 데이터를 사용합니다.
        List<Comment> comments = new ArrayList<>();

        // 가져온 댓글 목록을 RecyclerView에 연결하거나 다른 방법으로 표시
        // 예시로 몇 가지 더미 데이터를 추가하겠습니다.
        for (int i = 1; i <= 5; i++) {
            Comment comment = new Comment(postId, "Comment " + i);
            comments.add(comment);
        }

        return comments;
    }
    private int getCurrentPostId() {
        // 여기에서 현재 표시 중인 게시물의 ID를 가져오는 로직을 추가하세요.
        // 예를 들어, getArguments()를 사용하여 인자로 전달된 postId를 가져올 수 있습니다.
        if (getArguments() != null) {
            return getArguments().getInt("post_id", -1);
        } else {
            return -1; // 기본값
        }
    }
}