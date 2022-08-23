package com.example.amine.learngesture;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import butterknife.ButterKnife;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder>
{
    private File[] videoFiles;
    Context context;
    private boolean[] checked;
    public UploadListAdapter(File[] videos, Context context){
        this.videoFiles = videos;
        this.context = context;
        checked = new boolean[videos.length];
    }




    @NonNull
    @Override
    public UploadListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.row_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadListAdapter.ViewHolder viewHolder, int i) {

        if(videoFiles[i]!=null) {
            viewHolder.position = i;
            Uri uri = Uri.parse(videoFiles[i].getPath());
            viewHolder.vv_video.setVideoURI(uri);
            if(!viewHolder.vv_video.isPlaying())
                viewHolder.vv_video.start();

            String filename= videoFiles[i].getPath().substring(videoFiles[i].getPath().lastIndexOf("/")+1);
            viewHolder.tv_title.setText("");
            Log.e("msg",viewHolder.ischecked+"");
            if(checked[i]) {
                checked[i] = true;
                viewHolder.cb_check.setChecked(true);

            }
            else {
                checked[i] = false;
                viewHolder.cb_check.setChecked(false);

            }

        } else {

        }


    }

    @Override
    public int getItemCount() {
        return videoFiles.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        CheckBox cb_check;
        boolean ischecked;
        int position;
        public VideoView vv_video;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vv_video = (VideoView) itemView.findViewById(R.id.vv_video);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            cb_check = (CheckBox) itemView.findViewById(R.id.cb_check);
            cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ischecked= b;

                    checked[position] = ischecked;

                }
            });
            vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                   }
            });
            vv_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vv_video.start();
                }
            });
        }
    }

    public boolean[] getChecked() {
        return checked;
    }

    public File[] getVideoFiles() {
        return videoFiles;
    }

}
