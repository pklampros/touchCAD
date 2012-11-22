package net.orangevertex.glCore;

import android.opengl.*;
import java.nio.*;
import java.util.*;
//import net.orangevertex..*;
import net.orangevertex.glCore.*;
//import net.orangevertex.adaptivecanopy.ovx.gl.*;

public class polygon {
	public final String vertexShaderCode =
	"uniform mat4 uMVPMatrix;" +
	"attribute vec4 vPosition;" +
	"void main() {" +
//	"  v_TexCoordinate = a_TexCoordinate;" +
	"  gl_Position = vPosition * uMVPMatrix;" +
	"}";

	public final String fragmentShaderCode =
	"precision mediump float;" +
	"uniform vec4 vColor;" +
	"void main() {" +
	"  gl_FragColor = vColor;" +
	"}";
	private FloatBuffer vertexBuffer;
	private int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;
	public boolean border = false;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	float [] triangleCoords;
	private int polyCount;
	private int offsetCount;
	private int vertexCount;
	private int vertexStride;
	float [] transMatrix,scaleMatrix,mvpMatrix;

	// Set color with red, green, blue and alpha (opacity) values
	public float color[] = { 0f, 0f, 0.92265625f, 1.0f };
	boolean drawPoly = true;
	boolean drawOffset = true;


	public glVector cent;
	public glVector [] ptz;
	public int t, yr, sess, exMode;
	int selected = 0;
	public glVector [] offset;
	float offDist = 0.002f;
	public float pMinX,pMaxX,pMinY,pMaxY;
	public boolean current = false;

	
	int currYear;
	int currSession;
	int externalMode;

	public polygon(int type, int yaer, int session, int xm, glVector [] pointz) {
		t = type; // 0 = ? / 1 = for protection / 2 = footing / 3 - no footing / 4 -feature
		ptz = pointz;
		yr = yaer;
		sess = session;
		exMode = xm;
		calcStuff();
	}	
	public polygon(glVector [] pointz) {
		t = 0; // 0 = ? / 1 = for protection / 2 = footing
		ptz = pointz;
		yr = 0;
		calcStuff();
	}
	public polygon(int type, int yaer, int session, int xm, ArrayList<glVector> pointz) {
		t = type;
		ptz = new glVector[pointz.size()];
		for (int i = 0; i < ptz.length;i++) {
			ptz[i] = pointz.get(i);
		}
		yr = yaer;
		sess = session;
		exMode = xm;
//		current = c;
		calcStuff();
	}
	public void newType(int type) {
		t = type;
		current = true;
//		if(yr != GLRenderer.currYear || sess != GLRenderer.currSession) current = false;
		if(yr != currYear || sess != currSession) current = false;
		switch (t) {
			case 1:
				if (current) color = new float [] { 255, 180, 0, 180 };
				 else color = new float [] { 185, 152, 71, 180};
				break;
			case 2:
				if (current) color = new float [] { 0, 73, 255, 180 };
				else color = new float [] { 75, 105, 181, 180 };
				break;
			case 3:
//				if (current) color = new float [] { 115f, 255, 0, 180 };
//				else color = new float [] { 122, 183, 73, 180};
				color = new float [] { 128f, 128, 128, 180 };
//				else color = new float [] { 122, 183, 73, 180};
				break;
			case 4:
				if (current) color = new float [] { 255, 0, 0, 180 };
				else color = new float [] { 183, 73, 73, 180 };
				break;
		}
//		if(current && exMode == 0 && GLRenderer.exMode == 1) 
			if(current && exMode == 0 && externalMode == 1) 
				color[3] = 50;
		for (int i = 0; i < 4; i++) color[i] *= 0.00392156f; // -- 1/255
	}
//	public void calcStuff() {
//		newType(t);
	public void calcStuff() {
		newType(t);
		cent = new glVector(0, 0, 0);
		for (int i = 0; i < ptz.length; i++) {
			if (i == 0) {
				pMinX = pMaxX = ptz[i].x;
				pMinY = pMaxY = ptz[i].y;
			}
			if (ptz[i].x < pMinX) pMinX = ptz[i].x;
			if (ptz[i].x > pMaxX) pMaxX = ptz[i].x;
			if (ptz[i].y < pMinY) pMinY = ptz[i].y;
			if (ptz[i].y > pMaxY) pMaxY = ptz[i].y;
			cent.add(ptz[i]);
		}
		cent.div(ptz.length);
//		offset = ovv.offsetChain(ptz, offDist);
		buildGraphics();
	}
	public polygon get() {
		glVector [] temp = new glVector[ptz.length];
		System.arraycopy(ptz, 0, temp, 0, ptz.length);
		return new polygon(t, yr, sess, exMode, temp);
	}
	public void addVertex(float x, float y, float z) {
		float [] temp = new float[triangleCoords.length + 3];
		System.arraycopy(triangleCoords, 0, temp, 0, triangleCoords.length);
		temp[temp.length - 3] = x;
		temp[temp.length - 2] = y;
		temp[temp.length - 1] = z;
		triangleCoords = new float[temp.length];

	}
//	void draw()
//	{
	public void buildGraphics() {
//		triangleCoords = new float[3 * (ptz.length + offset.length)];
//		for (int i = 0; i < (ptz.length + offset.length);i ++)
		triangleCoords = new float[3 * (ptz.length)];
		for (int i = 0; i < (ptz.length);i ++) {
//			if (i < ptz.length)
//			{
			triangleCoords[3 * i] = ptz[i].x;
			triangleCoords[3 * i + 1] = ptz[i].y;
			triangleCoords[3 * i + 2] = ptz[i].z;
//			}
//			else
//			{
//				triangleCoords[3 * i] = offset[i - ptz.length].x;
//				triangleCoords[3 * i + 1] = offset[i - ptz.length].y;
//				triangleCoords[3 * i + 2] = offset[i - ptz.length].z;
//			}
		}

		transMatrix = new float[16];
		scaleMatrix = new float[16];
		mvpMatrix = new float[16];

		polyCount = ptz.length;
		offsetCount = offset.length;

		vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
		vertexStride = COORDS_PER_VERTEX * 4;
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
			// (number of coordinate values * 4 bytes per float)
			triangleCoords.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());


		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(triangleCoords);
		vertexBuffer.position(0);

		// prepare shaders and OpenGL program
		int vertexShader = glBase.loadShader(GLES20.GL_VERTEX_SHADER,
												 vertexShaderCode);
		int fragmentShader = glBase.loadShader(GLES20.GL_FRAGMENT_SHADER,
												   fragmentShaderCode);

		mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables



	}
	void updatePosition(glVector tr) {
		Matrix.setIdentityM(transMatrix, 0);
		glBase.translate(transMatrix, tr.x, tr.y, tr.z);

	}
	public void updatePosition(glVector tr, glVector sc) {
//			br.updateLine(index,tr);
//			br.indLines[index].update(tr);
		Matrix.setIdentityM(transMatrix, 0);
		Matrix.setIdentityM(scaleMatrix, 0);
		glBase.translate(transMatrix, tr.x, tr.y, tr.z);
		Matrix.scaleM(scaleMatrix, 0, sc.x, sc.y, sc.z);

	}

	public void draw(float[] mvp) {
		GLES20.glUseProgram(mProgram);
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
									 GLES20.GL_FLOAT, false,
									 vertexStride, vertexBuffer.position(0));

		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		glBase.checkGlError("glGetUniformLocation");
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvp, 0);
		glBase.checkGlError("glUniformMatrix4fv");
		if (drawPoly) {
			if (!border) GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, polyCount);
			else GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, polyCount);
		}
		GLES20.glDisableVertexAttribArray(mPositionHandle);


		for (int i = 0; i < offset.length - 1; i++) {
		}
	}
	public boolean isInPoly(float x, float y) {
//		if (ovv.inPolyCheck(new glVector(x, y), ptz) == 1) return true;
//		else 
		return false;

	}

}
