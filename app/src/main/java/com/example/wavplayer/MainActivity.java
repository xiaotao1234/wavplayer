package com.example.wavplayer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wavplayer.dao.MessageEvent;
import com.example.wavplayer.renderer.BarGraphRenderer;
import com.example.wavplayer.weight.CustomProgress;
import com.example.wavplayer.weight.VisualizerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @BindViews({R.id.main_back, R.id.previous_button, R.id.play_control, R.id.next_button})
    List<ImageView> imageViews;
    @BindViews({R.id.video_name, R.id.play_plan, R.id.music_length})
    List<TextView> textViews;
    @BindView(R.id.visualizerView)
    VisualizerView visualizerView;
    @BindView(R.id.video_progress)
    CustomProgress customProgress;
    volatile boolean playOrNotTab = true;
    MediaPlayer mediaPlayer;
    String fileName;
    int filePosition;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("xiaoxiao","oncreate invoke");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ui();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xiaoxiao","onstop invoke");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("xiaoxiao","onpause invoke");
        releaseResource();
        mediaPlayer.pause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        Log.d("xiaoxiao","onstart invoke");
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        Log.d("xiaoxiao","ondestory invoke");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d("xiaoxiao","onresume invoke");
        super.onResume();
        init(fileName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMoonEvent(MessageEvent messageEvent) {
        fileName = messageEvent.getMessageString();
        filePosition = messageEvent.getFilePosition();
    }

    public void releaseResource(){
        if(thread!=null){
            thread.interrupt();
        }
        if(visualizerView!=null){
            visualizerView.release();
        }
    }

    @OnClick(R.id.previous_button)
    public void previousMusic() {
        releaseResource();
        List<File> currentFiles = IAppliation.fileOs.getFiles();
        filePosition = filePosition - 1 < 0 ? currentFiles.size() - 1 : filePosition - 1;
        IAppliation.fileOs.setCurrentFile(currentFiles.get(filePosition));
        init(IAppliation.fileOs.getCurrentFile().getAbsolutePath());
    }

    @OnClick(R.id.next_button)
    public void nextMusic() {
        thread.interrupt();
        visualizerView.release();
        List<File> currentFiles = IAppliation.fileOs.getFiles();
        filePosition = filePosition + 1 >= currentFiles.size() ? 0 : filePosition + 1;
        IAppliation.fileOs.setCurrentFile(currentFiles.get(filePosition));
        init(IAppliation.fileOs.getCurrentFile().getAbsolutePath());
    }

    @OnClick(R.id.play_control)
    public void playControl() {
        if (playOrNotTab == false) {
            playclick();
            imageViews.get(2).setImageResource(R.drawable.play_icon);
            playOrNotTab = true;
        } else {
            mediaPlayer.pause();
            imageViews.get(2).setImageResource(R.drawable.stop_icon);
            playOrNotTab = false;
        }
    }

    private void ui() {
        textViews.get(0).setSystemUiVisibility(View.INVISIBLE);
        customProgress.setProgress(0);
        customProgress.setProgressListener(progress -> mediaPlayer.seekTo((int) (progress * mediaPlayer.getDuration() / 100)));
    }

    private void playclick() {
        startplayer();
        textViews.get(0).setText(IAppliation.fileOs.getCurrentFile().getAbsolutePath());
        thread = new Thread(runnable);
        thread.start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(100);
                    Message message = Message.obtain();
                    message.what = 1;
                    if (mediaPlayer != null) {
                        message.obj = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(message);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    textViews.get(1).setText(String.valueOf(IAppliation.timeTools.timetrans((Integer) msg.obj / 1000)));
                    if (mediaPlayer.getDuration() != 0) {
                        customProgress.setProgress((Integer) msg.obj / (mediaPlayer.getDuration() / 100));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this, "没有获得相关权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this, "没有获得相关权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this, "没有获得相关权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    visualizerView.link(mediaPlayer, imageViews.get(2));
                    // Start with just line renderer
                    addBarGraphRenderers();
                } else {
                    Toast.makeText(MainActivity.this, "没有获得相关权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void init(String fileName) {
        mediaPlayerInit(fileName);
        permissionCheck();
        playclick();
    }


    private void permissionCheck() {
        IAppliation.permissionManager.requestPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO, 3, () -> {
            visualizerView.link(mediaPlayer, imageViews.get(2));
            addBarGraphRenderers();
        });
    }

    private void mediaPlayerInit(String fileName) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            IAppliation.fileOs.setCurrentFile(new File(fileName));
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "音频文件打开失败，请检查文件格式是否正确", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void addBarGraphRenderers() {

        //底部柱状条
        Paint paint = new Paint();
        paint.setStrokeWidth(12f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 56, 138, 252));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(4, paint, false);
        visualizerView.addRenderer(barGraphRendererBottom);

        //顶部柱状条
//        Paint paint2 = new Paint();
//        paint2.setStrokeWidth(30f);
//        paint2.setAntiAlias(true);
//        paint2.setColor(Color.argb(200, 181, 111, 233));
//        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(9, paint2, true);
//        visualizerView.addRenderer(barGraphRendererTop);
    }

    private void startplayer() {
        if (mediaPlayer != null) {
            Log.d("xiao", "start");
        }
        mediaPlayer.start();
        textViews.get(2).setText(IAppliation.timeTools.timetrans(mediaPlayer.getDuration() / 1000));
    }
}
