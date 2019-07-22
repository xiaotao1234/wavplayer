package com.example.wavplayer.dao;

import java.io.File;

public class FileSearchData {
    public void setFile(File file) {
        this.file = file;
    }

    public void setStart(int start) {
        this.start = start;
    }

    File file;

    public FileSearchData(File file, int start) {
        this.file = file;
        this.start = start;
    }

    public File getFile() {
        return file;
    }

    public int getStart() {
        return start;
    }

    int start;
}
