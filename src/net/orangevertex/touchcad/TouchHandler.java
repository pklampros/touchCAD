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

import android.opengl.GLES20;
import android.util.Log;
import android.view.*;
import android.os.*;

public class TouchHandler {
	int prevPointerCount;
	boolean pointer1up = false;
	boolean pointer2up = false;
	float mPrevious1X;
	float mPrevious1Y;
	float mPrevious2X;
	float mPrevious2Y;
	float topOffset;
	int surfaceWidth,surfaceHeight;
	GLRenderer glR;
	
	TouchHandler(GLRenderer renderer,int width,int height) {
		glR = renderer;
		surfaceWidth = width;
		surfaceHeight = height;
		
	}
	final Handler handler = new Handler();
	Runnable mLongPressed = new Runnable() {
		public void run() {
			glR.longTouchInterpret(mPrevious1X, mPrevious1Y);
			Log.i("", "Long Press");
		}
	};
	public boolean handleTouch(MotionEvent e) {

		// MotionEvent reports input details from the touch screen
		// and other input controls. In this case, you are only
		// interested in events where the touch position changed.
		float c1x = e.getX();
		float c1y = e.getY();
		float c2x = e.getX();
		float c2y = e.getY();

		final int pointerCount = e.getPointerCount();
		if (pointerCount > 1) {

			c1x = e.getX(0);
			c1y = e.getY(0);
			c2x = e.getX(1);
			c2y = e.getY(1);
			glR.triggerTouchDisplay(new float [] {c1x,c1y,c2x,c2y});
		} else {
			glR.triggerTouchDisplay(new float [] {c1x,c1y});
		}
//			else {
////			s1x = e.getHistoricalX(0, historySize-1);
////			s1y = e.getHistoricalY(0, historySize-1);
//		}
//		for (int h = 0; h < historySize; h++)
//		{
//			for (int p = 0; p < pointerCount; p++)
//			{
//				System.out.printf("  pointer %d: (%f,%f)",
//								  ev.getPointerId(p), ev, ev.getHistoricalY(p, h));
//			}
//		}
		switch (e.getAction()) {
			case MotionEvent.ACTION_POINTER_1_UP:
				System.out.println("pointer 1: " + pointer1up);
				System.out.println("wasDouble: " + glR.gui.wasDouble);
				System.out.println("pointer 2: " + pointer2up); 
				if(glR.gui.wasDouble && pointer2up) glR.gui.wasDouble = false;
				else 
//				if (appendEvents) appendEvent("4");
				pointer1up = true;

				break;
			case MotionEvent.ACTION_POINTER_2_UP: 
				System.out.println("pointer 2: " + pointer2up);
				System.out.println("wasDouble: " + glR.gui.wasDouble);
				System.out.println("pointer 1: " + pointer1up);
				if(glR.gui.wasDouble && pointer1up) glR.gui.wasDouble = false;
				else
//				if (appendEvents) appendEvent("5");
				pointer2up = true;
				break;
			case MotionEvent.ACTION_UP:
				glR.triggerTouch = false;
//				if (appendEvents) appendEvent("3," + e.getX() + "," + e.getY());
				handler.removeCallbacks(mLongPressed);
				//System.out.println("simple up of pointer :" + e.getPointerId(0));
				if (e.getPointerId(0) == 0 && glR.gui.wasDouble && pointer2up) {
					mPrevious1X = 0;
					mPrevious1Y = 0;
					mPrevious2X = 0;
					mPrevious2Y = 0;
					glR.gui.wasDouble = false;
				}
				if (e.getPointerId(0) == 1 && glR.gui.wasDouble && pointer1up) {
					mPrevious1X = 0;
					mPrevious1Y = 0;
					mPrevious2X = 0;
					mPrevious2Y = 0;
					glR.gui.wasDouble = false;
				}
				if (!glR.gui.wasDouble && !glR.gui.wasMoving) {
					System.out.println("release");
					mPrevious1X = 0;
					mPrevious1Y = 0;
					glR.gui.setPointers(3, surfaceHeight,
										surfaceWidth,
										pointerCount,
										mPrevious1X, mPrevious1Y, c1x, c1y,
										mPrevious2X, mPrevious2Y,
										c2x, c2y);										
				}
				glR.gui.hoverButton = -1;
				glR.gui.wasMoving = false;
				glR.infoScreen = -1;

				break;
			case MotionEvent.ACTION_CANCEL:
			glR.gui.wasDouble = false;
				break;
			case MotionEvent.ACTION_DOWN:
				int longPressTime = 350;
				if (glR.gui.setPointerDown(c1x, c1y)) longPressTime = 1200;

				handler.postDelayed(mLongPressed, longPressTime);
				if (mPrevious1X != 0 && mPrevious1Y != 0) {
					//System.out.println("p1: " + mPrevious1X + " " + mPrevious1Y);
					//System.out.println("c1: " + c1x + " " + c1y);
					glR.gui.setPointers(0, surfaceHeight,
									surfaceWidth,
									pointerCount,
									mPrevious1X, mPrevious1Y, c1x, c1y,
									mPrevious2X, mPrevious2Y,
									c2x, c2y);
				}
//									if(pointerCount > 1) {
//										
//									}
				break;
			case MotionEvent.ACTION_MOVE:

//				System.out.println(e.toString());
				System.out.println(e.getX() + " " + e.getY());
				System.out.println(surfaceWidth + " " + surfaceHeight);
				handler.removeCallbacks(mLongPressed);
				if (glR.toMove == -1) {
					if (mPrevious1X != 0 && mPrevious1Y != 0) {
						glR.gui.wasMoving = true;
						glR.gui.setPointers(1, surfaceHeight,
											surfaceWidth,
											pointerCount,
											mPrevious1X, mPrevious1Y, c1x, c1y,
											mPrevious2X, mPrevious2Y,
											c2x, c2y);
					}
				} else {
					glR.dragInterpret(c1x, c1y);
				}

		}

		mPrevious1X = c1x;
		mPrevious1Y = c1y;
		mPrevious2X = c2x;
		mPrevious2Y = c2y;
		prevPointerCount = pointerCount;
		return true;
	}
}
