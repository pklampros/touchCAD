package net.orangevertex.glCore;

import android.content.*;
import android.graphics.*;
import android.opengl.*;

public class texHelper {
	public static int loadTexture(final Context context, final int resourceId) {
		final int [] textureHandle = new int[1];
		GLES20.glGenTextures(1, textureHandle, 0);
		if (textureHandle[0] != 0) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled =false; // no pre scaling

			// read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

			// bind texture to opengl
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

			// set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
//			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

			// load the bitmap into the bound texture
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		//			GLES20.glBlendFunc(GLES20.GL_ONE,GLES20.GL_ONE);

			// recycle the bitmap into the bound texture
			bitmap.recycle();			
		}
		if(textureHandle[0] == 0) {
			throw new RuntimeException("Error loading texture.");
		}
		return textureHandle[0];		
	}
}
