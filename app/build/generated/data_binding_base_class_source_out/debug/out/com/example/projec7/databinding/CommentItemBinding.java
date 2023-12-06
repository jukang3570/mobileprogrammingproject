// Generated by view binder compiler. Do not edit!
package com.example.projec7.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.projec7.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CommentItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView commentText;

  @NonNull
  public final ImageButton deleteButton;

  @NonNull
  public final TextView name;

  private CommentItemBinding(@NonNull LinearLayout rootView, @NonNull TextView commentText,
      @NonNull ImageButton deleteButton, @NonNull TextView name) {
    this.rootView = rootView;
    this.commentText = commentText;
    this.deleteButton = deleteButton;
    this.name = name;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CommentItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CommentItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.comment_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CommentItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.commentText;
      TextView commentText = ViewBindings.findChildViewById(rootView, id);
      if (commentText == null) {
        break missingId;
      }

      id = R.id.deleteButton;
      ImageButton deleteButton = ViewBindings.findChildViewById(rootView, id);
      if (deleteButton == null) {
        break missingId;
      }

      id = R.id.name;
      TextView name = ViewBindings.findChildViewById(rootView, id);
      if (name == null) {
        break missingId;
      }

      return new CommentItemBinding((LinearLayout) rootView, commentText, deleteButton, name);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
