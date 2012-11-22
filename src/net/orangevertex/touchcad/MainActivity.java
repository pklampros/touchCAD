package net.orangevertex.touchcad;

import android.app.*;
import android.content.*;
import android.opengl.*;
import android.os.*;
import android.util.*;
import android.view.*;
import java.io.*;
import java.util.*;
import net.orangevertex.glCore.*;
//import net.orangevertex.adaptivecanopy.obj.*;
import java.lang.reflect.*;
import android.graphics.*;
import java.nio.*;


public class MainActivity extends Activity {
	GLSurfaceView ourSurface;
	GLRenderer glR;
	public FileHandler fh;
	public TouchHandler th;
	public DialogHandler dh;
	Context myContext;
	public static int result = 0;
	int savedMode = -1;
	String events = "";
	boolean appendEvents = false;
	boolean recreateEvents = false;
	float [][] fakeEvents;
//	public static MainActivity self;
///	protected Dialog onCreateDialog(int d) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setMessage("Close the polygon?")
//			.setCancelable(false)
//			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
////					this.this.finish();
//				}
//			})
//			.setNegativeButton("No", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
//					dialog.cancel();
//				}
//			});
//		AlertDialog alert = builder.create();
//	}
	public static void kill() {
		new Runnable() {
			public void run() {
//				self.finish();
			}
		};
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		self = this;
		th = new TouchHandler();
		fh = new FileHandler(this);
		dh = new DialogHandler(this,fh);
		
		super.onCreate(savedInstanceState);
		myContext = this;
		ourSurface = new GLSurfaceView(this);
		glR = new GLRenderer(this,fh,dh);
//		gaR = new GARenderer();
		ourSurface = new theGLSurfaceView(this);
		setContentView(ourSurface);

		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		th.topOffset = dm.heightPixels - 800;	
		if (!appendEvents && recreateEvents) {
			buildEventSuccession();
		}
	}
	class theGLSurfaceView extends GLSurfaceView {

		public theGLSurfaceView(Context context) {
			super(context);
			// Create an Opengl ES 2.0 context
			setEGLContextClientVersion(2);
			setRenderer(glR);
		}
	}
	public void changeContent() {
		setContentView(ourSurface);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		th.topOffset = dm.heightPixels - ourSurface.getHeight();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		splashSurface.onPause();
		ourSurface.onPause();
//		clearFile("mode.txt");
//		writeLine("mode.text", "" + glR.mode);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		splashSurface.onResume();
		ourSurface.onResume();

		//	glR.gui.generate(this,ourSurface.getWidth(),ourSurface.getHeight());
	}
//	final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
//		public void onLongPress(MotionEvent e) {
//			Log.e("","Longpress detected");
//		}
//		public void onTouchEvent(MotionEvent e) {
//			
//		}
//	});
	void appendEvent(String s) {
		events += glR.frameCount + "," + s;
		if (events.length() > 500) {
			fh.writeLine("events.csv", events);
			events = "";
		} else events += "\n";
	}
	void buildEventSuccession() {
		String [] emapIn = new String[1];
		try {
			emapIn = fh.readLinesInternal("events.csv");
		}
		catch (IOException e) {
			System.out.println("File read Error");
		}
		fakeEvents = new float[emapIn.length][6];
		for (int i = 0; i < emapIn.length; i++) {
			String [] ln = emapIn[i].split(",");
			for (int j = 0; j < ln.length; j++) {
				fakeEvents[i][j] = new Float(ln[j]);
			}
		}
		glR.endFrame = (int)(fakeEvents[fakeEvents.length-2][0]);

//		return output;
	}
	void triggerFakeEvent(int frameCount) {
		for (int i = 0; i < fakeEvents.length; i++) {
			if ((int)(fakeEvents[i][0]) == frameCount) {
				switch ((int)(fakeEvents[i][1])) {
					case 0:
						simulateEventDown(fakeEvents[i][2], fakeEvents[i][3]);
						break;
					case 1:
						simulateEventMove(1, fakeEvents[i][2], fakeEvents[i][3], 0f, 0f);
						break;
					case 2:
						simulateEventMove(2, fakeEvents[i][2], fakeEvents[i][3],
										  fakeEvents[i][4], fakeEvents[i][5]);
						break;
					case 3:
						simulateEventUp(fakeEvents[i][2], fakeEvents[i][3]);
						break;
					case 4:
						simulateP1EventUp(fakeEvents[i][2], fakeEvents[i][3]);
						break;
					case 5:
						simulateP2EventUp(fakeEvents[i][2], fakeEvents[i][3]);
						break;
				}
				break;
			}
		}
	}
	public void simulateEventDown(float x, float y) {
		MotionEvent e= MotionEvent.obtain(SystemClock.uptimeMillis(),
										  SystemClock.uptimeMillis(),
										  MotionEvent.ACTION_DOWN,
										  x, y, 0);
		onTouchEvent(e);
	}
	public void simulateEventUp(float x, float y) {
		MotionEvent e= MotionEvent.obtain(SystemClock.uptimeMillis(),
										  SystemClock.uptimeMillis(),
										  MotionEvent.ACTION_UP,
										  x, y, 0);
		onTouchEvent(e);
	}
	public void simulateP1EventUp(float x, float y) {
		MotionEvent e= MotionEvent.obtain(SystemClock.uptimeMillis(),
										  SystemClock.uptimeMillis(),
										  MotionEvent.ACTION_POINTER_2_UP,
										  x, y, 0);
		onTouchEvent(e);
	}
	public void simulateP2EventUp(float x, float y) {
		MotionEvent e= MotionEvent.obtain(SystemClock.uptimeMillis(),
										  SystemClock.uptimeMillis(),
										  MotionEvent.ACTION_POINTER_2_UP,
										  x, y, 0);
		onTouchEvent(e);
	}
	public void simulateEventMove(int pointerCount, float x0, float y0, float x1, float y1) {
		MotionEvent.PointerProperties [] pP = new MotionEvent.PointerProperties[pointerCount];
		MotionEvent.PointerProperties pp0 = new MotionEvent.PointerProperties();
		pp0.id = 0;
		pp0.toolType = MotionEvent.TOOL_TYPE_FINGER;
		pP[0] = pp0;
		if (pointerCount > 1) {
			MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
			pp1.id = 1;
			pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
			pP[1] = pp1;
		}
		MotionEvent.PointerCoords [] pC = new MotionEvent.PointerCoords[pointerCount];
		MotionEvent.PointerCoords pc0 = new MotionEvent.PointerCoords();
		pc0.x = x0;
		pc0.y = y0;
		pc0.pressure = 1;
		pc0.size = 1;
		pC[0] = pc0;
		if (pointerCount > 1) {
			MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
			pc1.x = x1;
			pc1.y = y1;
			pc1.pressure = 1;
			pc1.size = 1;
			pC[1] = pc1;
		}
		MotionEvent e= MotionEvent.obtain(SystemClock.uptimeMillis(),
										  SystemClock.uptimeMillis(),
										  MotionEvent.ACTION_MOVE,
										  pointerCount, pP, pC,
										  0, 0, 1, 1, 0, 0, 0, 0);
		onTouchEvent(e);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return th.handleTouch(e);
	}
}
