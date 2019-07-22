package com.example.wavplayer.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearchMassage {

    public FileSearchMassage(List<File> files) {
        this.files = files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<File> getFiles() {
        return files;
    }

    private List<File> files = new ArrayList<>();

}
