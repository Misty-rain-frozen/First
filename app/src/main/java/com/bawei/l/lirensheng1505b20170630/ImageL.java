package com.bawei.l.lirensheng1505b20170630;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * 类描述：无限轮播
 * 创建人: 李仁晟
 * 创建时间 L on 2017/6/30.
 */
public class ImageL extends ImageLoader {
    public ImageL(M2Activity m2Activity) {

    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
