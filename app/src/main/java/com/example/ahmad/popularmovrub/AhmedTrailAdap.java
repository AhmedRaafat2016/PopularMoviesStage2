package com.example.ahmad.popularmovrub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AhmedTrailAdap extends RecyclerView.Adapter<AhmedTrailAdap.TrailerHolder> {

    private ArrayList<AhmedTrailRes> mTrailer;
    private Context mContext;


    public AhmedTrailAdap(ArrayList<AhmedTrailRes> trailers, Context context) {

        this.mTrailer = trailers;
        this.mContext = context;

    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AhmedTrailAdap.TrailerHolder holder, final int position) {

        holder.trailerName.setText(mTrailer.get(position).getName());

        Glide.with(mContext)
                .load(mTrailer.get(position).getYoutubeImg())
                .into(holder.trailerImage);

        holder.trailerImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTrailer.get(position).getYoutubeUrl()));
                mContext.startActivity(webIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailer.size();
    }


    class TrailerHolder extends RecyclerView.ViewHolder {

        TextView trailerName;
        ImageView trailerImage;

        TrailerHolder(View itemView) {
            super(itemView);

            trailerImage = itemView.findViewById(R.id.trailer_sample_image);
            trailerName = itemView.findViewById(R.id.trailer_tv_name);

        }
    }
}
