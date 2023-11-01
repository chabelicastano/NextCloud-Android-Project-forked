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

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.owncloud.android.R;

public class LoginThemeActivity extends AppCompatActivity {
    private final int GALLERY_REQUEST_CODE = 1000;
    ImageView imgTester;
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
                    String selectedOption = radioButton.getText().toString();

                    if (selectedOption.equals("FIU Light")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    }
                    if (selectedOption.equals("FIU Dark")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                }

            }
        });

        Button backButton = findViewById(R.id.backButton);
        // navigate back to previous activity
        backButton.setOnClickListener(v -> onBackPressed());

        imgTester = findViewById(R.id.image_tester);
        Button uploadTester = findViewById(R.id.upload_tester);
        uploadTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryTest = new Intent(Intent.ACTION_PICK);
                galleryTest.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryTest,GALLERY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            if(requestCode==GALLERY_REQUEST_CODE){
                // For Gallery
                imgTester.setImageURI(data.getData());
                
            }
        }
    }
}


