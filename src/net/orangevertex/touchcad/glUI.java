package net.orangevertex.touchcad;

import net.orangevertex.glCore.*;
import android.opengl.Matrix;
//import net.orangevertex.adaptivecanopy.obj.*;
import android.content.*;


public class glUI {
//	float minX,maxX,minY,maxY;
	camera cam;
	GLRenderer parent;
	boolean wasDouble = false;
	boolean wasMoving = false;
	int mode = 2;
	public static final float sceneSize = 0.1f;
	private textureQuad [] icons = new textureQuad[27];
	private textureQuad [] texts = new textureQuad[icons.length];
	public textureQuad [] infos = new textureQuad[icons.length];
	button [] buttons = new button[icons.length - 7];
	button [] editButtons;
	boolean displayText = false;
	int hoverButton = -1;
	public float screenXdim;
	public float screenYdim;

	public glUI(GLRenderer p) {
		parent = p;
		cam = new camera();
		for (int i = 0; i < icons.length; i++) {
			icons[i] = new textureQuad();
			texts[i] = new textureQuad();
			infos[i] = new textureQuad();
		}
//		buttons = new button[8];
//		for (int i = 0; i < 8; i++) {
////			buttons[i] = new button(2 * 1280 / 3 + (float)Math.floor(i / 4.1) * (1280 / 6) - 10,
////									2 * 1280 / 3 + ((float)Math.floor(i / 4) + 1) * (1280 / 6) - 10,
////									(i % 4) * 800 / 4 - 10,
////									((i % 4) + 1) * 800 / 4 - 10);
//			buttons[i] = new button(0,0,0,0);
//		}
	}

	public boolean checkButtons(int t, float x, float y) {
		boolean result = false;
		hoverButton = -1;
		for (int i = 0; i < buttons.length;i++) {
//				System.out.println("button: " + buttons[i].minX + " " + buttons[i].maxX);
//				System.out.println("button: " + buttons[i].minY + " " + buttons[i].maxY);
//				System.out.println("button: " + c1Y + " " + c1X);
			if (y > buttons[i].minY && y < buttons[i].maxY &&
				x > buttons[i].minX && x < buttons[i].maxX) {
				if (t == 0) result = parent.triggerEvent(i);
				if (t == 1) {
					if (parent.mode == 0 &&
						(i >= 0 || i <= 2)) 
						parent.infoScreen = i;
					if (parent.mode == 1 &&
						(i >= 3 && i <= 7))
						parent.infoScreen = i;
					if (parent.mode == 2 &&
						(i >= 8 && i <= 15))
						parent.infoScreen = i;
					if (parent.mode == 3 && 
						(i == 8 || i == 15))
						parent.infoScreen = i;
					if (parent.mode == 3 &&
						(i >= 9 && i <= 14))
						parent.infoScreen = i + 7;
					if (parent.infoScreen != -1)
						result = true;
				}
				if (t == 2) {
					if (parent.mode == 0 &&
						(i >= 0 || i <= 2)) 
						hoverButton = i;
					if (parent.mode == 1 &&
						(i >= 3 && i <= 7))
						hoverButton = i;
					if (parent.mode > 1 &&
						(i >= 8 && i <= 15))
						hoverButton = i;
					if (hoverButton != -1) 
						result = true;
				}
				//System.out.println("triggering event: " + i);

			}
		}
		return result;
	}
//	public void drawHoverButton(float [] mvp) {
//		if(hoverButton != -1) 
//	}
	public boolean setPointerDown(float x, float y) {
		return checkButtons(2, x, y);
	}
	public void setPointers(int type, float height, float width, int pointerCount,
							float s1X, float s1Y, float c1X, float c1Y,
							float s2X, float s2Y, float c2X, float c2Y) {
		boolean entered = false;
		if (type == 3) {
			entered = checkButtons(0, c1X, c1Y);
			if (!entered) {
				parent.touchInterpret(c1X, c1Y);
			}
		}
		if (c1X < 8 * width / 8 && !entered) {

			if (type == 1) {
				if (pointerCount == 1) {
//					if(wasDouble && pointer1up && pointer2up) {
//						pointer1up = false;
//						pointer2up = false;
//					wasDouble = false;
//					}
//					if ((s1X == c1X && s1Y == c1Y)) {} else 
					if (!wasDouble) {
						cam.setSinglePointer(height,
											 width, s1X, s1Y, c1X, c1Y);
					}
				} else if (pointerCount == 2) {
					if (s1X == c1X && 
						s1Y == c1Y && 
						s2X == c2X &&
						s2Y == c2Y) {} else {
						wasDouble = true;
						cam.setDoublePointer(s1X, s1Y, c1X, c1Y, s2X, s2Y, c2X, c2Y);
					}
				}
			} else {
				if (pointerCount == 1) {

				}
			}
		} else {
			if (type == 0) {
				for (int i = 0; i < buttons.length;i++) {
					if (c1Y > buttons[i].minY && c1Y < buttons[i].maxY &&
						c1X > buttons[i].minX && c1X < buttons[i].maxX) {
						parent.triggerEvent(i);
					}
				}
			}
		}
	}
	public void generate(Context context, int width, int height, boolean landscape) {
//
//		int [] resources = new int[icons.length];
//		String [] textStrings = new String[texts.length];
//		String [] infoStrings= new String[infos.length];
		float ratio =  (float)width / height;
		float largest = width;
		screenXdim = ratio;
		screenYdim = 1.0f;
		}
//		if(!landscape) {
//			ratio = (float)height / width;
//		 largest = height;
//			screenXdim = 1f;
//			screenYdim = ratio;
//			}
//		float buttonSizeProper = 256.0f;
//		float buttonSize = buttonSizeProper / largest;
//		float hardFix = 0.02f;
//
//		resources[0] = R.drawable.newsite;
//		infoStrings[0] = "Starts a new site \n" 
//			+ "All data is reset";
//		resources[1] = R.drawable.newsession;
//		infoStrings[1] = ""
//			+ "Begins a new session on the last site \n"
//			+ "                  -------                     \n" 
//			+ "All previous inputs and structure developments \n"
//			+ "are considered old but valid and existing. All \n"
//			+ "input data in this mode will expand the current \n"
//			+ "site, and the new generation of the structure \n"
//			+ "will expand on the old one.";
//		resources[2] = R.drawable.contsite;
//		infoStrings[2] = ""
//			+ "Continues where you left off on the last site \n"
//			+ "This does not affect the data previously input";
//
//		resources[3] = R.drawable.year;
//		infoStrings[3] = ""
//			+ "Set the year of the current excavation \n"; 
//		resources[4] = R.drawable.grid;
//		infoStrings[4] = ""
//			+ "Set various options of the grid. \n"
//			+ "                  -------                     \n" 
//			+ width
//			+ height
//			+ "This grid will be overlayed on the notation \n"
//			+ "surface to make data input easier";
//		resources[5] = R.drawable.position;
//		infoStrings[5] = ""
//			+ "Set the base location of the site. \n"
//			+ "                  -------                     \n" 
//			+ "This will allow the application to better \n"
//			+ "evaluate the shelter under sun conditions \n"
//			+ "specific for the location of the site it \n"
//			+ "is set to protect. The GPS sensor of your\n"
//			+ "device may be used for this";
//		textStrings[5] = "SET LOCATION";
//		resources[6] = R.drawable.preex;
//		textStrings[6] = "PRE EX";
//		infoStrings[6] = ""
//			+ "Generate the structure before excavation \n" 
//			+ "                  -------                     \n" 
//			+ "This will take you to the input screen to \n"
//			+ "designate the areas you want to cover, before \n"
//			+ "the excavation starts. The structure generated\n"
//			+ "will be optimized to be easily reconfigured \n";
//		resources[7] = R.drawable.postex;
//		textStrings[7] = "POST EX";
//		infoStrings[7] = ""
//			+ "Generate the structure after excavation \n"
//			+ "                  -------                     \n" 
//			+ "This will take you to the input screen to \n"
//			+ "designate the areas you want to cover, if the \n"
//			+ "site has been uncovered and the structure is \n"
//			+ "to be final. In this case the application will \n"
//			+ "try to optimize it to have the lowest possible \n"
//			+ "impact on the site";
//
//		resources[8] = R.drawable.help;
//		textStrings[8] = "DISPLAY TEXT";
//		infoStrings[8] = ""
//			+ "Displays text next to tasks";
//		resources[9] = R.drawable.addprotect;
//		textStrings[9] = "ADD PROTECTION AREA";
//		textStrings[0] = "CONVERT TO PROTECTION AREA";
//		infoStrings[9] = ""
//			+ "Add / Convert to protection area \n"
//			+ "                  -------                     \n" 
//			+ "This will let you designate areas for cover"
//			+ "protection. These are areas that the application\n"
//			+ " will try to find the best shape of the \n"
//			+ "structure for. Such areas are areas with mosaics \n"
//			+ " or murals. If you are in editing mode this \n"
//			+ "will convert a selected area to protection area";
//
//		resources[10] = R.drawable.addnostep;
//		textStrings[10] = "ADD RESTRICTED AREA";
//		textStrings[1] = "CONVERT TO RESTRICTED AREA";
//		infoStrings[10] = ""
//			+ "Add / Convert to restricted area \n"
//			+ "                  -------                     \n" 
//			+ "This will let you designate areas that the \n"
//			+ "structure IS NOT allowed to step onto. The \n"
//			+ "application will try to avoid them at all \n"
//			+ "costs. If you are in editing mode this will\n"
//			+ "convert a selected area to restricted area";
//		resources[11] = R.drawable.addstep;
//		textStrings[11] = "ADD AVAILABLE AREA";
//		textStrings[2] = "CONVERT TO AVAILABLE AREA";
//		infoStrings[11] = ""
//			+ "Add / Convert to available area \n"
//			+ "                  -------                     \n" 
//			+ "This will let you designate areas that the \n"
//			+ "structure IS allowed to step onto. The \n"
//			+ "application will favour structures that place\n"
//			+ "footings there. If you are in editing mode this\n"
//			+ "will convert a selected area to available area";
//		resources[12] = R.drawable.addfeature;
//		textStrings[12] = "ADD OTHER FEATURE";
//		textStrings[3] = "CONVERT TO OTHER FEATURE";
//		infoStrings[12] = ""
//			+ "Add / Convert to other feature \n"
//			+ "                  -------                     \n" 
//			+ "This will let you designate areas of other \n"
//			+ "site features such as trees or large rocks. \n"
//			+ "They do not affect the structure and are to be \n"
//			+ "placed as visual aids for the user. If you are \n"
//			+ "in editing mode this will convert a selected \n"
//			+ "area to an other feature area";
//		resources[13] = R.drawable.remove;
//		textStrings[13] = "REMOVE ELEMENT";
//		textStrings[26] = "SELECT A POLYGON";
//		infoStrings[13] = ""
//			+ "Remove areas \n"
//			+ "                  -------                     \n" 
//			+ "This allows you to remove areas you have just \n"
//			+ "created. Press it, tap the area you want to \n"
//			+ "select it and press the CONFIRM REMOVAL button \n";
//		resources[14] = R.drawable.edit;
//		textStrings[14] = "EDIT ELEMENT";
//		infoStrings[14] = ""
//			+ "Edit areas \n"
//			+ "                  -------                     \n" 
//			+ "This allows you to edit areas you have created \n"
//			+ "Press it, tap the area you want to remove, then \n"
//			+ "either tap and hold a point of the area to move \n"
//			+ "it, or press one of the conversion buttons above\n"
//			+ "to change the area's type";
//		resources[15] = R.drawable.generate;
//		textStrings[15] = "START CALCULATION";
//		infoStrings[15] = ""
//			+ "Start optimizing \n"
//			+ "                  -------                     \n" 
//			+ "Press this to start or resume the optimization\n"
//			+ "process. This will cause the structure to take\n"
//			+ "various shapes. When it is satisfactory it will\n"
//			+ "stop changing much. Press the stop button to \n"
//			+ "save the resulting structure and the TABLE\n"
//			+ "button to be presented with what you need to \n"
//			+ "build it";
//
//		resources[16] = R.drawable.exit;
//		textStrings[16] = "SAVE AND EXIT";
//		infoStrings[16] = "";
//		resources[17] = R.drawable.tableofelements;
//		textStrings[17] = "TABLE OF ELEMENTS";
//		infoStrings[17] = ""
//			+ "Table of required materials \n"
//			+ "                  -------                     \n" 
//			+ "This table presents the materials required to\n"
//			+ "build this structure and basic cost estimation\n"
//			+ "of said structure\n";
//		resources[18] = R.drawable.regenerate;
//		textStrings[18] = "REGENERATE SOLUTION";
//		infoStrings[18] = "";
//		resources[19] = R.drawable.cancel;
//		textStrings[19] = "CLEAR ALL HISTORY";
//		infoStrings[19] = "";
//		resources[20] = R.drawable.threed;
//		textStrings[20] = "SEE IN 3D";
//		infoStrings[20] = "";
//		resources[21] = R.drawable.pausegen;
//		textStrings[21] = "PAUSE OPTIMIZATION";
//		infoStrings[21] = ""
//			+ "Stops the optimization and stores the structure \n"
//			+ "                  -------                     \n" 
//			+ "Press this to stop the structure from further\n"
//			+ "being optimized. This also stores the solution\n";
//
//		resources[22] = R.drawable.undo;
//		textStrings[22] = "UNDO";
//		infoStrings[22] = "";
//		resources[23] = R.drawable.cancel;
//		resources[26] = R.drawable.join;
//		infoStrings[26] = "OK";
//		textStrings[23] = "OK";
//		textStrings[4] = "CLOSE POLYGON";
//		textStrings[5] = "CONFIRM REMOVAL";
//		infoStrings[23] = "";
//		resources[24] = R.drawable.enter;
////		textStrings[24] = "CANCEL";
//		textStrings[24] = "FINISHED ADDING";
//		textStrings[6] = "FINISHED EDITING";
//		textStrings[7] = "FINISHED REMOVING";
//		infoStrings[24] = "";
//		resources[25] = R.drawable.prev;
//		textStrings[25] = "START CALCULATION";
//		infoStrings[25] = "";
//		for (int i = 0; i < infoStrings.length; i++) {
//			infos[i].generate(context, 1.5f * buttonSize, 0, 0, 0, infoStrings[i], 1, 22);
//		}
//		if(landscape) {
//		for (int i = 0; i < 3; i++) {
//			icons[i].generate(context, buttonSize, ratio * (-0.5f + i * 0.5f), 0, 0, resources[i]);
//			buttons[i] = new button(width * (0.25f + i * 0.25f), height * 0.5f - hardFix * height, buttonSizeProper);
//		} 
//		} else {
//			for (int i = 0; i < 3; i++) {
//				icons[i].generate(context, buttonSize, 0, ratio * (-0.5f + i * 0.5f), 0, resources[i]);
//				buttons[i] = new button(height * 0.5f - hardFix * height, width * (0.25f + i * 0.25f), buttonSizeProper);
//			} 			
//		}
//
//		for (int i = 3; i < 6; i++) {
//			icons[i].generate(context, buttonSize, ratio * (-0.5f + (i - 3) * 0.5f), -0.5f, 0, resources[i]);
//			texts[i].generate(context, buttonSize, ratio * (-0.5f + (i - 3) * 0.5f), -0.5f, 0, textStrings[i]);
//			buttons[i] = new button(width * (0.25f + (i - 3) * 0.25f), height * 0.25f - hardFix * height, buttonSizeProper);
//		}
//
//		icons[6].generate(context, buttonSize, -ratio * 0.3f, 0.5f, 0, resources[6]);
//		texts[6].generate(context, buttonSize, -ratio * 0.3f, 0.5f, 0, textStrings[6]);
//		buttons[6] = new button(2.33f * width * 0.15f, height * 0.75f - hardFix * height, buttonSizeProper);
//
//		icons[7].generate(context, buttonSize, ratio * 0.3f, 0.5f, 0, resources[7]);
//		texts[7].generate(context, buttonSize, -ratio * 0.3f, 0.5f, 0, textStrings[7]);
//		buttons[7] = new button(4.33f * width * 0.15f, height * 0.75f - hardFix * height, buttonSizeProper);
//
//		int numButtons = 8;
//		float iNumButtons = 1.0f / numButtons;
//		buttonSizeProper = 64.0f;
//		buttonSize = buttonSizeProper / width;
//		hardFix = 0.75f;
//		float iconSize = buttonSize;
//		float iconX = ratio - 2.5f * buttonSize;
//		float iconY = -1 + 0.5f * 2f * iNumButtons;
//		float iconYmult = 2f * iNumButtons;
//		float iconXmult = 1;
//		float textSize = 3f * buttonSize;
//		float textX = ratio - 11 * buttonSize;
//		float textY = -1 + 1.5f * 2f * iNumButtons;
//		float textXmult;
//		float textYmult = 2f * iNumButtons;
//
//		for (int i = 8; i < 16;i++) {
//			icons[i].generate(context, buttonSize,
//							  iconX, iconY + (i - 8) * iconYmult, 0, resources[i]);
//			texts[i].generate(context, textSize,
//							  textX, textY + (i - 8) * textYmult, 0, textStrings[i]);
//			buttons[i] = new button(width - buttonSizeProper * hardFix, (i - 8) * height * iNumButtons + 0.5f * height * iNumButtons, buttonSizeProper);
//		}
//		for (int i = 0; i < 4; i++) {
//			texts[i].generate(context, textSize,
//							  textX, textY + (i + 1) * textYmult, 0, textStrings[i]);	
//		}
//
//		texts[26].generate(context, textSize,
//						   textX, textY + (5) * textYmult, 0, textStrings[26]);	
//
//		for (int i = 16; i < 22;i++) {
//			icons[i].generate(context, buttonSize,
//							  iconX, iconY + (i - 15) * iconYmult, 0, resources[i]);
//			texts[i].generate(context, textSize,
//							  textX, textY + (i - 15) * textYmult, 0, textStrings[i]);
//		}
//		// -- undo button
//		icons[22].generate(context, buttonSize, 
//						   ratio - 7.5f * buttonSize, -1 + 4.5f * 2f * iNumButtons, 0, resources[22]);
//		texts[22].generate(context, 3f * buttonSize, 
//						   ratio - 16 * buttonSize, textY + 4f * textYmult, 0, textStrings[22]);
//		buttons[16] = new button(width - 2 * buttonSizeProper, 4.5f * height * iNumButtons, buttonSizeProper);
//
//		// -- enter button
//		icons[23].generate(context, buttonSize, 
//						   ratio - 7.5f * buttonSize, -1 + 5.5f * 2f * iNumButtons, 0, resources[23]);
//
//		icons[26].generate(context, buttonSize, 
//						   ratio - 7.5f * buttonSize, -1 + 5.5f * 2f * iNumButtons, 0, resources[26]);
//		texts[23].generate(context, 3f * buttonSize, 
//						   ratio - 16 * buttonSize, textY + 5f * textYmult, 0, textStrings[23]);
//
//		texts[4].generate(context, 3f * buttonSize, 
//						  ratio - 16 * buttonSize, textY + 5f * textYmult, 0, textStrings[4]);
//		texts[5].generate(context, 3f * buttonSize, 
//						  ratio - 16 * buttonSize, textY + 5f * textYmult, 0, textStrings[5]);
//		buttons[17] = new button(width - 2 * buttonSizeProper, 5.5f * height / numButtons, buttonSizeProper);
//		// -- cancel button
//		icons[24].generate(context, buttonSize, 
//						   ratio - 7.5f * buttonSize, -1 + 6.5f * 2f / numButtons, 0, resources[24]);
//		texts[24].generate(context, 3f * buttonSize,
//						   ratio - 16 * buttonSize, textY + 6f * textYmult, 0, textStrings[24]);
//		texts[6].generate(context, 3f * buttonSize,
//						  ratio - 16 * buttonSize, textY + 6f * textYmult, 0, textStrings[6]);
//		texts[7].generate(context, 3f * buttonSize,
//						  ratio - 16 * buttonSize, textY + 6f * textYmult, 0, textStrings[7]);
//		buttons[18] = new button(width - 2 * buttonSizeProper, 6.5f * height / numButtons, buttonSizeProper);
//
//
//		icons[25].generate(context, buttonSize, 
//						   -ratio + 2.5f * buttonSize, -1 + 7.5f * 2f / numButtons, 0.02f, resources[25]);
//		texts[25].generate(context, 3f * buttonSize, 
//						   -ratio + 11 * buttonSize, textY + 8f * textYmult, 0f, textStrings[25]);
//		buttons[19] = new button(buttonSizeProper, 7.5f * height / numButtons, buttonSizeProper);
//		// GOD I HATE OPENGL TEXT / sorry for the mess future generation...
//	}
	public void displayStartScreen(float [] mvp) {
		//Matrix.multiplyMM(mvp,0,tempTranslate,0,mvp,0);
		icons[0].draw(mvp);
		//Matrix.multiplyMM(mvp,0,tempTransMatrixX,0,mvp,0);
		icons[1].draw(mvp);
		icons[2].draw(mvp);
	}

	public void displaySiteSetup(float [] mvp) {
		icons[3].draw(mvp);
		icons[4].draw(mvp);
		icons[5].draw(mvp);
		icons[6].draw(mvp);
		icons[7].draw(mvp);
	}
	public void drawHelpIcon(float [] mvp) {
		icons[8].draw(mvp);
		if (displayText)texts[8].draw(mvp);
	}
	public void notationModeMenu(float [] mvp) {
		for (int i = 8; i < 16; i ++) {
			icons[i].draw(mvp);
			if (displayText) {
				if (i == 15 && (parent.polyIsOpen || parent.editMode || parent.removeMode)) {

				} else if (i == 14 && (parent.polyIsOpen || parent.editMode || parent.removeMode)) {
				} else if (i == 13 && (parent.polyIsOpen || parent.editMode || parent.selectedI > -1)) {
				} else if (i == 12 && parent.polyIsOpen) {} else {
					int ii = i;
					if (parent.editMode && i > 8 && i < 13) ii = i - 9;
					if (parent.removeMode && parent.selectedI == -1 && i == 13) ii = 26;
					texts[ii].draw(mvp);
				}
			}
		}
	}
	public void displayUndo(float [] mvp) {
		icons[22].draw(mvp);
		if (displayText)texts[22].draw(mvp);
	}
	public void displayFunc(float [] mvp) { // has been inverted
		if (!parent.removeMode) icons[26].draw(mvp);
		else icons[23].draw(mvp);
		if (displayText) {
			if (!parent.removeMode) texts[4].draw(mvp);
//			if (parent.editMode)texts[4].draw(mvp);
			if (parent.removeMode)texts[5].draw(mvp);
		}
	}
	public void displayDone(float [] mvp) { 
		icons[24].draw(mvp);
		if (displayText) {
			if (!parent.removeMode && !parent.editMode) texts[24].draw(mvp);
			if (parent.editMode)texts[6].draw(mvp);
			if (parent.removeMode)texts[7].draw(mvp);
		}
	}
	public void generationModeMenu(float [] mvp) {
		drawHelpIcon(mvp);
		for (int i = 16; i < 22; i ++) {
			icons[i].draw(mvp);
			if (displayText)texts[i].draw(mvp);
		}
		drawBackward(mvp);
	}
	public void drawForward(float [] mvp) {
		icons[15].draw(mvp);
		if (displayText)texts[15].draw(mvp);
	}
	public void drawBackward(float [] mvp) {
		icons[25].draw(mvp);
		if (displayText)texts[25].draw(mvp);
	}
	class button {
		float minX,maxX,minY,maxY;
//		glObjects.multiLine ml;
		float centX, centY;
		public button(float posX, float posY, float size) {
			minX = posX - size * 0.5f;
			maxX = posX + size * 0.5f;
			minY = posY - size * 0.5f;
			maxY = posY + size * 0.5f;
			centX = posX;
			centY = posY;
		}
		public button(glVector tl, glVector br) {
			minX = tl.x;
			maxX = tl.y;
			minY = br.x;
			maxY = br.y;
			centX = tl.x;
			centY = tl.y;
		}
		public button(float minx, float maxx, float miny, float maxy) {
			minX = minx;
			maxX = maxx;
			minY = miny;
			maxY = maxy;
			centX = minx + (maxx - minx) * 0.5f;
			centY = miny + (maxy - miny) * 0.5f;
		}
//		void buildGraphics() {
//			ml = new glObjects.multiLine(new float [] {minX,minY,0,
//													maxX,minY,0,
//													maxX,maxY,0,
//													minX,maxY,0});
//		}
//		void draw(float [] mvp) {
//			ml.draw(mvp);
//		}
	}

	public class camera {

		private float zFar = 100f;
		private float zNear = 0.05f;
		private float rotFactor = 0.01f;
		private float pitchFactor = -0.005f;
		private float panFactor = 0.02f;
		private float zoomFactor = 0.005f;
		private int keyCode = 16;
		private int mouseButton = 2;
		private float camDist = 4;
		private float camRot = (float) (Math.PI * 0.25);
//	private float camPitch = (float) (Math.PI * 0.25);
		private float camPitch = 0.75f;
		private glVector panVector = new glVector(0, 0);
		private glVector rotVector = new glVector(0, 0);
		private glVector camCenter = new glVector(0, 0, 0);
		private glVector camPos = new glVector(0, 0, 0);
		private int switchPanOrbit = 0;
		private int extra = 0;
		private final float maxCamPitch = 0.99f;
		private final float minCamPitch = 0.51f;
		private final float [] mProjMatrix = new float [16];
		private final float [] mVMatrix = new float [16];
		private float ratio = 1;
//	public final static String VERSION = "##library.prettyVersion##";


		public camera() {
		}

		public void setupOrtho() {
				Matrix.setLookAtM(mVMatrix, 0, 0, 0, 7, 0f, 0f, 5f, 0f, -1.0f, 0f);
				Matrix.orthoM(mProjMatrix, 0, screenXdim, -screenXdim, -screenYdim, screenYdim, 3, 10f);
				
//		Matrix.setLookAtM(mVMatrix, 0, 0, 0, 7, 0f, 0f, 5f, 0f, 1f, 0f);
		}

		public void setSinglePointer(float height, float width, float sX, float sY, float cX, float cY) {
			if (parent.mode == 2 || parent.mode == 3) {
				if (sX > width * 0.85) {
					rotVector.y = pitchFactor * (sY - cY);
				} else {
					if (sY > height * 0.87) {

						rotVector.x = rotFactor * (sX - cX);
					}
					if (sY < height * 0.87) {

						panVector.x = panFactor * (sX - cX);
						panVector.y = -1 * panFactor * (sY - cY);
					}
				}
			} else {
				panVector.x = panFactor * (sX - cX);
				panVector.y = -1 * panFactor * (sY - cY);
			}
		}
		public void setDoublePointer(float s1X, float s1Y, float c1X, float c1Y,
									 float s2X, float s2Y, float c2X, float c2Y) {

			float ddist = (float)(Math.sqrt(Math.pow(c1X - c2X, 2) 
											+ Math.pow(c1Y - c2Y, 2)) 
				- Math.sqrt(Math.pow(s1X - s2X, 2) 
							+ Math.pow(s1Y - s2Y, 2)));
			if (camDist > zNear && camDist < zFar)
				camDist += (0.5f * camDist) * zoomFactor * ddist;
			else if (camDist < zNear) 
				camDist = zNear + 0.001f;
			else if (camDist > zFar)
				camDist = zFar - 0.001f;

		}


		public void call(float [] mvpMatrix) {
			camRot += rotVector.x;
			if ((camPitch + rotVector.y) < maxCamPitch && (camPitch + rotVector.y) > minCamPitch)
				camPitch += rotVector.y;

//			glVector camPan = new glVector((panVector.x * (float) Math.sin(Math.PI
//																		   * 0.5 + (Math.PI * camRot)))
//										   + (panVector.y * (float) Math.sin(Math.PI * 1.0
//																			 + (Math.PI * camRot))),
//										   (panVector.y * (float) Math.cos(Math.PI * 1.0
//																		   + (Math.PI * camRot)))
//										   + (panVector.x * (float) Math.cos(Math.PI * 0.5
//																			 + (Math.PI * camRot))));
//
//
			glVector camPan = new glVector((panVector.y * (float) Math.sin(Math.PI - (Math.PI * camRot)))
										   + (-panVector.x * (float) Math.sin(Math.PI * 1.5f + (Math.PI * camRot))),
										   (panVector.x * (float) Math.cos(Math.PI * 1.5f + (Math.PI * camRot)))
										   + (panVector.y * (float) Math.cos(Math.PI - (Math.PI * camRot))));


			camCenter.x += camPan.x;
			camCenter.y += camPan.y;
			camPan.x = 0;
			camPan.y = 0;
			panVector.x = 0;
			panVector.y = 0;
			rotVector.x = 0;
			rotVector.y = 0;
			// camPitch = 10;

			camPos.x = camCenter.x - camDist * (float) Math.sin(Math.PI * camRot)
				* (float) Math.sin(Math.PI * camPitch);
			camPos.y = camCenter.y - camDist * (float) Math.cos(Math.PI * camRot)
				* (float) Math.sin(Math.PI * camPitch);
			camPos.z = camCenter.z - camDist * (float) Math.cos(Math.PI * camPitch);

			float [] cam = new float [16];
			Matrix.setIdentityM(cam, 0);
//		Matrix.translateM(cam,0,camPos.x,camPos.y,camPos.z);
			cam[3] = camCenter.x;
			cam[7] = camCenter.y;
//		cam[3] = camCenter.x/(float)Math.sin(Math.PI * camRot);
//		cam[7] = camCenter.y/(float)Math.cos(Math.PI * camRot);
//		cam[11] = camCenter.z;
//		Matrix.invertM(cam,0,cam,0);
//		Matrix.multiplyMM(projMatrix,0,cam,0,projMatrix,0);
//		Matrix.translateM(cam,0,camPan.x,camPan.y,0);
//		Matrix.invertM(cam,0,cam,0);

			float [] scam = new float[16];
			Matrix.setIdentityM(scam, 0);
//			Matrix.scaleM(mvpMatrix, 0, sceneSize, sceneSize, sceneSize);
//			Matrix.scaleM(mvpMatrix, 0, camDist, camDist, camDist);
			Matrix.scaleM(scam, 0, sceneSize, sceneSize, sceneSize);
			Matrix.scaleM(scam, 0, camDist, camDist, camDist);
			Matrix.multiplyMM(mvpMatrix, 0, scam, 0, mvpMatrix, 0);
			float [] rotX  = new float [16];
			float [] rotY  = new float [16];
			float [] rotZ  = new float [16];
			Matrix.setIdentityM(rotZ, 0);
			Matrix.setIdentityM(rotX, 0);
			Matrix.setIdentityM(rotY, 0);
			Matrix.rotateM(rotX, 0, (float)Math.toDegrees(camPitch * Math.PI), -1, 0, 0);
			Matrix.rotateM(rotY, 0, 180, 0, 1, 0);
			Matrix.rotateM(rotZ, 0, (float)Math.toDegrees(camRot * Math.PI), 0, 0, 1);
			Matrix.multiplyMM(rotY, 0, rotX, 0, rotY, 0);
			Matrix.multiplyMM(rotZ, 0, rotZ, 0, rotY, 0);
			Matrix.multiplyMM(mvpMatrix, 0, rotZ, 0, mvpMatrix, 0);
			Matrix.multiplyMM(mvpMatrix, 0, cam, 0, mvpMatrix, 0);
//		Matrix.setLookAtM(cam, 0, (float)(Math.sin(0.02f * frameCount)),
//			(float)(Math.cos(0.02f * frameCount)), 3, 0f+ 0f, 0f+ 0f, 0f, 0f, 0f, -1.0f);

//		Matrix.setLookAtM(cam, 0, camPos.x, camPos.y, camPos.z,
//						  camCenter.x, camCenter.y, camCenter.z, 0f, 0f, -1f);
//		Matrix.invertM(cam,0,cam,0);		
//		Matrix.multiplyMM(mvpMatrix,0,cam,0,mvpMatrix,0);
//		p.camera(camPos.x, camPos.y, camPos.z, camCenter.x, camCenter.y,
//				 camCenter.z, 0, 0, -1);


		}
		public void top(float [] mvpMatrix) {
//			camRot += rotVector.x;
//			if ((camPitch + rotVector.y) < maxCamPitch && (camPitch + rotVector.y) > minCamPitch)
//				camPitch += rotVector.y;
//
//			glVector camPan = new glVector((panVector.x * (float) Math.sin(Math.PI
//																		   * 0.5 + (Math.PI * camRot)))
//										   + (panVector.y * (float) Math.sin(Math.PI * 1.0
//																			 + (Math.PI * camRot))),
//										   (panVector.y * (float) Math.cos(Math.PI * 1.0
//																		   + (Math.PI * camRot)))
//										   + (panVector.x * (float) Math.cos(Math.PI * 0.5
//																			 + (Math.PI * camRot))));



			camCenter.x -= 2 * panVector.x / camDist;
			camCenter.y += 2 * panVector.y / camDist;
//			camPan.x = 0;
//			camPan.y = 0;
			panVector.x = 0;
			panVector.y = 0;
//			rotVector.x = 0;
//			rotVector.y = 0;
			// camPitch = 10;

//			camPos.x = camCenter.x - camDist * (float) Math.sin(Math.PI * camRot)
//				* (float) Math.sin(Math.PI * camPitch);
//			camPos.y = camCenter.y - camDist * (float) Math.cos(Math.PI * camRot)
//				* (float) Math.sin(Math.PI * camPitch);
//			camPos.z = camCenter.z - camDist * (float) Math.cos(Math.PI * camPitch);

			float [] cam = new float [16];
			Matrix.setIdentityM(cam, 0);
//		Matrix.translateM(cam,0,camPos.x,camPos.y,camPos.z);
			cam[3] = camCenter.x;
			cam[7] = camCenter.y;
//		cam[3] = camCenter.x/(float)Math.sin(Math.PI * camRot);
//		cam[7] = camCenter.y/(float)Math.cos(Math.PI * camRot);
//		cam[11] = camCenter.z;
//		Matrix.invertM(cam,0,cam,0);
//		Matrix.multiplyMM(projMatrix,0,cam,0,projMatrix,0);
//		Matrix.translateM(cam,0,camPan.x,camPan.y,0);
//		Matrix.invertM(cam,0,cam,0);
			Matrix.multiplyMM(mvpMatrix, 0, cam, 0, mvpMatrix, 0);


			Matrix.scaleM(mvpMatrix, 0, sceneSize, sceneSize, sceneSize);
			Matrix.scaleM(mvpMatrix, 0, camDist, camDist, camDist);

		}

		public float [] getMatrixTransformTop(float [] nPv) {
			nPv[0] -= camCenter.x * camDist * sceneSize;
			nPv[1] += camCenter.y * camDist * sceneSize;
			nPv[0] /= camDist * sceneSize;
			nPv[1] /= -camDist * sceneSize;
			return nPv;
		}
		public float [] getInverseTransformTop(float x, float y) {
			x *= camDist * sceneSize;
			y *= camDist * sceneSize;
			x += camCenter.x * camDist * sceneSize;
			y += camCenter.y * camDist * sceneSize;
			return new float [] {x,y};
		}
		public float [] getCamCent() {
			return new float [] {camCenter.x,camCenter.y};
		}
 		public void matrixReset(float [] mvpMatrix) {
			Matrix.multiplyMM(mvpMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		}
		public float [] getMVP() {
			float [] mvp = new float [16];
			Matrix.multiplyMM(mvp, 0, mProjMatrix, 0, mVMatrix, 0);
			return mvp;
		}

		public void setVarsFull(float v1, float v2, float v3, float v4, float v5,
								int v6, int v7, int v8, int s, int i) {
			rotFactor = v1;
			pitchFactor = v2;
			panFactor = v3;
			zoomFactor = v4;
			camDist = v5;
			keyCode = v6;
			mouseButton = v7;
			extra = v8;
			switchPanOrbit = s;
//		if (i == 1)
//			registerEvents();
		}

		public void setVarsMode(float v1, float v2, float v3, float v4,
								int m) {
			rotFactor = v1;
			pitchFactor = -v1;
			panFactor = v2;
			zoomFactor = v3;
			camDist = v4;
			switch (m) {
				case 0: // AUTOCAD
					keyCode = 16;
					mouseButton = 2;
					extra = 0;
					switchPanOrbit = 0;
					break;
				case 1: // MAX
					keyCode = 17;
					mouseButton = 2;
					extra = 0;
					switchPanOrbit = 0;
					break;
				case 2: // RHINO
					keyCode = 16;
					mouseButton = 3;
					extra = 0;
					switchPanOrbit = 1;
					break;
				case 3: // MAYA
					keyCode = 18;
					mouseButton = 1;
					extra = 2;
					switchPanOrbit = 0;
					break;
			}

//		registerEvents();
		}

		public void resetPanRot() {
			panVector.x = 0;
			panVector.y = 0;
			rotVector.x = 0;
			rotVector.y = 0;
		}

	}
}
