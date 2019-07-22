package com.example.wavplayer.dao;

import java.util.ArrayList;
import java.util.List;

public class FileSearchMassage {

    public FileSearchMassage(List<FileSearchData> files) {
        this.files = files;
    }

    public void setFiles(List<FileSearchData> files) {
        this.files = files;
    }

    public List<FileSearchData> getFiles() {
        return files;
    }

    private List<FileSearchData> files = new ArrayList<>();

}
