package com.pascalso.inquire;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pso on 12/28/15.
 */
public class Camera extends Activity {
    private static Bitmap image;
    private int REQUEST_IMAGE_CAPTURE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public String mCurrentPhotoPath;
    private Uri fileUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    Intent startSelectedImageFragment = new Intent(Camera.this, SelectedImage.class);
                    startSelectedImageFragment.putExtra("calling-activity", ActivityConstants.STUDENT_CAMERA);
                    startActivity(startSelectedImageFragment);
                    finish();
                }
                catch(Exception e){
                    finish();
                }
                /**
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                image = photo;
                Intent startSelectedImageFragment = new Intent(Camera.this, SelectedImage.class);
                startSelectedImageFragment.putExtra("calling-activity", ActivityConstants.STUDENT_CAMERA);
                startActivity(startSelectedImageFragment);
                finish();
                 */
            } else if (resultCode == RESULT_CANCELED) {
                finish();
                // User cancelled the image capture
            } else {
                finish();
                // Image capture failed, advise user
            }
        }
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Inquire");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Inquire", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }



    protected void onPause(){
        super.onPause();
    }

    protected void onResume(){
        super.onResume();
    }

    protected void onStop() {super.onStop(); }

    protected void onDestroy() {super.onDestroy(); }

    public static Bitmap getImage(){
        return image;
    }
}
