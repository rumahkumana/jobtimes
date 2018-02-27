package com.jobtimes.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rumahkumana on 27/02/18.
 */

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.MyViewHolder> {
    private List<JobPost> mJobPostList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, message, title;

        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.mUsername);
            message = (TextView) view.findViewById(R.id.mMessage);
            title = (TextView) view.findViewById(R.id.mTitle);
  //          message = (TextView) view.findViewById(R.id.);
        }
    }

    public JobPostAdapter(List<JobPost> jobPostList) {
        this.mJobPostList = jobPostList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.jobpost_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JobPost jobPost = mJobPostList.get(position);
        holder.message.setText(jobPost.getMessage());
        holder.username.setText(jobPost.getUsername());
        holder.title.setText(jobPost.getTitle());
    }

    @Override
    public int getItemCount() {
        return mJobPostList.size();
    }
}
