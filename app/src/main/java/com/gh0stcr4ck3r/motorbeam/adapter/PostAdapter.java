package com.gh0stcr4ck3r.motorbeam.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gh0stcr4ck3r.motorbeam.PostDetailsActivity;
import com.gh0stcr4ck3r.motorbeam.R;
import com.gh0stcr4ck3r.motorbeam.model.PostModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static String TAG ="PostAdapter";

    private List<PostModel> PostList;
    private Context context;

    public PostAdapter(List<PostModel> PostList, Context context) {
        this.PostList = PostList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_layout,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        final PostModel PostListSingle = PostList.get(position);
        String postTitle=PostListSingle.getTitle();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.sTitle.setText(Html.fromHtml(postTitle,Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.sTitle.setText(Html.fromHtml(postTitle));
        }

        Picasso.get().load(PostListSingle.getThumb()).into(holder.sThumbnail);
        final String id=PostListSingle.getId();


        //final String Playlist_ID=PostListActivity.PLAYLIST_ID;
        holder.PostItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data",id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return PostList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView sTitle;
        public ImageView sThumbnail;
        public LinearLayout PostItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            sTitle=(itemView).findViewById(R.id.postTitle);
            sThumbnail=(itemView).findViewById(R.id.postThumb);
            PostItemLayout =(itemView).findViewById(R.id.PostLayout);
        }

    }



}