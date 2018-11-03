package com.example.ahmad.popularmovrub;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AhmedRevAdap extends RecyclerView.Adapter<AhmedRevAdap.ReviewHolder> {


    private ArrayList<AhmedRevRes> mReview;


    public AhmedRevAdap(ArrayList<AhmedRevRes> reviews) {

        this.mReview = reviews;

    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AhmedRevAdap.ReviewHolder holder, final int position) {

        holder.author.setText(mReview.get(position).getAuthor());
        holder.content.setText(mReview.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mReview.size();
    }

    static class ReviewHolder extends RecyclerView.ViewHolder {

        TextView author, content;

        ReviewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.review_author);
            content = itemView.findViewById(R.id.review_text);
        }
    }

}
