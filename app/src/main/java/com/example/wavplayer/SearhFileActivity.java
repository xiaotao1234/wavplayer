package com.example.wavplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import com.example.wavplayer.adapter.SearchFileListAdapter;
import com.example.wavplayer.dao.FileSearchMassage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearhFileActivity extends AppCompatActivity {
    SearchFileListAdapter searchFileListAdapter;
    @BindView(R.id.search_listview)
    RecyclerView recyclerView;
    @BindView(R.id.search_edit)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searh_file);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        searchFileListAdapter = new SearchFileListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchFileListAdapter);
        recyclerView.setSystemUiVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnTextChanged(value = R.id.search_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        IAppliation.fileOs.searh(IAppliation.fileOs.getCurrentFloder(), s.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void showSearchResult(FileSearchMassage fileSearchResult) {
        List<File> files = fileSearchResult.getFiles();
        List<String> filename = new ArrayList<>();
        for (File file:files){
            filename.add(file.getName());
        }
        searchFileListAdapter.fileList = files;
        searchFileListAdapter.fileNames = filename;
        searchFileListAdapter.notifyDataSetChanged();
    }
}
