package com.yanzhenjie.album.widget.gallery.view;

import android.content.Context;
import android.graphics.Canvas;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.widget.gallery.model.GalleryPhotoModel;
import com.yanzhenjie.album.widget.photoview.AttacherImageView;
import com.yanzhenjie.album.widget.photoview.PhotoViewAttacher;


public class GalleryPhotoView extends AttacherImageView {

    private GalleryPhotoModel photoModel;

    public GalleryPhotoView(Context context, GalleryPhotoModel photoModel) {
        super(context);
        this.photoModel = photoModel;
        setAttacher(new PhotoViewAttacher(this));
    }

    public void startGlide() {
        Glide.with(getContext()).load(photoModel.photoSource).into(GalleryPhotoView.this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
