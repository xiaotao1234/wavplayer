package com.example.wavplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wavplayer.adapter.FileListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
create by xt 2019/7/17
*/
public class FileListActivity extends AppCompatActivity {
    @BindView(R.id.list_files)
    RecyclerView recyclerView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.file_diecetory)
    TextView currentFloderName;
    @BindView(R.id.searh_file)
    ImageView searhFile;
    @BindView(R.id.add_floder)
    ImageView addfloderButton;
    @BindView(R.id.setting)
    ImageView settingButton;
    FileListAdapter fileListAdapter;
    List<String> filesname = new ArrayList<>();
    List<File> files = new ArrayList<>();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(FileListActivity.this, "未获得文件读取权限", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        ButterKnife.bind(this);
        IAppliation.permissionManager.requestPermission(FileListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1, () -> init());
    }

    private void init() {
        IAppliation.fileOs.setCurrentFloder(IAppliation.fileOs.getOsDicteoryPath(this));
        files = IAppliation.fileOs.getFiles();
        filesname = IAppliation.fileOs.getFilesName();
        recyclerView.setSystemUiVisibility(View.INVISIBLE);
        currentFloderName.setText(IAppliation.fileOs.getCurrentFloder().getAbsolutePath());
        fileListAdapter = new FileListAdapter(filesname, this, files);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(fileListAdapter);
        back.setOnClickListener(v -> finish());
    }

    @OnClick(R.id.add_floder)
    public void addfloder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FileListActivity.this);
        builder.setMessage("新建文件夹");
        EditText editText = new EditText(this);
        builder.setView(editText).setPositiveButton("确定", (dialog, which) -> Toast.
                makeText(FileListActivity.this, "确定", Toast.LENGTH_SHORT).show());
        builder.setNegativeButton("取消", (dialog, which) -> {
            Toast.makeText(FileListActivity.this, "确定", Toast.LENGTH_SHORT).show();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @OnClick(R.id.searh_file)
    public void SearhFile() {
        IAppliation.fileOs.pushStack(IAppliation.fileOs.getCurrentFloder());
        startActivity(new Intent(this, SearhFileActivity.class));
    }

    @OnClick(R.id.setting)
    public void setSettingButton(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!IAppliation.fileOs.fileStack.isEmpty()) {
            IAppliation.fileOs.popStack();
        }
    }

    @Override
    public void onBackPressed() {
        if (IAppliation.fileOs.getFileStack().isEmpty() == true) {
            super.onBackPressed();
        } else {
            IAppliation.fileOs.setCurrentFloder(IAppliation.fileOs.popStack());
            fileListAdapter.files = IAppliation.fileOs.getFiles();
            fileListAdapter.filesname = IAppliation.fileOs.getFilesName();
            fileListAdapter.notifyDataSetChanged();
        }
    }
}
