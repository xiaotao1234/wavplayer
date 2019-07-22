package com.example.wavplayer;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.reset_file_directory)
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        view.setSystemUiVisibility(View.INVISIBLE);
    }
    @OnClick(R.id.reset_file_directory)
    public void setView(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("User",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("RootDirectory",null);
        editor.commit();
        IAppliation.fileOs.setCurrentFloder(IAppliation.fileOs.getOsDicteoryPath(this));
        IAppliation.fileOs.getFileStack().clear();
    }
}
