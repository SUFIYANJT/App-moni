package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class UsercardWindow extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_CODE_PICK_FILE = 101;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    private TextInputEditText messageInputEditText;
    private MaterialButton sendButton, voiceMessageButton, attachmentButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercardwindow);
        messageInputEditText = findViewById(R.id.messageInputEditText);
        sendButton = findViewById(R.id.sendButton);
        voiceMessageButton = findViewById(R.id.voiceMessageButton);
        attachmentButton = findViewById(R.id.attachmentButton);
        backButton = findViewById(R.id.back_buttonuserwindow);

        // Set click listener for the back button
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UsercardWindow.this, newmainuser.class);
            startActivity(intent);
            finish();
        });

        // Set click listeners for the other buttons
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        voiceMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVoiceMessage(v);
            }
        });

        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFile();
            }
        });
    }

    public void sendMessage() {
        String message = messageInputEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            // Send text message
            Toast.makeText(this, "Text message sent: " + message, Toast.LENGTH_SHORT).show();
            // Clear the input field after sending
            messageInputEditText.setText("");
        }
    }

    public void recordVoiceMessage(View view) {
        if (!isRecording) {
            if (checkPermission()) {
                startRecording();
            } else {
                requestPermission();
            }
        } else {
            stopRecording();
        }
    }

    private boolean checkPermission() {
        int permissionRecordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return permissionRecordAudio == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }

    private void startRecording() {
        String filePath = getExternalCacheDir().getAbsolutePath() + "/voice_message.mp3";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(filePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to start recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;
        Toast.makeText(this, "Recording stopped...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Declare this instance variable
    private final ActivityResultLauncher<Intent> filePickerActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        Toast.makeText(this, "File attached: " + uri.getPath(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    public void attachFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        filePickerActivityResultLauncher.launch(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                // The result data contains a URI for the document or directory that the user selected.
                Uri uri = data.getData();
                // Perform operations on the document using its URI.
                Toast.makeText(this, "File attached: " + uri.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
