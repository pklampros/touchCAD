package net.orangevertex.touchcad;

import net.orangevertex.glCore.*;

public class grid {

	private static glObjects.multiLine grid;
	private static glObjects.multiLine gridSec;
	public static float gridX,gridY;
	public static int divisions;
	public static boolean def = true;
	public grid() {
	}
	public static void draw(float [] mvp) {
		gridSec.draw(mvp);
		grid.draw(mvp);
	}
	public static void setup(float gridXMeters, float gridYMeters,
							 int gridXNum, int gridYNum,
							 int gridXOffset, int gridYOffset, int gridSecDivision) {
		divisions = gridSecDivision;
		gridX = gridXMeters * glUI.sceneSize;
		gridY = gridYMeters * glUI.sceneSize;
		float gridSecX = gridX / gridSecDivision;
		float gridSecY = gridX / gridSecDivision;
		float [] gridLinez = new float[(gridXNum + gridYNum) * 6];
		float [] gridSecLinez = new float[(gridXNum + gridYNum) * 6 * (gridSecDivision - 1)];
		for (int i = 0; i < gridXNum; i++) {
			gridLinez[6 * i] = -gridX * gridXNum * 0.5f - gridX * gridXOffset + gridX * i;
			gridLinez[6 * i + 1] = -gridY * gridYNum * 0.5f - gridY * gridYOffset - gridY * 0.5f * (gridYNum % 2) - gridX * 0.5f;
			gridLinez[6 * i + 2] = 0;
			gridLinez[6 * i + 3] = -gridX * gridXNum * 0.5f - gridX * gridXOffset + gridX * i;
			gridLinez[6 * i + 4] = gridY * gridYNum * 0.5f - gridY * gridYOffset  - gridY * 0.5f * (gridYNum % 2) - gridX * 0.5f;
			gridLinez[6 * i + 5] = 0;
			for (int j = 0; j < gridSecDivision - 1; j++) {
				gridSecLinez[(6 * i) * (gridSecDivision - 1) + 6 * j] = gridLinez[6 * i] + gridSecX * (j + 1);
				gridSecLinez[(6 * i) * (gridSecDivision - 1) + 6 * j + 1] = gridLinez[6 * i + 1];
				gridSecLinez[(6 * i) * (gridSecDivision - 1) + 6 * j + 2] = 0;
				gridSecLinez[(6 * i) * (gridSecDivision - 1) + 6 * j + 3] = gridLinez[6 * i] + gridSecX * (j + 1);
				gridSecLinez[(6 * i) * (gridSecDivision - 1) + 6 * j + 4] = gridLinez[6 * i + 4];
				gridSecLinez[(6 * i) * (gridSecDivision - 1) + 6 * j + 5] = 0;
			}
		}
		for (int i = 0; i < gridYNum; i++) {
			gridLinez[gridXNum * 6 + 6 * i] = -gridX * gridXNum * 0.5f - gridX * gridXOffset - gridX * 0.5f * (gridXNum % 2) - gridX * 0.5f;
			gridLinez[gridXNum * 6 + 6 * i + 1] = -gridY * gridYNum * 0.5f - gridY * gridYOffset + gridY * i;
			gridLinez[gridXNum * 6 + 6 * i + 2] = 0;
			gridLinez[gridXNum * 6 + 6 * i + 3] = gridX * gridXNum * 0.5f - gridX * gridXOffset - gridX * 0.5f * (gridXNum % 2) - gridX * 0.5f;
			gridLinez[gridXNum * 6 + 6 * i + 4] = -gridY * gridYNum * 0.5f - gridY * gridYOffset + gridY * i;
			gridLinez[gridXNum * 6 + 6 * i + 5] = 0;
			for (int j = 0; j < gridSecDivision - 1; j++) {
				gridSecLinez[(gridXNum * 6 + 6 * i) * (gridSecDivision - 1) + 6 * j] = gridLinez[gridXNum * 6 + 6 * i];
				gridSecLinez[(gridXNum * 6 + 6 * i) * (gridSecDivision - 1) + 6 * j + 1] = gridLinez[gridXNum * 6 + 6 * i + 1] + gridSecY * (j + 1);
				gridSecLinez[(gridXNum * 6 + 6 * i) * (gridSecDivision - 1) + 6 * j + 2] = 0;
				gridSecLinez[(gridXNum * 6 + 6 * i) * (gridSecDivision - 1) + 6 * j + 3] = gridLinez[gridXNum * 6 + 6 * i + 3];
				gridSecLinez[(gridXNum * 6 + 6 * i) * (gridSecDivision - 1) + 6 * j + 4] = gridLinez[gridXNum * 6 + 6 * i + 4] + gridSecY * (j + 1);
				gridSecLinez[(gridXNum * 6 + 6 * i) * (gridSecDivision - 1) + 6 * j + 5] = 0;
			}
		}
		grid = new glObjects.multiLine(gridLinez);
		gridSec = new glObjects.multiLine(gridSecLinez);
		grid.color = new float [] {0.8f,0.8f,0.8f,1f};
		gridSec.color = new float [] {0.95f,0.95f,0.95f,1f};
	}
}
