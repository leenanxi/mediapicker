package com.leenanxi.android.open.common.ImagePicker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leenanxi.android.open.common.R;

/**
 * Created by leenanxi on 16/4/12.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {
    private View mContentView;


    /**
     * @deprecated Use {@link #newInstance(View)} instead.
     */
    public BottomSheetFragment(){

    }


    public  BottomSheetFragment newInstance(View contentView){
        this.mContentView = contentView;
        return new BottomSheetFragment();
    }




    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        if(mContentView == null) {
        mContentView  = new ImagePickerSheetView.Builder(getContext())
                    .setMaxItems(30)
                    .setShowCameraOption(true)
                    .setShowPickerOption(true)
                    .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                        @Override
                        public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                            Glide.with(getContext())
                                    .load(imageUri)
                                    .centerCrop()
                                    .crossFade()
                                    .into(imageView);
                        }
                    })
                    .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                        @Override
                        public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {

                        }
                    })
                    .setTitle("Choose an image (Long Click Multiple Choice)")
                    .create();
        }
        dialog.setContentView(mContentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) mContentView.getParent()).getLayoutParams();
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        mContentView.requestLayout();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if( behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setPeekHeight(((BottomSheetBehavior) behavior).getPeekHeight()+dip2px(getContext(),30));
        }
    }

    private int dip2px(Context context, float dpValue) {
        if (context == null) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getStatusBarHeight() {
        Rect rect = new Rect();
        Window win = getActivity().getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * This checks to see if there is a suitable activity to handle the {@link MediaStore#ACTION_IMAGE_CAPTURE}
     * intent and returns it if found. {@link MediaStore#ACTION_IMAGE_CAPTURE} is for letting another app take
     * a picture from the camera and store it in a file that we specify.
     *
     * @return A prepared intent if found.
     */
    @Nullable
    private Intent createCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }


    /**
     * This checks to see if there is a suitable activity to handle the `ACTION_PICK` intent
     * and returns it if found. {@link Intent#ACTION_PICK} is for picking an image from an external app.
     *
     * @return A prepared intent if found.
     */
    @Nullable
    private Intent createPickIntent() {
        Intent picImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (picImageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

}
