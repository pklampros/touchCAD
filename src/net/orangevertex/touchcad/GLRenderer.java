package net.orangevertex.touchcad;

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
import javax.crypto.interfaces.*;


public class GLRenderer implements GLSurfaceView.Renderer {
	private static final String TAG = "GLRenderer";

	private final float [] mMVPMatrix = new float [16];
	private final float [] tempTranslate = new float [16];
	private final float [] tempScale = new float [16];
	private final float [] tempTransMatrixX = new float[16];
	private final float [] tempTransMatrixY = new float[16];
	private final float [] armTranslator = new float [16];
	private final float [] hoverTrans = new float [16];
	private final float [] touchTrans = new float [16];
	public int endFrame = 12000000;

	private float [] touchEvents;
	private boolean debugger = false;

	glVector [] shadows;

	public int frameCount;
//	public ovv v;
//	public params pp;
	private int wWidth, wHeight;

	// contains 1/width, 1/height (division is costly)
	private float iWidth, iHeight; 

	private glObjects.axizLines axiz;
//	private glObjects.circle c,cB,cS;
//	private glObjects.multiCircle mC;
	private glObjects.mobiLine mL;
	private glObjects.multiLine tableLines,ui3d;

	glUI gui = new glUI(this);

	float ratio;
	float iratio;

//	public Feeder f;
	boolean allowDraw = false;
	boolean is3D = true;

//	public glObjects.multiLine history;
//	public glObjects.multiLine current;
//	public textureQuad tC;
//	public int yr = 1989;

//	scaff.scaffolder scaff;
	Context parent;

	int mode = 2; // 0 - view / 1 - add / 2 - edit

	int newPolyType = 0;
	ArrayList<glVector> newPoly = new ArrayList<glVector>();


	public static int currYear = 2012;
	public static int currSession = 0;

	float gridX = 5;
	float gridY = 5;
	int gridSecDiv = 5;
	int gridXNum = 151;
	int gridYNum = 151;
	int gridXOffset = 0;
	int gridYOffset = 0;
	private double baseLocationLat = 34.756536;
	private double baseLocationLon = 32.405125;

// ---------------------------------------------------- POLYGONS

//	polygon nPoly, sideScreen, fullScreen, overlayScreen,toolTipScreen,toolTipBorder;
//	polygon [] polys, polysPrev, polysPostEx, polysPreEx;
//	public static polygon [] poly = new polygon[1];
//	ArrayList<polygon> newPolyz = new ArrayList<polygon>();

// ---------------------------------------------------- BOOLEANS

	public static int triggerAction = -1;
	public static int infoScreen = -1;
	public static boolean polyIsOpen = false,
	passToGAPreEx = false,
	passToGAPostEx = false,
	passToPostEx = false;
//	stopCalc = false;
	boolean polyChanged = false;
//	boolean generate = false;
//	boolean storeScaff = false;
	public boolean editMode = false;
	public boolean removeMode = false;
	public boolean selectMode = false;
	private boolean displayTable = false;
//	private boolean newSession = false;
	public boolean popLive = true;
	boolean toEvolve;
	boolean newPopPreEx = false;
	public static int exMode = 0; // 0 = pre excavation mode // 1 = post ex


// ---------------------------------------------------- TEXTURE QUADS

//	textureQuad hover,hoverSmall,hoverLight,tubeText,clampText,plan,locInfo,
//	currentBest,toolTip,gridInfo,yearInfo;
//	textureQuad [] overMessage;

	public int selectedI = -1;
	public int toMove = -1;
	private int overMessageNum = 0;
	private float mPos [] = {0,0};
	private float overlayPercent = 0.75f;
	public boolean triggerTouch = false;
	public boolean landscape = true;
	public float largeDim;
	public float smallDim;
	
	FileHandler fh;
	DialogHandler dh;
	
	public GLRenderer(Context p, FileHandler fileHandler, DialogHandler dialogHandler) {
		parent = p;
		fh = fileHandler;
		dh = dialogHandler;
//		f = new Feeder(p);
	}
	public void drawTouch() {
		float [] mvp = gui.cam.getMVP();
		Matrix.setIdentityM(touchTrans, 0);
//		for (int i = 0; i < touchEvents.length * 0.5; i++) {
		glBase.translate(touchTrans,
							-gui.screenXdim + touchEvents[0] * 2 / smallDim,
							-gui.screenYdim + touchEvents[1] * 2 / smallDim,
							0);
		Matrix.multiplyMM(mvp, 0, touchTrans, 0, mvp, 0);
//		tC.draw(mvp);
		if (touchEvents.length > 2) {
			mvp = gui.cam.getMVP();
			glBase.translate(touchTrans,
								(touchEvents[2] - touchEvents[0]) * 2 / smallDim,
								(touchEvents[3] - touchEvents[1]) * 2 / smallDim,
								0);
			Matrix.multiplyMM(mvp, 0, touchTrans, 0, mvp, 0);
//			tC.draw(mvp);
		}
//		}
	}
	public void writeGlobalOptions() {
		String s = ""
			+ currYear + ","
			+ currSession + ","
			+ gridX + ","
			+ gridY + ","
			+ gridXNum + ","
			+ gridYNum + ","
			+ gridXOffset + ","
			+ gridYOffset + ","
			+ gridSecDiv + ","
			+ baseLocationLat + ","
			+ baseLocationLon;
		fh.clearFile("global.csv");
		fh.writeLine("global.csv", s);
	}
	public void loadGlobalOptions() {
		float [] f = fh.readGlobal();

		currYear = (int)f[0];
		currSession = (int)f[1];
		gridX = f[2];
		gridY = f[3];
		gridXNum = (int)f[4];
		gridYNum = (int)f[5];
		gridXOffset = (int)f[6];
		gridYOffset = (int)f[7];
		gridSecDiv = (int)f[8];
		baseLocationLat = f[9];
		baseLocationLon = f[10];
	}
	public void writeCurrentOptions() {
		String s = ""
			+ mode + ","
			+ exMode;
		fh.clearFile("current.csv");
		fh.writeLine("current.csv", s);
	}
	public void loadCurrentOptions() {
		float [] f = fh.readCurrent();
		mode = (int)f[0];
		exMode = (int)f[1];
	} 
	public void onSurfaceCreated(GL10 unused, javax.microedition.khronos.egl.EGLConfig eglConfig) {
//		v = new ovv();
		GLES20.glClearColor(1f, 1f, 1f, 1f);
//		GLES20.glClearDepthf(1f);
		axiz = new glObjects.axizLines(0.1f);
//		c = new glObjects.circle(0.01f);
//		cS = new glObjects.circle(0.1f);
//		cB = new glObjects.circle(0.2f);
//		cB.color = new float [] {1f,0f,0f,1f};
//		cS.color = new float [] {1f,0.5f,0f,1f};
//		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);



		glVector [] ppt = new glVector[20];
		for (int i = 0; i < 20; i++) {
			ppt[i] = new glVector(Math.random() * 0.05, Math.random() * 0.05, 0f);
		}
		glVector [] polyPtz;
//		polyPtz = delaunay.findConvexHull(ppt);
//		poly[0] = new polygon(1, 2000, polyPtz);

//		params.aMinX = poly[0].pMinX;
//		params.aMaxX = poly[0].pMaxX;
//		params.aMinY = poly[0].pMinY;
//		params.aMaxY = poly[0].pMaxY;

//		shadows = sun.getShadows(2012, 32.19f, 23.01f);

//		mC = new glObjects.multiCircle(shadows, 0.05f);
		mL = new glObjects.mobiLine();
//		polysPostEx = fh.readData();
// ---------------------------------------------------- FIX
//		setupPopulation(poly);
//		p = new population(popNum);
// ---------------------------------------------------- FIX
		Matrix.setIdentityM(tempScale, 0);
		Matrix.setIdentityM(tempTranslate, 0);
		Matrix.setIdentityM(tempTransMatrixX, 0);
		Matrix.setIdentityM(armTranslator, 0);
		Matrix.setIdentityM(hoverTrans, 0);
		glBase.translate(tempTransMatrixX, 5f, 0, 0);
		glBase.translate(armTranslator, 0f, 0, 0.3f);
		Matrix.scaleM(tempScale, 0, 0.1f, 0.1f, 0.1f);
		glBase.translate(tempTranslate, -0.6f, -2f, 0);


//		tC = new textureQuad();
//		clampText = new textureQuad();
//		tubeText = new textureQuad();
//		currentBest = new textureQuad();
//		yearInfo = new textureQuad();
//		gridInfo = new textureQuad();
//		locInfo = new textureQuad();
////		overMessage = new textureQuad[5];
//		hover = new textureQuad();
//		hoverSmall = new textureQuad();
//		hoverLight = new textureQuad();
////		tooltip = new textureQuad[];
//
//		plan = new textureQuad();
//		setYear.draw(mvp);
//		Matrix.multiplyMM(mvp,0,tempTransMatrixX,0,mvp,0);
//		setGrid.draw(mvp);
//		Matrix.multiplyMM(mvp,0,tempTransMatrixX,0,mvp,0);
//		setLocation
	}
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		int bm = 0;
		ratio = (float) width / (height + bm);
		iratio = (float) (height + bm)/ width;
		largeDim = width;
		smallDim = (height + bm);
		landscape = true;
		if(height > width) {
			landscape = false;
			ratio = (float) (height +bm)/ width;
			iratio = (float) width / (height + bm);
			largeDim = (height + bm);
			smallDim = width;
			}
		grid.setup(gridX, gridY, gridXNum, gridYNum, gridXOffset, gridYOffset, gridSecDiv);

		if (wWidth != width || wHeight != height) {
//			tC.generate(parent, 64.0f / width, R.drawable.touchpoint);
//			hover.generate(parent, 128f / width, R.drawable.hover);
//			hoverSmall.generate(parent, 72f / width, R.drawable.hover);
//			hoverLight.generate(parent, 72f / width, R.drawable.hoverlight);
			gui.generate(parent, width, height + bm,landscape);
			gui.cam.setupOrtho();
			glVector [] screenPoints = new glVector [] {
				new glVector(-ratio, -1),
				new glVector(-ratio, 1),
				new glVector(ratio, 1),
				new glVector(ratio, -1)
			};
//			fullScreen = new polygon(screenPoints);
//			fullScreen.color = new float [] {1f,1f,1f,0.7f};
//			screenPoints = new glVector [] {
//				new glVector(-ratio * overlayPercent, -overlayPercent),
//				new glVector(-ratio * overlayPercent, overlayPercent),
//				new glVector(ratio * overlayPercent, overlayPercent),
//				new glVector(ratio * overlayPercent, -overlayPercent)
//			};
//			overlayScreen = new polygon(screenPoints);
//			overlayScreen.color = new float [] {1f,1f,1f,0.8f};
//			float toolTipSize = 512.0f / height;
//			screenPoints = new glVector [] {
//				new glVector(-toolTipSize, toolTipSize),
//				new glVector(toolTipSize, toolTipSize),
//				new glVector(toolTipSize, -toolTipSize),
//				new glVector(-toolTipSize, -toolTipSize)
//			};
//			toolTipScreen = new polygon(screenPoints);
//			toolTipScreen.color = new float [] {1f,1f,1f,0.9f};
//			toolTipBorder = new polygon(screenPoints);
//			toolTipBorder.color = new float [] {0f,0f,0f,1.0f};
//			toolTipBorder.border = true;
//			screenPoints = new glVector [] {
//				new glVector(-ratio, -1),
//				new glVector(-ratio, 1),
//				new glVector(-4 * ratio / 5, 1),
//				new glVector(-4 * ratio / 5, -1)
//			};
//
//			sideScreen = new polygon(screenPoints);
//			sideScreen.color = new float [] {1f,1f,1f,0.7f};
			Matrix.setIdentityM(tempTransMatrixY, 0);
//			glBase.translate(tempTransMatrixY, 0f, 2f / (popNum - 1) * 10, 0);
			glVector [] vecs = new glVector[8 * 2];
			vecs[0] = new glVector(-ratio * overlayPercent, -overlayPercent);
			vecs[1] = new glVector(ratio * overlayPercent, -overlayPercent);
			vecs[2] = new glVector(-ratio * overlayPercent, +overlayPercent);
			vecs[3] = new glVector(-ratio * overlayPercent, -overlayPercent);
			vecs[4] = new glVector(-ratio * overlayPercent, +overlayPercent);
			vecs[5] = new glVector(ratio * overlayPercent, +overlayPercent);
			vecs[6] = new glVector(ratio * overlayPercent, +overlayPercent);
			vecs[7] = new glVector(ratio * overlayPercent, -overlayPercent);
			vecs[8] = new glVector(-ratio * overlayPercent, -overlayPercent + 0.1f);
			vecs[9] = new glVector(ratio * overlayPercent, -overlayPercent + 0.1f);
			vecs[10] = new glVector(-ratio * overlayPercent + 0.1f, +overlayPercent);
			vecs[11] = new glVector(-ratio * overlayPercent + 0.1f, -overlayPercent);
			vecs[12] = new glVector(-ratio * overlayPercent, -overlayPercent + 0.1f);
			vecs[13] = new glVector(ratio * overlayPercent, -overlayPercent + 0.1f);
			vecs[14] = new glVector(-ratio * overlayPercent + 0.1f, +overlayPercent);
			vecs[15] = new glVector(-ratio * overlayPercent + 0.1f, -overlayPercent);
			tableLines = new glObjects.multiLine(vecs);
			tableLines.color = new float [] {0f,0f,0f,1f};
			float [] uil = new float [] {
				-0.4f * ratio,-1 + 0.87f * 2f,0,
				0.4f * ratio,-1 + 0.87f * 2f,0,
				(-1 + 0.85f * 2f) * ratio,-0.4f,0,
				(-1 + 0.85f * 2f) * ratio, 0.4f,0,
			};
			ui3d = new glObjects.multiLine(uil);
			ui3d.color = new float [] {0f,0f,0f,1f};

		}
		wWidth = width;
		wHeight = height;
		iWidth = 1.0f / width;
		iHeight = 1.0f / height;
		setOverMessage();

//		mL.constructStatic(new float [] {-1,-1,0,-1,1,0});
		generateGridInfo();
		generateYearInfo();
		generateLocationInfo();
		generateLocationInfo();

		buildPlan();
	}
	public void triggerTouchDisplay(float [] pC) {
		touchEvents = pC;
		triggerTouch = true;
	}
	public void onDrawFrame(GL10 unused) {
		if (debugger) System.out.println("--");
		
		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
//		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);

//		float [] camCent = gui.cam.getCamCent();




		if (debugger) System.out.println("1"); // ------------ DEBUG 1




		grid.draw(mMVPMatrix);
		switch (mode) {
			case 2:
//				plan.draw(mMVPMatrix);
//				if (newPolyz.size() > 0) { 
//					for (int i = 0; i < newPolyz.size(); i++) {
//						newPolyz.get(i).draw(mMVPMatrix);
//					}
//				}
//				if (newPoly.size() > 2) {
//					if (polyChanged) {
//						if (newPoly.size() > 2) {
////							mL.constructStatic(new float [] {newPoly.get(newPoly.size() - 2).x,
////													newPoly.get(newPoly.size() - 2).y});
//						}
//						if (newPoly.size() > 2) {
//							nPoly = new polygon(newPolyType, currYear, currSession, exMode, newPoly);
//						}
//					}
//					nPoly.draw(mMVPMatrix);
//				}
				for (int i = 0 ; i < newPoly.size(); i++) {
//					c.draw(mMVPMatrix, newPoly.get(i).x, newPoly.get(i).y);
				}
				break;

		}




		if (debugger) System.out.println("2"); // ------------ DEBUG 2



		if (mL.valid && toMove != -1) {
//			mL.build();
//			mL.draw(mMVPMatrix);
		}

// ---------------------------------------------------- RESETTER

		gui.cam.matrixReset(mMVPMatrix);

// ---------------------------------------------------- GUI ELEMENTS 1

		switch (mode) {
			case 0:
//				gui.displayStartScreen(mMVPMatrix);
				break;
			case 1:
//				gui.displaySiteSetup(mMVPMatrix);
//				gridInfo.draw(mMVPMatrix);
//				yearInfo.draw(mMVPMatrix);
//				locInfo.draw(mMVPMatrix);
//				gui.drawBackward(mMVPMatrix);
				break;
			case 2:
//				gui.drawForBack(mMVPMatrix);
				//placeholder
//				break;
//				gui.drawHelpIcon(mMVPMatrix);
//				displayNotationMode(mMVPMatrix);
//				gui.drawForward(mMVPMatrix);
//				gui.drawBackward(mMVPMatrix);
//				if (newPoly.size() > 2 || (removeMode && selectedI > -1)) gui.displayFunc(mMVPMatrix);
//				if (newPoly.size() > 0) {
//					gui.displayUndo(mMVPMatrix);
//					gui.displayDone(mMVPMatrix);
//				}
//				if (editMode || removeMode) {
//					gui.displayDone(mMVPMatrix);
//				}
				//axiz.draw(mMVPMatrix);

				break;
		}
		if (selectMode && selectedI > -1) {
			overMessageNum = 5;
			int len = 0;
//			if (selectedI > -1) len = newPolyz.get(selectedI).ptz.length;
//			for (int i =0; i < len; i++) {
//				float x = -22;
//				float y = -22;
//				if (selectedI > -1) {
//					x = newPolyz.get(selectedI).ptz[i].x;
//					y = newPolyz.get(selectedI).ptz[i].y;
//				}
//				float [] nPv = gui.cam.getInverseTransformTop(x, y);
////				cS.draw(mMVPMatrix, nPv[0],
////						nPv[1]);
//			}
		}
		if (toMove != -1) {
//			cB.draw(mMVPMatrix, newPolyz.get(selectedI).ptz[toMove].x,
//					newPolyz.get(selectedI).ptz[toMove].y);
//			cB.draw(mMVPMatrix, mPos[0], mPos[1]);
		}
		if (displayTable && triggerAction == -1) {
			toEvolve = false;
//			drawTable(mMVPMatrix);
		}


		if (triggerTouch) {
			drawTouch();
		}
		if (debugger) System.out.println("3"); // ------------ DEBUG 3


// ---------------------------------------------------- RESETTER

		gui.cam.matrixReset(mMVPMatrix);

// ---------------------------------------------------- GUI ELEMENTS 2

//		if (gui.hoverButton != -1) {
//			float [] mvp = gui.cam.getMVP();
//			Matrix.setIdentityM(hoverTrans, 0);
//			glBase.translate(hoverTrans,
//								-ratio + gui.buttons[gui.hoverButton].centX * 2 * iHeight,
//								-1 + gui.buttons[gui.hoverButton].centY * 2 * iHeight,
//								0);
//			Matrix.multiplyMM(mvp, 0, hoverTrans, 0, mvp, 0);
////			if (mode < 2) hover.draw(mvp);
////			else hoverSmall.draw(mvp);
//		}
//		if (editMode || removeMode || polyIsOpen) {
//			float [] mvp = gui.cam.getMVP();
//			int butt = -1;
//			if (polyIsOpen) {
//				switch (newPolyType) {
//					case 1:butt = 9;
//						break;
//					case 2:butt = 11;
//						break;
//					case 3:butt = 12;
//						break;
//					case 4:butt = 10;
//						break;
//				}
//			}
//			if (removeMode) butt = 13;
//			if (editMode) butt = 14;
//			Matrix.setIdentityM(hoverTrans, 0);
//			glBase.translate(hoverTrans,
//								-ratio + gui.buttons[butt].centX * 2 * iHeight,
//								-1 + gui.buttons[butt].centY * 2 * iHeight,
//								0);
//			Matrix.multiplyMM(mvp, 0, hoverTrans, 0, mvp, 0);
//			hoverLight.draw(mvp);
//		}

//		if (mode > 0) overMessage[overMessageNum].draw(mMVPMatrix);

		if (debugger) System.out.println("4"); // ------------ DEBUG 4

//		if (infoScreen > -1) {
//			toolTipScreen.draw(mMVPMatrix);
//			toolTipBorder.draw(mMVPMatrix);
//			if (infoScreen > -1) gui.infos[infoScreen].draw(mMVPMatrix);
//
//		}




		if (debugger) System.out.println("5"); // ------------ DEBUG 5



// ---------------------------------------------------- CAMERA SETUP

		if (!is3D) gui.cam.top(mMVPMatrix);
		else {
			ui3d.draw(mMVPMatrix);
			gui.cam.call(mMVPMatrix);
		}

// ---------------------------------------------------- LOAD AT RUNTIME

		frameCount++;
		if (triggerAction != -1)
			switch (triggerAction) {
				case 0: // ------------- poly to close
					triggerAction = -1;
					closePoly();
					newPolyType = 0;
					polyIsOpen = false;
					break;
				case 1: // ------------- site to reset
					triggerAction = -1;
					siteReset();
					break;
				case 2: // ------------- discart current poly 
					triggerAction = -1;
					newPoly.clear();
					newPolyType = 0;
					polyIsOpen = false;
					break;
				case 3: // ------------- store scaffolding
					triggerAction = -1;
//					if (popLive && p.setup) p.store();
					break;
				case 5:
//					if (p == null) {
//						if (!p.setup) {
//						int polyNum = 0;
//						for (int i = 0; i < newPolyz.size(); i++) {
//							if (newPolyz.get(i).current && newPolyz.get(i).t == 1) {
//								polyNum++;
//							}
//						}
//						System.out.println("loller" + polyNum);
//						if (polyNum == 0) {} else {
//							newPopPreEx = true;
//						}	
						//setupPopulation(polys);
//						}
//					}
					triggerAction = -1;
					break;
				case 22:
					loadGlobalOptions();
					currSession++;
					buildCurrentSite();
					mode++;
					writeCurrentOptions();
				 	generateYearInfo();
					generateGridInfo();
					generateLocationInfo();


					triggerAction = -1;
					break;
				case 6: // ------------- build current site
					loadGlobalOptions();
					loadCurrentOptions();
					buildCurrentSite();
					triggerAction = -1;
					break;
				case 7:
					stopAndSave();
					triggerAction = -1;
					break;
				case 8: // ------------- rebuild polygon
//					if (selectedI > -1) newPolyz.get(selectedI).calcStuff();
					triggerAction = -1;
					break;
				case 9: // ------------- discart selected poly
					triggerAction = -1;
//					newPolyz.remove(selectedI);
					selectedI = -1;
					break;
				case 109: // ------------ regen scaff
					triggerAction = -1;
//					setupPopulation(polys);
					// storeScaff = false;
					break;
				case 110: // ------------ delete history
					triggerAction = -1;

					//	if (popLive)p.store();
					// storeScaff = false;
					break;
				case 12: 
					stopAndSave();
					MainActivity.kill();
					triggerAction = -1;
					break;
//				case 14:
//					triggerAction = -1;
//					break;
				case 15:
					triggerAction = -1;
					break;

			}
		if (debugger) System.out.println("--"); // ------------ DEBUG END
//		if(frameCount == 50) parent.simulateEventDown((long)(wWidth*0.5f),(long)(wHeight*0.5f));
		if (frameCount == endFrame) MainActivity.kill();
	}

// ---------------------------------------------------- END OF DRAW


	public boolean triggerEvent(int e) {
		boolean triggered = false;
		switch (e) {
			case 0:
				if (mode == 0) {
					if (fh.fileIsEmpty()) dh.popYesNoDialog(1, "Reset the site?");
					else mode++;
					triggered = true;
				}
				break;
			case 1:
				if (mode == 0) {

					if (fh.fileIsEmpty()) dh.popYesNoDialog(22, "Start new session?");
					else mode++;
					triggered = true;
				}
				break;
			case 2:
				if (mode == 0) {
					triggerAction = 6;
//					loadCurrentOptions();
//					if (mode == 3) toEvolve = false;

					triggered = true;
				}
				//System.out.println("Fetching pre ex polys");
//				if (f.exhibitionPolys != null) polys = f.fetchPolysPreEx();
				break;
			case 3:
				if (mode == 1) { 
					dh.popYesNoDialog(-1, "");
					triggered = true;
				}
				//System.out.println("Fetching post ex polys");
//				if (f.exhibitionPolys != null) polys = f.fetchPolysPostEx();
				break;
			case 4:
				if (mode == 1) {

					dh.popYesNoDialog(-1, "");
					triggered = true;
				}
//				toEvolve = !toEvolve;
				break;
			case 5:
				if (mode == 1) {
					dh.popYesNoDialog(-1, "");
					triggered = true;
				}
//				storeScaff = true;
				break;
			case 6: // pre ex mode
			case 7:
				if (mode == 1) {
					triggered = true;
					mode = 2;
					triggerAction = 14;
					overMessageNum = 1;
					writeGlobalOptions();
					writeCurrentOptions();
				}
				break;
			case 71111: // post ex mode
				if (mode == 1) {
					triggered = true;
					mode = 4;
					triggerAction = 15;
					overMessageNum = 2;
					writeGlobalOptions();
					writeCurrentOptions();
				}
				break;
			case 8:
				if (mode > 1) {
					gui.displayText = !gui.displayText;
					triggered = true;
				}
//				if (f.exhibitionPolys != null) newPopPreEx = true;
				break;
			case 9:
				if (mode == 2) {
					triggered = true;
					if (!removeMode) {
						if (selectedI == -1) {
							editMode = false;
							selectMode = false;
							polyIsOpen = true;
							newPolyType = 1;
							overMessageNum = 6;
						} else {
//							newPolyz.get(selectedI).newType(1);
						}
					}
				}
				if (mode == 3) {
					dh.popYesNoDialog(12, "Save before exit?");
					triggered = true;
				}
				//if (f.exhibitionPolys != null) newPopPreEx = true;
				break;
			case 10: // --- add  / pause
				if (mode == 2) {
					triggered = true;
					if (!removeMode) {
						if (selectedI == -1) {
							editMode = false;
							selectMode = false;
							polyIsOpen = true;
							newPolyType = 4;
							overMessageNum = 7;
						} else {
//							newPolyz.get(selectedI).newType(4);
						}
					}
				}

				if (mode == 3) {
					displayTable = !displayTable;
					triggerAction = 7;
					triggered = true;
				}
				//is3D = !is3D;
				break;
			case 11:
				if (mode == 2) {
					triggered = true;
					if (!removeMode) {
						if (selectedI == -1) {
							editMode = false;
							selectMode = false;
							polyIsOpen = true;
							newPolyType = 2;
							overMessageNum = 8;
						} else {
//							newPolyz.get(selectedI).newType(2);
						}
					}
				}

				if (mode == 3) {
					triggered = true;
					dh.popYesNoDialog(109, "Restart optimization?");
				}
				break;
			case 12: // add feature / regenerate button
				if (mode == 2) {
					triggered = true;
					if (!removeMode) {
						if (selectedI == -1) {
							editMode = false;
							selectMode = false;
							polyIsOpen = true;
							newPolyType = 3;
							overMessageNum = 9;
						} else {
//							newPolyz.get(selectedI).newType(3);
						}
					}
				}

				if (mode == 3) {
					triggered = true;
					dh.popYesNoDialog(110, "Clear data?");
				}
				break;
			case 13: // remove button // remove all button
				if (mode == 2) {
					triggered = true;
					if (polyIsOpen && newPoly.size() > 2) dh.popYesNoDialog(2, "Discart current polygon?");
					else {
						overMessageNum = 3;
						triggerAction = 2;
						newPolyType = 0;
						selectMode = true;
						removeMode = true;
						editMode = false;
					}
				}
				if (mode == 3) {
					is3D = !is3D;
					triggered = true;
				}
				break;
			case 14: //Edit mode button // go 3d
				if (mode == 2) {
					triggered = true;
					if (polyIsOpen && newPoly.size() > 2) dh.popYesNoDialog(0, "Close polygon?");
					else newPolyType = 0;
					selectMode = true;
					removeMode = false;
					editMode = true;
					overMessageNum = 4;
					triggerAction = 2;
				}
				if (mode == 3) {
					toEvolve = false;
					triggerAction = 7;
				}
				break;
			case 15: // go forward
				if (mode < 3) {
					triggered = true;
//					if (mode == 2 && newPolyz.size() < 1) dh.popYesNoDialog(101, "You didnt designate any areas for this session");
//					else {
//						mode++;
//						if (mode == 3) {
//							int polyNum = 0;
//							for (int i = 0; i < newPolyz.size(); i++) {
//								if (newPolyz.get(i).current && newPolyz.get(i).t == 1) {
//									polyNum++;
//								}
//							}
//							if (polyNum == 0) {} else {
//								newPopPreEx = true;
//							}
//						}
//						writeCurrentOptions();
//					}
				} else if (mode == 3) {
					triggered = true;
					triggerAction = 5;
					toEvolve = true;
				}
				break;
			case 19: // go back
				if (mode > 0) {
					if (mode == 3) {
						displayTable = false;
						is3D = false;
					}
					mode--;

					if (mode > 0) writeCurrentOptions();
					triggered = true;
				}
				break;
			case 16: // undo button
				if (mode == 2) {
					if (newPoly.size() > 1) newPoly.remove(newPoly.size() - 1);
					if (newPoly.size() == 1) {
						dh.popYesNoDialog(2, "Discart current polygon?");
					}
					triggered = true;
				}
				break;
			case 17: // enter button
				if (mode == 2) {

					if (newPoly.size() > 2) {
						dh.popYesNoDialog(0, "Close polygon?");
						triggered = true;
					}
					if (removeMode) {
						triggered = true;
						if (selectedI == -1) removeMode = false;
						else 
							dh.popYesNoDialog(9, "Discart selected polygon?");
					}
					if (editMode) {
						fh.clearFile("polys.csv");
//						for (int i = 0; i < newPolyz.size(); i++) {
//							fh.appendPoly(newPolyz.get(i));
//						}
					}
				}
				break;
			case 18: // done button
				if (mode == 2) { 
					triggered = true;
					if (newPoly.size() > 0) {
						dh.popYesNoDialog(2, "Discart current polygon?");
					}
					editMode = false;
					removeMode = false;
					selectMode = false;
					selectedI = -1;
					siteReWrite();
				}
				break;
		}

		return triggered;
	}
	public void drawOverMessageString() {
		switch (overMessageNum) {
			case 0:
				break;
			case 1:
				break;
		}
	}
	public void siteReWrite() {
		fh.clearFile("polys.csv");
//		for (int i = 0; i < newPolyz.size(); i++) {
//			fh.appendPoly(newPolyz.get(i));
//		}
	}
	public void siteReset() {
		currYear = 2012;
		currSession = 0;
		gridX = 5f;
		gridY = 5f;
		gridSecDiv = 5;
		baseLocationLat = 34.756536;
		baseLocationLon = 32.405125;
		generateYearInfo();
		writeCurrentOptions();
//		newPolyz.clear();
		fh.clearFile("polys.csv");
		fh.clearFile("scaff.csv");
		mode++;
	}
	public void setupSiteSetup() {
		Matrix.setIdentityM(tempTranslate, 0);
		glBase.translate(tempTranslate, -ratio * 0.5f, 0, 0);
		Matrix.setIdentityM(tempTransMatrixX, 0);
		glBase.translate(tempTransMatrixX, ratio * 0.5f, 0, 0);

	}
	public void displayNotationMode(float [] mvp) {
//		gui.notationModeMenu(mvp);
	}
	float [] translatePointToWorld(float x, float y) {
		float [] nPv = {((2 * x) / smallDim) - gui.screenXdim,
			-((2 * y) / smallDim) + gui.screenYdim,1f};
		return gui.cam.getMatrixTransformTop(nPv);
	}
	public void dragInterpret(float x, float y) {
		float [] nPv = translatePointToWorld(x, y);
		mPos[0] = -ratio + x * 2f * iHeight;
		mPos[1] = -1f + y * 2f * iHeight;
//		mL.constructMoving(nPv[0], nPv[1]);
	}
	public void longTouchInterpret(float x, float y) {
//		boolean b = gui.checkButtons(1, x, y);
		boolean b = false;
		if (!b) {
			float [] nPv = translatePointToWorld(x, y);
			if (selectedI > -1 && editMode) {
//				for (int i = 0; i < newPolyz.get(selectedI).ptz.length;i++) {
//					if (glVector.dist(newPolyz.get(selectedI).ptz[i], new glVector(nPv[0], nPv[1])) < 0.2) {
//						toMove = i;
//						mPos[0] = -ratio + x * 2f * iHeight;
//						mPos[1] = -1f + y * 2f * iHeight;
//						int im1 = i - 1;
//						int ip1 = i + 1;
//						if (ip1 > newPolyz.get(selectedI).ptz.length - 1) ip1 = 0;
//						if (im1 < 0) im1 = newPolyz.get(selectedI).ptz.length - 1;
////						mL.constructStatic(new float [] {
////											   newPolyz.get(selectedI).ptz[im1].x,
////											   newPolyz.get(selectedI).ptz[im1].y,
////											   0,
////											   newPolyz.get(selectedI).ptz[ip1].x,
////											   newPolyz.get(selectedI).ptz[ip1].y,
////											   0});
//						i = newPolyz.get(selectedI).ptz.length;
//					}
//				}
			}
		}
	}
	public float dist(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	public void touchInterpret(float x , float y) {

		if (wWidth != 0) {
			float [] nPv = translatePointToWorld(x, y);


			if (mode == 2) {
				if (toMove != -1) {
//					newPolyz.get(selectedI).ptz[toMove] = new glVector(nPv[0], nPv[1]);
					triggerAction = 8;
					toMove = -1;
				} else {
//					if (selectMode && newPolyz.size() > 0) {
//						selectedI = -1;
//						for (int i = 0; i < newPolyz.size(); i++) {
//							if (newPolyz.get(i).current) {
//								if (newPolyz.get(i).isInPoly(nPv[0], nPv[1])) {
//									selectedI = i;
//									i = newPolyz.size();
//								}
//							}
//						}
//					}
//					if (newPolyType > 0) {
//						if (newPoly.size() > 0 && Math.abs(nPv[0] - newPoly.get(0).x) < 0.02 &&
//							Math.abs(nPv[1] - newPoly.get(0).y) < 0.02) {
//							dh.popYesNoDialog(0, "Close polygon?");
//						} else {
//							newPoly.add(new glVector(nPv[0], nPv[1]));
//							polyChanged = true;			
//						}
//					}
				}
			} 
		}
	}
	public void stopAndSave() {
//		params.clearHistory(currYear, currSession);
//		params.historyML = new glObjects.multiLine(params.history);
		fh.clearFile("scaff.csv");
//		fh.storeSolution(params.historyInfo, params.history);
	}
	public void loadAndStart() {

	}
	private void closePoly() {
		newPoly.clear();
//		newPolyz.add(nPoly);
//		fh.appendPoly(nPoly);
	}

	public void buildCurrentSite() {
		polygon [] prevPolyz = fh.readData();
//		newPolyz.clear();
//		System.out.println("tammit" + prevPolyz.length);
		for (int i = 0; i < prevPolyz.length; i++) {
			//System.out.println(prevPolyz[i].t);
//			newPolyz.add(prevPolyz[i]);
		}
		ArrayList <glVector> inf = new ArrayList <glVector>();
		ArrayList <glVector> ptz = new ArrayList <glVector>();
		glVector [] hist = fh.readScaff();
		for (int i = 0; i < hist.length; i++) {
			if (i % 2 == 0) inf.add(hist[i]);
			else ptz.add(hist[i]);
			//	System.out.println(hist[i].x);
		}

//		for(int i = 0; i < params.history.length; i++) {
//		}
	}
	public void buildPlan() {
//		plan.generate(parent, 2.5f * 2048.0f * iWidth, 0.35f, 0f, 0f, R.drawable.planclean);
	}
	public void setOverMessage() {
		String [] overStrings = new String[10];
//		overMessage = new textureQuad[overStrings.length];

		overStrings[0] = "";
		overStrings[1] = "PRE - EXCAVATION NOTATION MODE";
		overStrings[2] = "POST - EXCAVATION NOTATION MODE";
		overStrings[3] = "AREA REMOVAL MODE \n Tap on polygon to select";
		overStrings[4] = "AREA EDITING MODE \n Tap on polygon to select";
		overStrings[5] = "AREA EDITING MODE \n Tap and hold to move points";
		overStrings[6] = "AREA ADDITION MODE \n Tap to add points of polygon";
		overStrings[7] = "AREA ADDITION MODE \n Tap to add points of polygon";
		overStrings[8] = "AREA ADDITION MODE \n Tap to add points of polygon";
		overStrings[9] = "AREA ADDITION MODE \n Tap to add points of polygon";

		for (int i = 0; i < overStrings.length;i++) {
//			overMessage[i] = new textureQuad();
//			overMessage[i].generate(parent, 256.0f * iWidth, -ratio + 512.0f * iWidth, -1f + 512.0f * iWidth, 0f,
////									overMessage[i].generate(parent, 256.0f * iWidth, 0, 0, 0f,
//									overStrings[i], 0);
		}

	}
	public void generateYearInfo() {

		String s =
			"Current session:" + currYear + " / " + currSession;
//		yearInfo.generate(parent, 256.0f * iWidth, -ratio * 0.5f, 0.05f, 0f, s, 1);
	}
	public void generateGridInfo() {
		String s = "Grid Properties:";
		if (grid.def) s += "(default)";
		s += "\n" +
			"Line Distance in X: " + grid.gridX * 10 + "m \n" +
			"Line Distance in Y: " + grid.gridY * 10 + "m \n" +
			"Sub-grid divisions: " + grid.divisions + "\n" +
			"";
//		gridInfo.generate(parent, 256.0f / wWidth, 0f, 0.05f, 0f, s, 1);
	}
	public void generateLocationInfo() {
		String s =
			"Selected base location:\n" +
			"Lat: " + baseLocationLat + "\n" +
			"Lon: " + baseLocationLon + "\n" +
			"";
//		locInfo.generate(parent, 256.0f / wWidth, ratio / 2, 0.05f, 0f, s, 1);
	}

}
