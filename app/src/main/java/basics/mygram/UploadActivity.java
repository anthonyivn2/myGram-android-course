package basics.mygram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;
    ImageView imageView_Upload;
    Bitmap image_to_upload;
    EditText edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload2);
        imageView_Upload = (ImageView) findViewById(R.id.imageView_Upload);
        edit_text = (EditText)findViewById(R.id.editText);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
        }
        else if(requestCode == REQUEST_IMAGE_PICK){
            if(grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK && data != null){
                Bundle extras = data.getExtras();
                Bitmap photoBitmap = (Bitmap) extras.get("data");
                imageView_Upload.setImageBitmap(photoBitmap);
                image_to_upload = photoBitmap;
            }
            else{
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == REQUEST_IMAGE_PICK){
            if(resultCode == RESULT_OK && data != null){
                Uri selectedImage = data.getData();

                try{
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    imageView_Upload.setImageBitmap(imageBitmap);
                    image_to_upload = imageBitmap;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error, " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void buttonCameraClicked(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            }
            else{
                openCamera();
            }

        }
        else{
            openCamera();
        }

    }

    public void buttonUploadFromGalleryClicked(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
            }
            else{
                openGallery();
            }
        }
        else{
            openGallery();
        }
    }

    private void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { // Resolve Activity digunakan untuk menggunakan aplikasi diluar aplikasi yang sedang dijalankan
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);//Startactivityforresult dibutuhkan untuk berpindah ke hampir semua plikasi lain
        }
    }

    private void openGallery() {
        Intent getGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (getGalleryIntent.resolveActivity(getPackageManager()) != null) { // Resolve Activity digunakan untuk menggunakan aplikasi diluar aplikasi yang sedang dijalankan
            startActivityForResult(getGalleryIntent, REQUEST_IMAGE_PICK);//Startactivityforresult dibutuhkan untuk berpindah ke hampir semua plikasi lain
        }
    }

    public void uploadImage(View view) {
        if (image_to_upload == null){
            Toast.makeText(this, "No Image Yet!", Toast.LENGTH_SHORT).show();
            return;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image_to_upload.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile("image.png", byteArray);

        ParseObject obj = new ParseObject("Gambar");
        obj.put("username", ParseUser.getCurrentUser().getUsername());
        obj.put("image", file);
        obj.put("caption", edit_text.getText().toString());
        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(UploadActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UploadActivity.this, "Error, " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
