package com.example.myapplication.Support;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MediaStoreHelper {

    private static final String TAG = "MediaStoreHelper";

    // Method to save an image to the MediaStore and return its URI
    public static Uri saveImageToMediaStore(Context context, File file) {
        Uri imageUri = null;
        try {
            // Create a ContentValues object to hold the image details
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            // Insert the image details into the MediaStore
            ContentResolver resolver = context.getContentResolver();
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Write the image data to the MediaStore OutputStream
            try (OutputStream os = resolver.openOutputStream(imageUri)) {
                os.write(fileToByteArray(file));
                os.flush();
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to save image to MediaStore", e);
        }
        return imageUri;
    }

    // Method to convert a File to a byte array
    private static byte[] fileToByteArray(File file) throws IOException {
        // Read the file data into a byte array
        byte[] data;
        try (FileInputStream fis = new FileInputStream(file)) {
            data = new byte[(int) file.length()];
            fis.read(data);
        }
        return data;
    }
}
