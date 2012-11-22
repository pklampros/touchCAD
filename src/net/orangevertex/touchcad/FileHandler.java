package net.orangevertex.touchcad;
//
// Book:      OpenGL(R) ES 2.0 Programming Guide
// Authors:   Aaftab Munshi, Dan Ginsburg, Dave Shreiner
// ISBN-10:   0321502795
// ISBN-13:   9780321502797
// Publisher: Addison-Wesley Professional
// URLs:      http://safari.informit.com/9780321563835
//            http://www.opengles-book.com
//

// ESShader
//
//    Utility functions for loading shaders and creating program objects.
//

import android.content.*;
import android.graphics.*;
import android.opengl.*;
import android.os.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import net.orangevertex.glCore.*;

public class FileHandler {
	Context pContext;
	public FileHandler(Context parentContext) {
		pContext = parentContext;
	}
	public void clearFile(String fiel) {
		try {
			FileOutputStream fos = pContext.openFileOutput(fiel, Context.MODE_PRIVATE);
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void writeLine(String fiel, String line) {
		line += "\n";
//		System.out.println("adding: "+ fiel + " " + line);
		try {
			FileOutputStream fos = pContext.openFileOutput(fiel, Context.MODE_APPEND);
			fos.write(line.getBytes());
			fos.flush();
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] readLinesInternal(String fiel) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
															   pContext.openFileInput(fiel)));
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		return lines.toArray(new String[lines.size()]);
	}
	public boolean fileIsEmpty() {
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
																   pContext.openFileInput("polys.csv")));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		}
		catch (IOException e) {}
		if (lines.size() > 0) return true;
		else return false;
	}

//	polygon lineToPoly(String s) {
//		String [] ln = s.split(",");
//		glVector [] ptz = new glVector[(int)((ln.length - 4) * 0.5)];
//		for (int i = 0; i < ptz.length;i++) ptz[i] = new glVector(new Float(ln[4 + (i * 2)]), new Float(ln[5 + (i * 2)]));
//
//		polygon p = new polygon(Integer.parseInt(ln[0]), Integer.parseInt(ln[1]), Integer.parseInt(ln[2]), Integer.parseInt(ln[3]), ptz);
//		return p;
//	}
	public float [] readCurrent() {
		float [] output;
		String [] emapIn = new String[1];
		try {
			emapIn = readLinesInternal("current.csv");
		}
		catch (IOException e) {
			System.out.println("File read Error");
		}
		String [] ln = emapIn[0].split(",");
		output = new float[ln.length];
		for (int i = 0; i < ln.length; i++) {
			output[i] = new Float(ln[i]);
		}
		return output;

	}
	public float [] readGlobal() {
		float [] output;
		String [] emapIn = new String[1];
		try {
			emapIn = readLinesInternal("global.csv");
		}
		catch (IOException e) {
			System.out.println("File read Error");
		}
		String [] ln = emapIn[0].split(",");
		output = new float[ln.length];
		for (int i = 0; i < ln.length; i++) {
			output[i] = new Float(ln[i]);
		}
		return output;

	}
	public glVector [] readScaff() {
		glVector [] output;
		String [] emapIn = new String[1];
		try {
			emapIn = readLinesInternal("scaff.csv");
		}
		catch (IOException e) {
			System.out.println("File read Error");
		}
		output = new glVector[emapIn.length];
		for (int i = 0; i < emapIn.length; i++) {
			String [] ln = emapIn[i].split(",");
			output[i] = new glVector(new Float(ln[0]), new Float(ln[1]), new Float(ln[2]));
		}
		return output;

	}
	public polygon [] readData() {
		polygon [] polys;
//  PVector [] ptz;
		String [] emapIn = new String[1];
//		float [][] emapItems;
		try {

			emapIn = readLinesInternal("polys.csv");
		}
		catch (IOException e) {
			System.out.println("File read Error");
		}

		polys = new polygon[emapIn.length];
		for (int i = 0; i < emapIn.length; i++) {
//			polys[i] = lineToPoly(emapIn[i]);
		}
		return polys;
	}
//	void storeSolution(int [][] inf, glVector [] hull, glVector [] linn) {
	void storeSolution(glVector [] inf, glVector [] linn) {
		String input = "";
//		for (int i = 0; i < hull.length; i++) {
//			input = hull[i].x + "," + hull[i].y + "," + hull[i].z;
//			writeLine("scaff.csv", input);
//		}
		for (int i = 0; i < linn.length; i++) {
			input = inf[i].x + "," + inf[i].y + "," + inf[i].z + "\n" + linn[i].x + "," + linn[i].y + "," + linn[i].z;
			writeLine("scaff.csv", input);
		}
	}
	void appendPoly(int type, int yaer, int session, int exMode, ArrayList<glVector> pointz) {
		String input = "";
		try { 
//			file = new FileWriter(params.csvpath, true); //bool tells to append
			input += type + "," + yaer + "," + session + "," + exMode;
			for (int i = 0; i < pointz.size(); i++) {
				glVector v = pointz.get(i);
				input += "," + v.x + "," + v.y; //(string, start char, end char)
			}
			input += "\n";
			writeLine("polys.csv", input);
//			file.close();
		} 
		catch (Exception e) {
			System.out.println("Error: Can't open file!");
		}
	}
	void appendPoly(polygon p) {
		String input = "";
		try {  
//			file = new FileWriter(params.csvpath, true); //bool tells to append
			input += p.t + "," + p.yr + "," + p.sess + "," + p.exMode;
			for (int i = 0; i < p.ptz.length; i++) {
				glVector v = p.ptz[i];
				input += "," + v.x + "," + v.y; //(string, start char, end char)
			}
//			input += "\n";

//			System.out.println(input);
			writeLine("polys.csv", input);
//			file.close();
		} 
		catch (Exception e) {
			System.out.println("Error: Can't open file!");
		}
	}

	// --------- EXHIBITION PURPOSES ONLY ------------



//	public String[] readLinesRaw() throws IOException {
//		InputStream is = this.getResources().openRawResource(R.raw.polys);
//
//		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
//														   (is));
//		List<String> lines = new ArrayList<String>();
//		String line = null;
//		while ((line = bufferedReader.readLine()) != null) {
//			lines.add(line);
//		}
//		bufferedReader.close();
//		return lines.toArray(new String[lines.size()]);
//	}

//	public polygon [] readRawData() {
//		polygon [] polys;
////  PVector [] ptz;
//		String [] emapIn = new String[1];
////		float [][] emapItems;
//		try {
//
//			emapIn = readLinesRaw();
//		}
//		catch (IOException e) {
//			System.out.println("File read Error");
//		}
//
//		polys = new polygon[emapIn.length];
//		for (int i = 0; i < emapIn.length; i++) {
//			polys[i] = lineToPoly(emapIn[i]);
//		}
//		return polys;
//	}	
	public void savePNG(int x, int y, int w, int h, String name) {
		Bitmap bmp = SavePixels(x, y, w, h);
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/anim");
		File f = new File(myDir,name);
		if(f.exists()) f.delete();
//		System.out.println("IN!!!");
		try {
			FileOutputStream fos = new FileOutputStream(f);
//			FileOutputStream fos = myContext.openFileOutput(f,Context.MODE_WORLD_WRITEABLE);
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			try {
				fos.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Bitmap SavePixels1(int x, int y, int w, int h) {
		int b [] = new int[w * h];
		int bt[] = new int[w * h];
		IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		GLES20.glReadPixels(x, y, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);

		for (int i = 0; i < h; i ++) {
			for (int j = 0; j < w; j++) {
				int pix = b[i * w + j];
				int pb = (pix >> 16) & 0xff;
				int pr = (pix << 16) & 0x00ff0000;
				int pix1 = (pix & 0xff00ff00) | pr | pb;
				bt[(h - i - 1) * w + j] = pix1;
			}
		}
		Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
//		Bitmap sb = Bitmap.create
		return sb;
	}
	public static Bitmap SavePixels(int x, int y, int w, int h) {
		int b [] = new int[w * h];
		IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		GLES20.glReadPixels(x, y, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
		Bitmap glBitmap = Bitmap.createBitmap(b, w, h,
											  Bitmap.Config.ARGB_8888);
		ib = null;
		b = null;
		final float [] cmVals = {0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0};
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(cmVals)));
		Bitmap bitmap = Bitmap.createBitmap(w, h,
											Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(glBitmap,0,0,paint);
		glBitmap = null;
//		android.graphics.Matrix matrix = new android.graphics.Matrix();
//		matrix.preScale(1.0f,-1.0f);
//		Bitmap p = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),
//									   bitmap.getHeight(),matrix,true);
		return bitmap;
	}
}
