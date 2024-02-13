package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;

public class UsercardWindow extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private LinearProgressIndicator progressIndicator;
    int duration;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    private TextInputEditText messageInputEditText;
    private MaterialButton sendButton, voiceMessageButton, attachmentButton, backButton;

    // Declare this instance variable
    private final ActivityResultLauncher<Intent> filePickerActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        Toast.makeText(this, "File attached: " + uri.getPath(), Toast.LENGTH_SHORT).show();
                        // Display the attached file
                        displayAttachedFile(uri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercardwindow);
        messageInputEditText = findViewById(R.id.messageInputEditText);
        sendButton = findViewById(R.id.sendButton);
        voiceMessageButton = findViewById(R.id.voiceMessageButton);
        attachmentButton = findViewById(R.id.attachmentButton);
        backButton = findViewById(R.id.back_buttonuserwindow);
        progressIndicator=findViewById(R.id.progress_linear_bar);
        progressIndicator.setMax(100);

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
        ImageView attachedImageView = findViewById(R.id.attachedImageView);
        attachedImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.attached_image_width);
        attachedImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.attached_image_height);
        attachedImageView.requestLayout();


        // Set click listener for the attached image view
        attachedImageView.setOnClickListener(v -> openFullScreenImage());
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

    public void recordVoiceMessage() {
        if (!isRecording) {
            if (checkPermission()) {
                try {
                    startRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    private void startRecording() throws IOException {
        String filePath = getExternalCacheDir().getAbsolutePath() + "/voice_message.mp3";
        File file=new File(filePath);
        if(!file.exists()) {
            if (file.createNewFile()) {
                Toast.makeText(this, "file created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "new File created failed", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to start recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            Toast.makeText(this, "Recording stopped...", Toast.LENGTH_SHORT).show();

            // Play the recorded voice
            String filePath = getExternalCacheDir().getAbsolutePath() + "/voice_message.mp3";
            playRecordedVoice(filePath);
        }
    }

    private void playRecordedVoice(String filePath) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setOnPreparedListener(mp -> {
                // Start playback when prepared
                mp.start();
                duration = mp.getDuration();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                // Release the MediaPlayer when playback completes
                mp.release();
            });
            mediaPlayer.prepareAsync(); // Asynchronously prepare the MediaPlayer
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to play recorded voice: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                try {
                    startRecording();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void attachFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Restrict to images only
        filePickerActivityResultLauncher.launch(intent);
    }

    private void displayAttachedFile(Uri fileUri) {
        // Update the UI to display the attached image
        ImageView attachedImageView = findViewById(R.id.attachedImageView);
        attachedImageView.setImageURI(null); // Clear existing image
        attachedImageView.setTag(fileUri); // Set URI as tag
        attachedImageView.setImageURI(fileUri); // Set image using URI
    }

    private void openFullScreenImage() {
        // Get the URI of the attached image
        ImageView attachedImageView = findViewById(R.id.attachedImageView);
        Uri imageUri = (Uri) attachedImageView.getTag();

        if (imageUri != null) {
            // Open the image in a full-screen view
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(imageUri, "image/*");
            startActivity(intent);
        } else {
            Toast.makeText(this, "No image attached", Toast.LENGTH_SHORT).show();
        }
    }
}
