package com.example.wavplayer.tools;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private List<File> files;
    public File file;
    private static FileManager fileManager;

    public static FileManager getInstane(){
        if(fileManager==null){
            fileManager = new FileManager();
        }
        return fileManager;
    }

    private FileManager() {
        files = new ArrayList<>();
    }

    public File setAlbumStorageDir(){//建立保存主目录
        //参数2是文件名称
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        if(!file.mkdirs()){
            Log.d("xiao","file has exist");
        }else {
            Log.d("xiao","file has make");
        }
        return file;
    }

    public String getAlbunStorageDir(){
        return file.getAbsolutePath();
    }

    //获得当前指定文件的子目录下的所有文件
    public List<File> openFloder(File file){
        files.clear();
        if(file==null){
            return null;
        }
        if(!file.isDirectory()||!file.exists()){
            return null;
        }
        if(file.listFiles()==null){
            return null;
        }
        for(File file1:file.listFiles()){
            files.add(file1);
        }
        return files;
    }

    //获得指定文件
    public File getFile(String filename){
        for (File file:files){
            if(file.getName().equals(filename)){
                return file;
            }
        }
        return null;
    }
}

