package com.example.myapplication.Support;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class ImageSearchHelper {

    private static final String TAG = "ImageSearchHelper";

    // Method to search for a specific image in MediaStore
    public static Uri searchForImage(ContentResolver contentResolver, String imageName) {
        Uri imageUri = null;
        try {
            // Define the columns to retrieve from the MediaStore
            String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};

            // Define the selection criteria
            String selection = MediaStore.Images.Media.DISPLAY_NAME + "=?";

            // Define the selection arguments
            String[] selectionArgs = {imageName};

            // Perform the query on the MediaStore
            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve the image URI from the cursor
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                long imageId = cursor.getLong(columnIndex);
                imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(imageId));
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error searching for image in MediaStore", e);
        }
        return imageUri;
    }
}

