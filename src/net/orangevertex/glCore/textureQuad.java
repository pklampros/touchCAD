package net.orangevertex.glCore;

import android.opengl.*;
import java.nio.*;
import java.util.*;
//import net.orangevertex.*;
import net.orangevertex.glCore.*;
//import net.orangevertex.adaptivecanopy.ovx.gl.*;
import android.content.*;

public class textureQuad {


    // Handle to a program object
    private int mProgramObject;

    // Attribute locations
    private int mPositionLoc;
    private int mTexCoordLoc;

    // Sampler location
    private int mSamplerLoc;

    // Texture handle
    private int mTextureId;

    // Additional member variables
    private int mWidth;
    private int mHeight;
    private FloatBuffer mVertices;
    private ShortBuffer mIndices;

    private float[] mVerticesData;

    private final short[] mIndicesData =
    { 
		0, 1, 2, 0, 2, 3 
    };

	private int mMVPMatrixHandle;

	Context mActivityContext;
	float [] scale = new float [16];
	public boolean isValid;
	public boolean display = false;
	private int createSimpleTexture2D() {
        // Texture object handle
        int[] textureId = new int[1];

        // 2x2 Image, 3 bytes per pixel (R, G, B)
        byte[] pixels = 
		{  
			127,   0,   0, // Red
			0, 127,   0, // Green
			0,   0, 127, // Blue
			127, 127,   0  // Yellow
		};
        ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(4 * 3);
        pixelBuffer.put(pixels).position(0);

        // Use tightly packed data
        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);

        //  Generate a texture object
        GLES20.glGenTextures(1, textureId, 0);

        // Bind the texture object
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

        //  Load the texture
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 2, 2, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);

        // Set the filtering mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        return textureId[0];        
	}
	public textureQuad() {
	}
	public void generate(final Context activityContext, float size, float posX, float posY, float posZ, int resource) {
		calcStuff(size, posX, posY, posZ);
		mTextureId = texHelper.loadTexture(activityContext, resource);


	}
	public void generate(final Context activityContext, float size, float posX, float posY, float posZ, String s, int just) {
		calcStuff(size, posX, posY, posZ);
		mTextureId = textRenderer.loadText(activityContext, s, just, 32);


	}
	public void generate(final Context activityContext, float size, float posX, float posY, float posZ, String s) {
		calcStuff(size, posX, posY, posZ);
		mTextureId = textRenderer.loadText(activityContext, s, 2, 32);


	}
	public void generate(final Context activityContext, float size, int resource) {
		calcStuff(size, 0, 0, 0);
		mTextureId = texHelper.loadTexture(activityContext, resource);


	}
	public void generate(final Context activityContext, float size, String s) {
		calcStuff(size, 0, 0, 0);
		mTextureId = textRenderer.loadText(activityContext, s, 0, 32);
	}
	public void generate(final Context activityContext, float size, float posX, float posY, float posZ, String s,int just,int textSize) {
		calcStuff(size, 0, 0, 0);
		mTextureId = textRenderer.loadText(activityContext, s, just, textSize);
	}
	public void calcStuff(float size, float posX, float posY, float posZ) {
		mVerticesData = new float []
		{ 
			posX - 2 * size, posY - 2 * size, posZ, // Position 0
			0.0f, 0.0f, // TexCoord 0
			posX - 2 * size, posY + 2 * size, posZ, // Position 1
			0.0f, 1.0f, // TexCoord 1
			posX + 2 * size, posY + 2 * size, posZ, // Position 2
			1.0f, 1.0f, // TexCoord 2
			posX + 2 * size, posY - 2 * size, posZ, // Position 3
			1.0f, 0.0f // TexCoord 3
		};
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
			.order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
        mIndices = ByteBuffer.allocateDirect(mIndicesData.length * 2)
			.order(ByteOrder.nativeOrder()).asShortBuffer();
        mIndices.put(mIndicesData).position(0);


		String vShaderStr =	
			"uniform mat4 uMVPMatrix;" +
			"attribute vec4 a_position;   \n"
			+ "attribute vec2 a_texCoord;   \n"
			+ "varying vec2 v_texCoord;     \n"
			+ "void main()                  \n"
			+ "{                            \n"
			+ "   gl_Position = a_position * uMVPMatrix;; \n"
			+ "   v_texCoord = a_texCoord;  \n"
			+ "}                            \n";

        String fShaderStr = 
			"precision mediump float;                            \n"
			+ "varying vec2 v_texCoord;                            \n"
			+ "uniform sampler2D s_texture;                        \n"
			+ "void main()                                         \n"
			+ "{                                                   \n"
			+ "  gl_FragColor = texture2D( s_texture, v_texCoord );\n"
			+ "}                                                   \n";

        // Load the shaders and get a linked program object
        mProgramObject = ESShader.loadProgram(vShaderStr, fShaderStr);

        // Get the attribute locations
        mPositionLoc = GLES20.glGetAttribLocation(mProgramObject, "a_position");
        mTexCoordLoc = GLES20.glGetAttribLocation(mProgramObject, "a_texCoord");

        // Get the sampler location
        mSamplerLoc = GLES20.glGetUniformLocation(mProgramObject, "s_texture");

		Matrix.setIdentityM(scale, 0);
		isValid = true;
	}
    public void draw(float [] mvp) {
//		Matrix.scaleM(scale,0,5*(float)Math.sin(5*frameCount),5*(float)Math.sin(5*frameCount),0);
//		Matrix.multiplyMM(mvp,0,scale,0,mvp,0);
        // Use the program object
		System.out.println("numba 1");
        GLES20.glUseProgram(mProgramObject);

		System.out.println("numba 2");
        // Load the vertex position
        mVertices.position(0);
		System.out.println("numba 3");
        GLES20.glVertexAttribPointer(mPositionLoc, 3, GLES20.GL_FLOAT, 
									 false, 
									 5 * 4, mVertices);
		System.out.println("numba 4");
        // Load the texture coordinate
        mVertices.position(3);
		System.out.println("numba 5");
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
									 false, 
									 5 * 4, 
									 mVertices);
		System.out.println("numba 6");

        GLES20.glEnableVertexAttribArray(mPositionLoc);
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
		System.out.println("numba 7");

        // Bind the texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
		System.out.println("numba 8");

        // Set the sampler texture unit to 0
        GLES20.glUniform1i(mSamplerLoc, 0);
		System.out.println("numba 9");

		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramObject, "uMVPMatrix");
		glBase.checkGlError("glGetUniformLocation");
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvp, 0);
		glBase.checkGlError("glUniformMatrix4fv");

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, mIndices);
	}

}
