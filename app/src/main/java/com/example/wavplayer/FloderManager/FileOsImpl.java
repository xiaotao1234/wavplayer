package com.example.wavplayer.FloderManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.example.wavplayer.dao.FileSearchData;
import com.example.wavplayer.dao.FileSearchMassage;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
create by xiao tao 2019/7/19/13:37
*/
public class FileOsImpl {
    List<File> files = new ArrayList<>();//列表内文件
    List<String> filesName = new ArrayList<>();//列表内文件名
    public File OsDicteoryPath;//文件的根目录
    public Stack<File> fileStack = new Stack<>();//文件的遍历栈
    List<File> willDeleteFiles = new ArrayList<>();//将要删除的文件列表
    List<File> selectedFiles = new ArrayList<>();//被勾选的文件列表
    List<File> searchResults = new ArrayList<>();//搜索结果列表
    File currentFloder;//当前所在目录
    File currentFile;//当前正在使用的文件
    private static FileOsImpl fileOsImpl;
    private Thread thread;


    public FileOsImpl() {
    }

    public static FileOsImpl getInstance() {
        if (fileOsImpl == null) {
            fileOsImpl = new FileOsImpl();
        }
        return fileOsImpl;
    }

    public Stack<File> getFileStack() {
        return fileStack;
    }

    public void pushStack(File file) {
        fileStack.push(file);
        Log.d("stack_os_in", fileStack.toString());
    }


    public File popStack() {
        File file = fileStack.pop();
        Log.d("stack_os_out", fileStack.toString());
        return file;
    }


    public File getOsDicteoryPath(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        String filepatc = sharedPreferences.getString("RootDirectory",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
        OsDicteoryPath = new File(filepatc);
        if (!OsDicteoryPath.mkdirs()) {
            Log.d("xiao", "file has exist");
        } else {
            Log.d("xiao", "file has make");
        }
        return OsDicteoryPath;
    }


    public File getCurrentFloder() {
        return currentFloder;
    }

    public void setCurrentFloder(File floder) {
        if (floder != null) {
            currentFloder = floder;
            if (files != null) {
                files.clear();
            }
            if (filesName != null) {
                filesName.clear();
            }
            for (File file : currentFloder.listFiles()) {
                filesName.add(file.getName());
                files.add(file);
            }
        }
    }

    public List<File> getFiles() {
        return files;
    }

    public List<File> getWillDeleteFiles() {
        return willDeleteFiles;
    }

    public void setWillDeleteFiles(File file) {
        if (file != null) {
            willDeleteFiles.add(file);
        }
    }

    public void setOsDicteoryPath(File file) {
        if (file != null && file.exists()) {
            OsDicteoryPath = file;
        }
    }

    public void setSelectedFiles(File file) {
        if (file != null) {
            selectedFiles.add(file);
        }
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }

    public void createFloder(String name) {
        File file = new File(currentFloder.getAbsolutePath() + File.separator + name);
        if (file.exists() == false) {
            if (file.mkdirs()) ;
        }
    }

    public void addFloder(String floderName) {
        File addFile = new File(currentFloder.getAbsolutePath() + File.separator + floderName);
        if (!addFile.exists()) {
            addFile.mkdirs();
        }
    }

    public List<String> getFilesName() {
        return filesName;
    }

    public void searh(File file, String key) {
        Runnable runnable = () -> {
            long time = System.currentTimeMillis();
            FileSearchMassage fileSearchResult = new FileSearchMassage(searchInThisFloder(file, key));
            long timeafter = System.currentTimeMillis()-time;
            Log.d("xiaotaoni", String.valueOf(timeafter));
            EventBus.getDefault().post(fileSearchResult);
        };
        thread = new Thread(runnable);
        thread.start();
    }

    public List<FileSearchData> searchInThisFloder(File file, String key) {//在文件夹内递归搜索相应文件
        List<FileSearchData> thisPartResult = new ArrayList<>();
        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                if (file1.getName().toLowerCase().contains(key)&&file1.isDirectory()){
                    thisPartResult.add(new FileSearchData(file1,file1.getName().toLowerCase().indexOf(key)));
                }
                thisPartResult.addAll(searchInThisFloder(file1, key));
            }
        } else {
            if (file.getName().toLowerCase().contains(key)){
                thisPartResult.add(new FileSearchData(file,file.getName().toLowerCase().indexOf(key)));
            }
        }
        Log.d("xiaoxiao", String.valueOf(thisPartResult.size()));
        return thisPartResult;
    }
}
