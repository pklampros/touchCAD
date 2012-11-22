package net.orangevertex.glCore;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.opengl.*;

public class textRenderer {
	public static int loadText(Context context, String s, int alignment,int textSize) {
		// create an empty mutable bitmap
		Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ALPHA_8);
		// get a canvas to paint over the bitmap
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0);                // Set Transparent Background (ARGB)

		// get a background image from resources
		// note the image format must match the bitmap format

//		Drawable background = context.getResources().getDrawable(R.drawable.splash);
//		background.setBounds(0,0,256,256);
//		background.draw(canvas);

		Paint textPaint = new Paint();
		textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		textPaint.setTextSize(textSize);
		textPaint.setAntiAlias(true);
//		textPaint.setFakeBoldText(true);
		textPaint.setStrokeWidth(2f);
		textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
		int x = 0, y = -2*(int)(textPaint.ascent() + textPaint.descent());
		switch (alignment) {
			case 0:
				x = 16;
				textPaint.setTextAlign(Paint.Align.LEFT);
				break;
			case 1:
				textPaint.setTextAlign(Paint.Align.CENTER);
				canvas.translate(256, 0);
				break;
			case 2:
				textPaint.setTextAlign(Paint.Align.RIGHT);
				canvas.translate(490, 0);
				break;
		}
//		canvas.drawText(""+s,16,112,textPaint);
//		int x = 16, y = 0;
		for (String line: s.split("\n")) {
			canvas.drawText(line, x, y, textPaint);
			y -= 2f*(textPaint.ascent() + textPaint.descent());
		}
		int[] textureId = new int[1];
		// generate one texture pointer
		GLES20.glGenTextures(1, textureId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		return textureId[0];
	}
}
	
