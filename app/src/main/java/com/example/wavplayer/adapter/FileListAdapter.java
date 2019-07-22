package com.example.wavplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wavplayer.FloderManager.FileOsImpl;
import com.example.wavplayer.IAppliation;
import com.example.wavplayer.MainActivity;
import com.example.wavplayer.R;
import com.example.wavplayer.dao.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.Stack;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.viewholder> {
    public File currentFile = IAppliation.fileOs.getCurrentFloder();
    public List<String> filesname;
    Context context;
    public List<File> files;
    SharedPreferences sharedPreferences;

    public FileListAdapter(List<String> filesname, Context context, List<File> files) {
        this.filesname = filesname;
        this.files = files;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_show_item, parent, false);
        viewholder viewholder1 = new viewholder(view);
        return viewholder1;
    }

    @Override
    public int getItemCount() {
        return filesname.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.file_box);
            textView = itemView.findViewById(R.id.file_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
        holder.textView.setText(filesname.get(position));
        if (files.get(position).isDirectory() == true) {
            holder.imageView.setImageResource(R.drawable.floder_icon);
        }
        holder.itemView.setOnClickListener(v -> {
            currentFile = IAppliation.fileOs.getCurrentFloder();
            if (files.get(position).isDirectory() == true) {
                IAppliation.fileOs.pushStack(currentFile);
                currentFile = files.get(position);
                IAppliation.fileOs.setCurrentFloder(currentFile);
                files = IAppliation.fileOs.getFiles();
                filesname = IAppliation.fileOs.getFilesName();
                notifyDataSetChanged();
            } else {
                EventBus.getDefault().postSticky(new MessageEvent(files.get(position).getAbsolutePath(),position));
                context.startActivity(new Intent(context, MainActivity.class));
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if(files.get(position).isDirectory()){
                sharedPreferences = context.getSharedPreferences("User",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("RootDirectory",files.get(position).getAbsolutePath());
                editor.commit();
                IAppliation.fileOs.setOsDicteoryPath(files.get(position));
                IAppliation.fileOs.setCurrentFloder(files.get(position));
                files = IAppliation.fileOs.getFiles();
                filesname = IAppliation.fileOs.getFilesName();
                notifyDataSetChanged();
            }else {
                Toast.makeText(context,"不能使用文件作为主目录",Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

    }
}
