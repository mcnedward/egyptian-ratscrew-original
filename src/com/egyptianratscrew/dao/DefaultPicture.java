package com.egyptianratscrew.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DefaultPicture {
	public static Bitmap getImage(Context context) {
		int resourceId = context.getResources().getIdentifier("b2fv", "drawable", context.getPackageName());
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
		return bitmap;
	}
}
