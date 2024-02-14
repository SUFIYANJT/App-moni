package com.example.myapplication.Audio;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.myapplication.model.ItemModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.IOException;
import java.nio.ByteBuffer;

public class AudioThread extends Thread{
    String filePath;
    MediaExtractor extractor;
    long presentationTime = 0;
    MediaCodec codec;
    AudioTrack audioTrack;
    float playbackSpeed;
    LinearProgressIndicator progressIndicator;
    public AudioThread(String filePath,MediaExtractor extractor,MediaCodec codec,AudioTrack audioTrack,float playbackSpeed,LinearProgressIndicator progressIndicator){
        this.filePath=filePath;
        this.extractor=extractor;
        this.codec=codec;
        this.audioTrack=audioTrack;
        this.playbackSpeed=playbackSpeed;
        this.progressIndicator=progressIndicator;
    }
    @Override
    public void run() {
        super.run();
        try {
            playAudioFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void playAudioFile(String audioFilePath) throws IOException {
        ItemModel itemModel=new ItemModel();
        extractor = new MediaExtractor();
        extractor.setDataSource(audioFilePath);
        MediaFormat format = null;
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            assert mime != null;
            if (mime.startsWith("audio/")) {
                extractor.selectTrack(i);
                break;
            }
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(audioFilePath);
        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        assert durationStr != null;
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
        int bufferSize = AudioTrack.getMinBufferSize((int) (format.getInteger(MediaFormat.KEY_SAMPLE_RATE) * playbackSpeed), channelConfig, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, (int) (format.getInteger(MediaFormat.KEY_SAMPLE_RATE) * playbackSpeed), channelConfig, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        audioTrack.setPlaybackRate((int) (format.getInteger(MediaFormat.KEY_SAMPLE_RATE) * playbackSpeed)); // Adjust the playback speed
        audioTrack.play();
        long lastPresentationTime = -1;
        while (duration!=info.presentationTimeUs) {
            presentationTime=extractor.getSampleTime();
            //progressIndicator.setProgress((int)presentationTime);
            Log.d(TAG, "playAudioFile: info "+presentationTime+" "+duration);
            if(presentationTime==-1){
                break;
            }
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
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    itemModel.updateLiveDataExample(presentationTime);
                    Log.d(TAG, "run: sufiyan oru kundan ");
                }
            });
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
        presentationTime=0;
        releaseResources();
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
}
