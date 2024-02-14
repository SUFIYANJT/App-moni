package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
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
import java.nio.ByteBuffer;

public class UsercardWindow extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private LinearProgressIndicator progressIndicator;
    private MediaExtractor extractor;
    private MediaCodec codec;
    private AudioTrack audioTrack;
    private float playbackSpeed = 0.5f;
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
        progressIndicator.setMax(100);int duration;

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
                }catch (IOException e){
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
// ... other initialization here ...

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
            try {
                playAudioFile(filePath);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    private void playRecordedVoice(String filePath) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
// ... other initialization here ...
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setDataSource(filePath);

            duration = mediaPlayer.getDuration();
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                // Release the MediaPlayer when playback completes
                mediaPlayer.release();
            });
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
    private void playAudioFile(String audioFilePath) throws IOException {
        extractor = new MediaExtractor();
        extractor.setDataSource(audioFilePath);
        MediaFormat format = null;
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                extractor.selectTrack(i);
                break;
            }
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(audioFilePath);
        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        long duration=Long.parseLong(durationStr);
        duration=duration*1000;
        Log.d(TAG, "playAudioFile: "+duration);
        codec = MediaCodec.createDecoderByType(format.getString(MediaFormat.KEY_MIME));
        codec.configure(format, null, null, 0);
        codec.start();
        ByteBuffer[] inputBuffers = codec.getInputBuffers();
        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
        int bufferSize = AudioTrack.getMinBufferSize((int) (format.getInteger(MediaFormat.KEY_SAMPLE_RATE) * playbackSpeed),
                channelConfig, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                (int) (format.getInteger(MediaFormat.KEY_SAMPLE_RATE) * playbackSpeed), channelConfig,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        audioTrack.setPlaybackRate((int) (format.getInteger(MediaFormat.KEY_SAMPLE_RATE) * playbackSpeed)); // Adjust the playback speed
        audioTrack.play();
        long lastPresentationTime = -1;
        int count=0;
        long presentationTime = 0;
        while (duration!=info.presentationTimeUs) {

            presentationTime=extractor.getSampleTime();
            Log.d(TAG, "playAudioFile: info "+presentationTime+" "+duration);
            int inputIndex = codec.dequeueInputBuffer(-1);

            if (inputIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputIndex];
                int sampleSize = extractor.readSampleData(inputBuffer, 0);

                if (sampleSize < 0) {
                    codec.queueInputBuffer(inputIndex, 0, 0, 0, 0);
                } else {
                    codec.queueInputBuffer(inputIndex, 0, sampleSize, extractor.getSampleTime(), 0);
                    extractor.advance();
                }
            }

            if (presentationTime > lastPresentationTime) {
                lastPresentationTime = presentationTime;
            }
            int outputIndex = codec.dequeueOutputBuffer(info, 0);

            if (outputIndex >= 0) {
                ByteBuffer outputBuffer = outputBuffers[outputIndex];
                byte[] chunk = new byte[info.size];
                outputBuffer.get(chunk);
                outputBuffer.clear();
                audioTrack.write(chunk, 0, chunk.length);
                codec.releaseOutputBuffer(outputIndex, false);
            }
            switch (outputIndex) {
                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                    Log.d("DecodeActivity", "INFO_OUTPUT_BUFFERS_CHANGED");
                    outputBuffers = codec.getOutputBuffers();
                    break;
                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                    MediaFormat mediaFormat = codec.getOutputFormat();
                    Log.d("DecodeActivity", "New format " + mediaFormat);
                    //   audioTrack.setPlaybackRate(mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE));
                    break;
                case MediaCodec.INFO_TRY_AGAIN_LATER:
                    Log.d("DecodeActivity", "dequeueOutputBuffer timed out!");
                    break;

                default:
                    Log.v("", "Inside While Loop Break Point 3");
                    break;
            }
            Log.d(TAG, "playAudioFile: "+info.flags+" "+MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                Log.d("DecodeActivity", "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                break;
            }
        }
    }

    private void releaseResources() {
        if (codec != null) {
            codec.stop();
            codec.release();
            Log.d(TAG, "releaseResources: Codec released");
        }
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            Log.d(TAG, "releaseResources: audioTracker released");
        }
        if (extractor != null) {
            extractor.release();
            Log.d(TAG, "releaseResources: extractor released");
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
