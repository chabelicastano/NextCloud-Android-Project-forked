package com.owncloud.android.ui.fragment;

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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nextcloud.android.common.ui.theme.utils.ColorRole;
import com.nextcloud.client.di.Injectable;
import com.owncloud.android.R;
import com.owncloud.android.features.FeatureItem;
import com.owncloud.android.utils.theme.ViewThemeUtils;

import java.io.File;
import java.io.FileInputStream;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FeatureFragment extends Fragment implements Injectable {
    private FeatureItem item;
    @Inject ViewThemeUtils.Factory viewThemeUtilsFactory;
    private ViewThemeUtils viewThemeUtils;

    static public FeatureFragment newInstance(FeatureItem item) {
        FeatureFragment f = new FeatureFragment();
        Bundle args = new Bundle();
        args.putParcelable("feature", item);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewThemeUtils = viewThemeUtilsFactory.withPrimaryAsBackground();
        super.onCreate(savedInstanceState);
        item = getArguments() != null ? (FeatureItem) getArguments().getParcelable("feature") : null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.whats_new_element, container, false);

        ImageView whatsNewImage = view.findViewById(R.id.whatsNewImage);
        if (item.shouldShowImage()) {
            /*
            final Drawable image = ResourcesCompat.getDrawable(getResources(), item.getImage(), null);
            if (image != null) {
                whatsNewImage.setImageDrawable(viewThemeUtils.platform.tintDrawable(requireContext(), image, ColorRole.ON_PRIMARY));
            }*/


            File filePath = this.getContext().getFileStreamPath("logoTest.png");
            if (filePath.exists()){
                Bitmap testBitmap = readFromInternalStorage("logoTest.png");
                Log.d("Image File Found", "CONFIRMATION THAT THE BITMAP FILE IS FOUND AND READ"); //DEBUG MESSAGE
                whatsNewImage.setImageBitmap(testBitmap);
            }

            else{
                Drawable fiu_logo = getResources().getDrawable(R.drawable.fiu_alone);
                whatsNewImage.setImageDrawable(fiu_logo);
            }

        }

        TextView whatsNewTitle = view.findViewById(R.id.whatsNewTitle);
        if (item.shouldShowTitleText()) {
            whatsNewTitle.setText(item.getTitleText());
            viewThemeUtils.platform.colorTextView(whatsNewTitle, ColorRole.ON_PRIMARY);
            whatsNewTitle.setVisibility(View.VISIBLE);
        } else {
            whatsNewTitle.setVisibility(View.GONE);
        }

        LinearLayout linearLayout = view.findViewById(R.id.whatsNewTextLayout);
        if (item.shouldShowContentText()) {
            if (item.shouldShowBulletPointList()) {
                String[] texts = getText(item.getContentText()).toString().split("\n");

                for (String text : texts) {
                    TextView textView = generateTextView(text, requireContext(),
                                                         item.shouldContentCentered(), true);

                    linearLayout.addView(textView);
                }
            } else {
                TextView textView = generateTextView(getText(item.getContentText()).toString(),
                                                     requireContext(), item.shouldContentCentered(), false);

                linearLayout.addView(textView);
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }

        return view;
    }

    private TextView generateTextView(String text, Context context,
                                      boolean shouldContentCentered,
                                      boolean showBulletPoints) {
        int standardMargin = context.getResources().getDimensionPixelSize(R.dimen.standard_margin);
        int doubleMargin = context.getResources()
            .getDimensionPixelSize(R.dimen.standard_double_margin);
        int zeroMargin = context.getResources().getDimensionPixelSize(R.dimen.zero);

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(doubleMargin, standardMargin, doubleMargin, zeroMargin);
        textView.setTextAppearance(context, R.style.NextcloudTextAppearanceMedium);
        textView.setLayoutParams(layoutParams);

        if (showBulletPoints) {
            BulletSpan bulletSpan = new BulletSpan(standardMargin);
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(bulletSpan, 0, spannableString.length(),
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        } else {
            textView.setText(text);
        }
        viewThemeUtils.platform.colorTextView(textView, ColorRole.ON_PRIMARY);

        if (!shouldContentCentered) {
            textView.setGravity(Gravity.START);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        return textView;
    }

    private Bitmap readFromInternalStorage(String filename){
        try {
            File filePath = this.getContext().getFileStreamPath(filename);
            FileInputStream fi = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(fi);
        } catch (Exception ex) { /* do nothing here */}

        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        File filePath = this.getContext().getFileStreamPath("logoTest.png");
        ImageView whatsNewImage = getView().findViewById(R.id.whatsNewImage);
        if (filePath.exists()){
            Bitmap testBitmap = readFromInternalStorage("logoTest.png");
            Log.d("Image File Found", "CONFIRMATION THAT THE BITMAP FILE IS FOUND AND READ"); //DEBUG MESSAGE
            whatsNewImage.setImageBitmap(testBitmap);
        }
        else{
            Drawable fiu_logo = getResources().getDrawable(R.drawable.fiu_alone);
            whatsNewImage.setImageDrawable(fiu_logo);
        }
    }
}
