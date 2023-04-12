package com.example.aiya_test_3.incidents;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;

public class FireBaseUtils {
    final static long FIVE_MEGABYTE = 1024 * 1024 * 5;
    public static void downloadToImageView(final Context context, StorageReference storageRef, final ImageView imageView) {

        storageRef.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for image reference is returns, use this as needed
                //Toast.makeText(context, "Download successful", Toast.LENGTH_SHORT).show();
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
