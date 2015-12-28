package com.pascalso.inquire;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

/**
 * Created by pso on 12/28/15.
 */
public class Gallery extends Activity {
    private static Bitmap image;
    private int REQUEST_GALLERY = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dispatchGalleryIntent();
    }

    private void dispatchGalleryIntent(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(galleryIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();
            image = BitmapFactory.decodeFile(imagePath);
            Intent startSelectedImageFragment = new Intent(Gallery.this, SelectedImage.class);
            startSelectedImageFragment.putExtra("calling-activity", ActivityConstants.GALLERY);
            startActivity(startSelectedImageFragment);
            finish();
        }
        else{
            onDestroy();
            startActivity(new Intent(Gallery.this, Student.class));
            finish();

        }
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
