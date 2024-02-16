package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Support.SubmitHolder;
import com.example.myapplication.model.ItemModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class UsercardWindow extends AppCompatActivity  {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private SeekBar seekBar;
    private MediaRecorder mediaRecorder;

    private boolean isRecording = false;
    private boolean isPlaying=false;

    private TextInputEditText messageInputEditText;
    ArrayList<PlayerChecker> playerCheckers=new ArrayList<>();
    ArrayList<SubmitHolder> submitHolders=new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerReport recyclerReport;


    // Declare this instance variable
    private final ActivityResultLauncher<Intent> filePickerActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        assert uri != null;
                        Toast.makeText(this, "File attached: " + uri.getPath(), Toast.LENGTH_SHORT).show();
                        // Display the attached file
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercardwindow);
        messageInputEditText = findViewById(R.id.messageInputEditText);
        MaterialButton sendButton = findViewById(R.id.sendButton);
        recyclerReport=new RecyclerReport(this,submitHolders,new UsercardWindow());
        recyclerView=findViewById(R.id.chatRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerReport);
        MaterialButton voiceMessageButton = findViewById(R.id.voiceMessageButton);
        MaterialButton attachmentButton = findViewById(R.id.attachmentButton);
        MaterialButton backButton = findViewById(R.id.back_buttonuserwindow);
        seekBar=findViewById(R.id.progress_linear_bar);

        // Set click listener for the back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UsercardWindow.this, newmainuser.class);
            startActivity(intent);
            finish();
        });


        // Set click listeners for the other buttons
        sendButton.setOnClickListener(v -> sendMessage());

        voiceMessageButton.setOnClickListener(v -> recordVoiceMessage());

        attachmentButton.setOnClickListener(v -> attachFile());

        // Set fixed dimensions for the attached image view
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Log.d(TAG, "onProgressChanged: progress "+progress);
                    seekBar.setProgress(progress);
                    isPlaying=false;
                    if(playerCheckers.size()>0)
                        playerCheckers.get(playerCheckers.size()-1).stopPlaying(true);
                  //  playAudio(new File(Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath() + "/voice_message.mp3"),progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch: start touch "+seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch: stop touch "+seekBar.getProgress());
            }
        });
        // Set click listener for the attached image view
    }

    @Override
    protected void onResume() {
        super.onResume();
        ItemModel itemModel=new ItemModel();
        Log.d(TAG, "onResume: resumed activity ");
        if(itemModel.getArrayListMutableLiveData()!=null){
            submitHolders=itemModel.getArrayListMutableLiveData();
            Log.d(TAG, "onCreate: submitHolders "+submitHolders.size());
        }
    }

    public void sendMessage() {
        String message = Objects.requireNonNull(messageInputEditText.getText()).toString().trim();
        if (!message.isEmpty()) {
            // Send text message
            Toast.makeText(this, "Text message sent: " + message, Toast.LENGTH_SHORT).show();
            // Clear the input field after sending
            messageInputEditText.setText("");
        }
    }

    public void recordVoiceMessage() {
        if (!isRecording) {
            if (checkPermission()) {
                Audio audio=new Audio();
                audio.start();
            } else {
                Log.d(TAG, "recordVoiceMessage: requesting for permission ");
                requestPermission();
                Log.d(TAG, "recordVoiceMessage: finished permission ");
            }
        } else {
            stopRecording();
        }
    }



    class Audio extends Thread{
        @Override
        public void run() {
            super.run();
            startRecording();
        }
        private void startRecording(){
            String filePath = Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath() + "/voice_message.mp3";
            File file=new File(filePath);
            if(!file.exists()) {
                try {
                    if (file.createNewFile()) {
                       // Toast.makeText(this, "file created successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "startRecording: file created successfully");
                    } else {
                        //Toast.makeText(this, "new File created failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "startRecording: file create failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(filePath);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
               // Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
                UsercardWindow.this.runOnUiThread(() -> Toast.makeText(UsercardWindow.this, "Recording started ", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
               // Toast.makeText(this, "Failed to start recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            Toast.makeText(this, "Recording stopped...", Toast.LENGTH_SHORT).show();
            String filePath = Objects.requireNonNull(getExternalCacheDir()).getAbsolutePath() + "/voice_message.mp3";
            if(ContextCompat.checkSelfPermission(UsercardWindow.this,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED){
                //playAudio(new File(filePath),0);
                SubmitHolder submitHolder=new SubmitHolder();
                File file=new File(filePath);
                if(file.exists()){
                    submitHolder.setFile(file);
                    submitHolder.setMode(2);
                }
                submitHolders.add(submitHolder);
                recyclerReport.notifyItemInserted(submitHolders.size());
            }else{
                ActivityCompat.requestPermissions(UsercardWindow.this, new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_PERMISSION_CODE);
            }
        }
    }

    private boolean checkPermission() {
        int permissionRecordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return permissionRecordAudio == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }

    public void playAudio(File audioFile,int seek,SeekBar seekBar){
        MediaPlayer mediaPlayer = new MediaPlayer();
        if (audioFile != null && audioFile.exists()) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                mediaPlayer.prepare();
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.seekTo(seek);
                mediaPlayer.setVolume(1.0f,1.0f);
                mediaPlayer.start();
                isPlaying = true;
                PlayerChecker playerChecker= new PlayerChecker(mediaPlayer, seekBar);
                playerChecker.start();
                playerCheckers.add(playerChecker);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to play recording", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No recording available to play", Toast.LENGTH_SHORT).show();
        }
    }
    public void StopAudio(int position){
        playerCheckers.get(position).stopPlaying(true);
    }
    public void seekPosition(int position,int progress){
        playerCheckers.get(position).setProgress(progress);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save data to outState bundle
        ItemModel model=new ItemModel();
        model.setArrayListMutableLiveData(submitHolders);
    }
    static class PlayerChecker extends Thread {
        MediaPlayer mediaPlayer;
        boolean isPlaying=true;
        SeekBar seekBar;
        PlayerChecker(MediaPlayer mediaPlayer,SeekBar seekBar){
            this.mediaPlayer=mediaPlayer;
            this.seekBar=seekBar;
        }
        @Override
        public void run() {
            while (isPlaying){
                if(mediaPlayer.getCurrentPosition()==mediaPlayer.getDuration()){
                    stopPlaying(true);
                    break;
                }
                try {
                    Thread.sleep(50);
                    if(mediaPlayer!=null&&mediaPlayer.isPlaying())
                        seekBar.setProgress(mediaPlayer.getCurrentPosition(),true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "run: playing running");
            }
            mediaPlayer.release();
            Log.d(TAG, "run: mediaPlayer released ");
        }
        public void stopPlaying(boolean isPlaying){
            this.isPlaying=!isPlaying;
        }
        public void setProgress(int progress){
            this.mediaPlayer.seekTo(progress);
            this.seekBar.setProgress(progress,true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: trying to access permission ");
        if (requestCode == REQUEST_PERMISSION_CODE) {
            Log.d(TAG, "onRequestPermissionsResult: working ");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: working too");
                Toast.makeText(this, "permission granted ", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission denied ");
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Log.d(TAG, "onRequestPermissionsResult: permission code not found");
        }
    }

    public void attachFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Restrict to images only
        filePickerActivityResultLauncher.launch(intent);
    }
}
