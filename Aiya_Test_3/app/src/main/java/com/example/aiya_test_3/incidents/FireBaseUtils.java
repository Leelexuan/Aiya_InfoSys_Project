package com.example.aiya_test_3.incidents;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class FireBaseUtils {
    final static long FIVE_MEGABYTE = 1024 * 1024 * 5;
    public static Task<byte[]> downloadToImageView(final Context context, StorageReference storageRef, final ImageView imageView) {

        return storageRef.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for image reference is returns, use this as needed
                Toast.makeText(context, "Download successful", Toast.LENGTH_SHORT).show();
                ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                Drawable drw = Drawable.createFromStream(is, "articleImage");
                imageView.setImageDrawable(drw);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("ERROR", exception.toString());
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
