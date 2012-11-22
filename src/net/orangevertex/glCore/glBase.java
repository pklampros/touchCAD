package net.orangevertex.glCore;

import android.opengl.*;
import android.util.*;
import java.util.*;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;
import net.orangevertex.glCore.*;
//import net.orangevertex.adaptivecanopy.obj.*;
//import net.orangevertex.adaptivecanopy.ovx.gl.*;
import android.content.*;
import android.os.*;


public class glBase {
	private static final String TAG = "GLRenderer";


	public static int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}
	public static void translate(float [] m, float x, float y, float z) {
		m[3] += x;
		m[7] += y;
		m[11] += z;
	}
	
}
