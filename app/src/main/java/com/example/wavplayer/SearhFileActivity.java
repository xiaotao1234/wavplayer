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
import com.example.wavplayer.dao.FileSearchData;
import com.example.wavplayer.dao.FileSearchMassage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

public class SearhFileActivity extends AppCompatActivity {
    SearchFileListAdapter searchFileListAdapter;
    @BindView(R.id.search_listview)
    RecyclerView recyclerView;
    @BindView(R.id.search_edit)
    EditText editText;
    int searchTextLength;

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
        searchFileListAdapter.flag = true;
        searchTextLength = s.toString().toLowerCase().trim().length();
        searchFileListAdapter.searchResultLength = searchTextLength;
        IAppliation.fileOs.searh(IAppliation.fileOs.getCurrentFloder(), s.toString().toLowerCase().trim());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void showSearchResult(FileSearchMassage fileSearchResult) {
        List<FileSearchData> files = fileSearchResult.getFiles();
        List<String> filename = new ArrayList<>();
        for (FileSearchData file:files){
            filename.add(file.getFile().getName());
        }
        searchFileListAdapter.fileList = files;
        searchFileListAdapter.fileNames = filename;
        searchFileListAdapter.notifyDataSetChanged();
    }
}
