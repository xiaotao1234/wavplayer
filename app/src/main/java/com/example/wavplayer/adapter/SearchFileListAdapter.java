package com.example.wavplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wavplayer.FloderManager.FileOsImpl;
import com.example.wavplayer.IAppliation;
import com.example.wavplayer.MainActivity;
import com.example.wavplayer.R;
import com.example.wavplayer.dao.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFileListAdapter extends RecyclerView.Adapter<SearchFileListAdapter.viewHolder> {
    public List<String> fileNames = new ArrayList<>();
    public List<File> fileList = new ArrayList<>();
    FileOsImpl fileOs = new FileOsImpl();
    Context context;
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_show_search_item,parent,false);
        viewHolder viewHolder = new viewHolder(view);
        this.context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.textView.setText(fileNames.get(position));
        holder.fullDirectory.setText(fileList.get(position).getAbsolutePath());
        holder.itemView.setOnClickListener(v -> {
            Log.d("xiao",fileNames.get(position));
            if (fileList.get(position).isDirectory() == true) {
                fileOs.setCurrentFloder(fileList.get(position));
                fileList = fileOs.getFiles();
                fileNames = IAppliation.fileOs.getFilesName();
                notifyDataSetChanged();
            } else {
                EventBus.getDefault().postSticky(new MessageEvent(fileList.get(position).getAbsolutePath(),position));
                context.startActivity(new Intent(context, MainActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        TextView fullDirectory;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.file_box);
            textView = itemView.findViewById(R.id.file_name);
            fullDirectory = itemView.findViewById(R.id.search_file_directory);
        }
    }
}
