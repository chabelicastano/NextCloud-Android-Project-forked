package com.owncloud.android.ui.activity;
/*
    Nextcloud Android client application

    Copyright (C) 2023 Ralph Calixte for FIU senior project
    Copyright (C) 2023 Chabeli Castano for FIU senior project

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU AFFERO GENERAL PUBLIC LICENSE
    License as published by the Free Software Foundation; either
    version 3 of the License, or any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU AFFERO GENERAL PUBLIC LICENSE for more details.

    You should have received a copy of the GNU Affero General Public
    License along with this program.  If not, see http://www.gnu.org/licenses/

 */

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.owncloud.android.R;
import com.owncloud.android.authentication.AuthenticatorActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginThemeActivity extends AppCompatActivity {
    static String selectedOption;
    private final int GALLERY_REQUEST_CODE = 1000;
    ImageView imgTester;
    Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_theme);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                RadioButton radioButton = findViewById(checked);

                if (radioButton != null) {
                    selectedOption = radioButton.getText().toString();

                    if (selectedOption.equals("FIU Light")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    }
                    if (selectedOption.equals("FIU Dark")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }

                }

            }
        });
        //Set default radio button
        int currentMode = AppCompatDelegate.getDefaultNightMode();

        if(currentMode == AppCompatDelegate.MODE_NIGHT_NO)
            radioGroup.check(R.id.radioButton);

        if(currentMode == AppCompatDelegate.MODE_NIGHT_YES)
            radioGroup.check(R.id.radioButton2);


        Button backButton = findViewById(R.id.backButton);
        // navigate back to previous activity
        backButton.setOnClickListener(v -> onBackPressed());

        imgTester = findViewById(R.id.image_tester);

        //setting imageView
        File filePath = this.getFileStreamPath("logoTest.png");
        if (filePath.exists()){
            Bitmap testBitmap = readFromInternalStorage("logoTest.png");
            Log.d("Image File Found", "CONFIRMATION THAT THE BITMAP FILE IS FOUND AND READ"); //DEBUG MESSAGE
            imgTester.setImageBitmap(testBitmap);
        }
        else{
            Drawable fiu_logo = getResources().getDrawable(R.drawable.fiu_alone);
            imgTester.setImageDrawable(fiu_logo);
        }

        Button uploadTester = findViewById(R.id.upload_tester);
        uploadTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryTest = new Intent(Intent.ACTION_PICK);
                galleryTest.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryTest,GALLERY_REQUEST_CODE);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(LoginThemeActivity.this, AuthenticatorActivity.class);
                startActivity(intent);
            }
        });

        Button resetButton = findViewById(R.id.loginThemeReset);
        resetButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(filePath.exists()){
                    filePath.delete();
                    Drawable fiu_logo = getResources().getDrawable(R.drawable.fiu_alone);
                    imgTester.setImageDrawable(fiu_logo);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            if(requestCode==GALLERY_REQUEST_CODE){
                // For Gallery
                Uri imageURIfromGallery = data.getData();
                imgTester.setImageURI(imageURIfromGallery);
                uriToBitmap(imageURIfromGallery);
                saveToInternalStorage(imageBitmap);
            }
        }
    }


    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            imageBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // save image to internal storage to make it persistent
    private boolean saveToInternalStorage(Bitmap image) {

        try {
            FileOutputStream fos = this.openFileOutput("logoTest.png", Context.MODE_PRIVATE);

            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private Bitmap readFromInternalStorage(String filename){
        try {
            File filePath = this.getFileStreamPath(filename);
            FileInputStream fi = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(fi);
        } catch (Exception e) {}

        return null;
    }
}
