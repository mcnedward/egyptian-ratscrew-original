package com.egyptianratscrew.dao;

import com.egyptianratscrew.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DefaultPicture {
	public static Bitmap getImage(Context context) {
		//int resourceId = context.getResources().getIdentifier("b2fv", "drawable", context.getPackageName());
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.b2fv);//resourceId);
		return bitmap;
	}
}
