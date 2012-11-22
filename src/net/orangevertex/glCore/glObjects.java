package net.orangevertex.glCore;

//import net.orangevertex.adaptivecanopy.glCore.glVector;
//import net.orangevertex.adaptivecanopy.ovx.gl.*;
import java.nio.*;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import java.util.ArrayList;

public class glObjects {
//public glObjects() {}
	public static String TAG;
	public final static String vertexShaderCode =
	"uniform mat4 uMVPMatrix;" +
	"attribute vec4 vPosition;" +
	"void main() {" +
	"  gl_Position = vPosition * uMVPMatrix;" +
	"}";

	public final static String fragmentShaderCode =
	"precision mediump float;" +
	"uniform vec4 vColor;" +
	"void main() {" +
	"  gl_FragColor = vColor;" +
	"}";

	static class circle {
		private final FloatBuffer vertexBuffer;
		private final int mProgram;
		private int mPositionHandle;
		private int mColorHandle;
		private int mMVPMatrixHandle;

		static final int COORDS_PER_VERTEX = 3;
		float [] vertexCoords;
		private int vertexCount;
		private int vertexStride;
		float [] transMatrix;

		float color[] = { 0f, 0f, 0.92265625f, 1.0f };
		public circle(float r) {
			transMatrix = new float[16];
			int sides = 24;
			vertexCoords = new float[sides * 3];
			double angle = 2 * Math.PI / sides;

			for (int i = 0; i < sides; i++) {
				vertexCoords[3 * i] = (float) (Math.cos(i * angle) * r);
				vertexCoords[3 * i + 1] = (float) (Math.sin(i * angle) * r);
				vertexCoords[3 * i + 2] = 0f;
			}
			vertexCount = vertexCoords.length / COORDS_PER_VERTEX;
			vertexStride = COORDS_PER_VERTEX * 4;
			ByteBuffer bb = ByteBuffer.allocateDirect(
                vertexCoords.length * 4);
			bb.order(ByteOrder.nativeOrder());
			vertexBuffer = bb.asFloatBuffer();
			vertexBuffer.put(vertexCoords);
			vertexBuffer.position(0);
			int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
										  vertexShaderCode);
			int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
											fragmentShaderCode);

			mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
			GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
			GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
			GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		}
		public void draw(float[] mvp, float x, float y) {
			float [] mvpMatrix = new float [16];
			System.arraycopy(mvp, 0, mvpMatrix, 0, 16);
			Matrix.setIdentityM(transMatrix, 0);
//
			glBase.translate(transMatrix, x, y, 0);
			Matrix.multiplyMM(mvpMatrix, 0, transMatrix, 0, mvpMatrix, 0);
			GLES20.glUseProgram(mProgram);
			mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
										 GLES20.GL_FLOAT, false,
										 vertexStride, vertexBuffer);
			mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
			GLES20.glUniform4fv(mColorHandle, 1, color, 0);
			mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			checkGlError("glGetUniformLocation");
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			checkGlError("glUniformMatrix4fv");
			GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount);
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
	}
	static class multiCircle {
		private final FloatBuffer vertexBuffer;
		private final int mProgram;
		private int mPositionHandle;
		private int mColorHandle;
		private int mMVPMatrixHandle;

		static final int COORDS_PER_VERTEX = 3;
		float [] triangleCoords;
		private int vertexCount;
		private int vertexStride;
		float [] transMatrix;

		float color[] = { 0f, 0f, 0.92265625f, 1.0f };
		public multiCircle(glVector [] ptz, float r) {
			transMatrix = new float[16];
			int sides = 24;
			triangleCoords = new float[ptz.length * sides * 3];
			double angle = 2 * Math.PI / sides;
			for (int p = 0; p < ptz.length;p++) {
				for (int i = 0; i < sides; i++) {
					triangleCoords[3 * (p * sides + i)] = ptz[p].x + (float) (Math.cos(i * angle) * r);
					triangleCoords[3 * (p * sides + i) + 1] = ptz[p].y + (float) (Math.sin(i * angle) * r);
					triangleCoords[3 * (p * sides + i) + 2] = 0f;
				}
			}
			vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
			vertexStride = COORDS_PER_VERTEX * 4;
			ByteBuffer bb = ByteBuffer.allocateDirect(
                triangleCoords.length * 4);
			bb.order(ByteOrder.nativeOrder());
			vertexBuffer = bb.asFloatBuffer();
			vertexBuffer.put(triangleCoords);
			vertexBuffer.position(0);
			int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
										  vertexShaderCode);
			int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
											fragmentShaderCode);

			mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
			GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
			GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
			GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		}
		public void draw(float[] mvpMatrix) {
//			float [] mvpMatrix = new float [16];
//			System.arraycopy(mvp, 0, mvpMatrix, 0, 16);
//			Matrix.setIdentityM(transMatrix, 0);
////
//			translate(transMatrix, x, y, 0);
//			Matrix.multiplyMM(mvpMatrix, 0, transMatrix, 0, mvpMatrix, 0);
			GLES20.glUseProgram(mProgram);
			mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
										 GLES20.GL_FLOAT, false,
										 vertexStride, vertexBuffer);
			mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
			GLES20.glUniform4fv(mColorHandle, 1, color, 0);
			mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			checkGlError("glGetUniformLocation");
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			checkGlError("glUniformMatrix4fv");
			GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
	}
	public static class mobiLine {
		private FloatBuffer vertexBuffer;
		private int mProgram;
		private int mPositionHandle;
		private int mColorHandle;
		private int mMVPMatrixHandle;
		static final int COORDS_PER_VERTEX = 3;
		float [] vertexCoords;
		private int vertexCount;
		private int vertexStride;
		private int lineNum =0;
		public float color[] = { 0f, 0f, 0f, 1.0f };
		public boolean valid;

		public mobiLine() {
		}
		void constructStatic(float [] staticCoords) {
			lineNum = staticCoords.length/3;
			vertexCoords = new float[staticCoords.length*2];
			for(int i = 0; i < lineNum; i++) {
				vertexCoords[6*i] = staticCoords[3*i];
				vertexCoords[6*i+1] = staticCoords[3*i+1];
				vertexCoords[6*i+2] = 0f;
			}
			valid = false;
		}
		void constructMoving(float movingCoordX,float movingCoordY) {
			for(int i = 0; i < lineNum; i++) {
				vertexCoords[6*i+3] = movingCoordX;
				vertexCoords[6*i+4] = movingCoordY;
				vertexCoords[6*i+5] = 0f;
			}
			valid = true;
		}
		void build() {
			
			vertexCount = vertexCoords.length / COORDS_PER_VERTEX;
			vertexStride = COORDS_PER_VERTEX * 4;
			ByteBuffer bb = ByteBuffer.allocateDirect(
                vertexCoords.length * 4);
			bb.order(ByteOrder.nativeOrder());
			vertexBuffer = bb.asFloatBuffer();
			vertexBuffer.put(vertexCoords);
			vertexBuffer.position(0);
			int vertexShader = glObjects.loadShader(GLES20.GL_VERTEX_SHADER,
													vertexShaderCode);
			int fragmentShader = glObjects.loadShader(GLES20.GL_FRAGMENT_SHADER,
													  fragmentShaderCode);

			mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
			GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
			GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
			GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		}
		void draw(float[] mvpMatrix) {

			GLES20.glUseProgram(mProgram);
			mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
										 GLES20.GL_FLOAT, false,
										 vertexStride, vertexBuffer);

			mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
			GLES20.glUniform4fv(mColorHandle, 1, color, 0);
			mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			glObjects.checkGlError("glGetUniformLocation");
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			glObjects.checkGlError("glUniformMatrix4fv");
			GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
	}

	public static class multiLine {
		private FloatBuffer vertexBuffer;
		private int mProgram;
		private int mPositionHandle;
		private int mColorHandle;
		private int mMVPMatrixHandle;

		// number of coordinates per vertex in this array
		static final int COORDS_PER_VERTEX = 3;
//		float [] vertexCoords;
		private int vertexCount;
		private int vertexStride;
		public float fit;
		float [] t,s,mvpMatrix;

		// Set color with red, green, blue and alpha (opacity) values
		public float color[] = { 0f, 0f, 0.92265625f, 1.0f };
		public multiLine(ArrayList<glVector> vertices) {
			float [] vertexCoords = new float[vertices.size()*3];
			for(int i = 0; i < vertices.size(); i++) {
				vertexCoords[3*i] = vertices.get(i).x;
				vertexCoords[3*i+1] = vertices.get(i).y;
				vertexCoords[3*i+2] = 0;
			}
			build(vertexCoords);
		}
		public multiLine(glVector [] vertices) {
			float [] vertexCoords = new float[vertices.length*3];
			for(int i = 0; i < vertices.length; i++) {
				vertexCoords[3*i] = vertices[i].x;
				vertexCoords[3*i+1] = vertices[i].y;
				vertexCoords[3*i+2] = vertices[i].z;
			}
			build(vertexCoords);
		}
		public multiLine(float [] vertexCoords) {
			build(vertexCoords);
		}
		void build(float [] vertexCoords) {
			t = new float[16];
			s = new float[16];
			mvpMatrix = new float[16];
			vertexCount = vertexCoords.length / COORDS_PER_VERTEX;
			vertexStride = COORDS_PER_VERTEX * 4;
	        // initialize vertex byte buffer for shape coordinates
			ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertexCoords.length * 4);
			// use the device hardware's native byte order
			bb.order(ByteOrder.nativeOrder());

			// create a floating point buffer from the ByteBuffer
			vertexBuffer = bb.asFloatBuffer();
			// add the coordinates to the FloatBuffer
			vertexBuffer.put(vertexCoords);
			// set the buffer to read the first coordinate
			vertexBuffer.position(0);

			// prepare shaders and OpenGL program
			int vertexShader = glObjects.loadShader(GLES20.GL_VERTEX_SHADER,
													vertexShaderCode);
			int fragmentShader = glObjects.loadShader(GLES20.GL_FRAGMENT_SHADER,
													  fragmentShaderCode);

			mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
			GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
			GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
			GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		}
		public void draw(float[] mvpMatrix) {
//			System.arraycopy(mvp, 0, mvpMatrix, 0, mvp.length);
//			Matrix.multiplyMM(mvpMatrix, 0, s, 0, mvpMatrix, 0);
//			Matrix.multiplyMM(mvpMatrix, 0, t, 0, mvpMatrix, 0);

			GLES20.glUseProgram(mProgram);
			mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
										 GLES20.GL_FLOAT, false,
										 vertexStride, vertexBuffer);

			mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
			GLES20.glUniform4fv(mColorHandle, 1, color, 0);
			mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			glObjects.checkGlError("glGetUniformLocation");
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			glObjects.checkGlError("glUniformMatrix4fv");
			GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
//		public int compareTo(Object objB)
//		{
//			glMultiLines b = (glMultiLines) objB;
//			if (fit > b.fit) return 1;
//			else if (fit < b.fit) return -1;
//			else return 0;
//		}
		public void update(glVector tr, glVector sc) {

			Matrix.setIdentityM(t, 0);
			Matrix.setIdentityM(s, 0);
			glBase.translate(t, tr.x, tr.y, tr.z);
			Matrix.scaleM(s, 0, sc.x, sc.y, sc.z);
		}
		public void update(glVector tr) {
			Matrix.setIdentityM(t, 0);
			glBase.translate(t, tr.x, tr.y, tr.z);
		}
	}
	
	public static class axizLines {
		private final FloatBuffer vertexBuffer;
		private final int mProgram;
		private int mPositionHandle;
		private int mColorHandle;
		private int mMVPMatrixHandle;
		static final int COORDS_PER_VERTEX = 3;
		float [] triangleCoords;
		private int vertexCount;
		private int vertexStride;
		float color[] = { 0f, 0f, 0.92265625f, 1.0f };
		public axizLines(float size) {
			triangleCoords = new float [] {
				0f,0f,0f,
				size,0f,0f,
				0f,0f,0f,
				0f,0,size,
				0f,0f,0f,
				0f,size,0f,
//				0f,0f,0f,
//				size,size,size
			};
			vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
			vertexStride = COORDS_PER_VERTEX * 4;
	        // initialize vertex byte buffer for shape coordinates
			ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
			// use the device hardware's native byte order
			bb.order(ByteOrder.nativeOrder());

			// create a floating point buffer from the ByteBuffer
			vertexBuffer = bb.asFloatBuffer();
			// add the coordinates to the FloatBuffer
			vertexBuffer.put(triangleCoords);
			// set the buffer to read the first coordinate
			vertexBuffer.position(0);

			// prepare shaders and OpenGL program
			int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
										  vertexShaderCode);
			int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
											fragmentShaderCode);

			mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
			GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
			GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
			GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
		}
		public void draw(float[] mvpMatrix) {
			GLES20.glUseProgram(mProgram);
			mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
										 GLES20.GL_FLOAT, false,
										 vertexStride, vertexBuffer);

			mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
			GLES20.glUniform4fv(mColorHandle, 1, color, 0);
			mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
			checkGlError("glGetUniformLocation");
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
			checkGlError("glUniformMatrix4fv");
			GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
	}
//	public class polygon {
		

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
}
