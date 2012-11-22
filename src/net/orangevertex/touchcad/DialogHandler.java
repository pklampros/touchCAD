package net.orangevertex.touchcad;

import android.app.*;
import android.content.*;
import android.os.*;

public class DialogHandler {
	Context pContext;
	FileHandler fh;
	public DialogHandler(Context parentContext,FileHandler fileHandler) {
		pContext = parentContext;
		fh = fileHandler;
	}
	
	public void popYesNoDialog(final int i, final String s) {
//		result = 0;
		Handler handler = new Handler();
//		MainActivity.this.runOnUiThread(
		handler.post(new Runnable() {
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
					switch (i) {
						case -1:
							builder.setMessage("PlaceHolder for later addition")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 0:
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 0;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 1:
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 1;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 2:
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 2;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 22:
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 22;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 3: // pass to calculation mode
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.passToGAPreEx = true;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 4: // close poly and pass to GA
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 1;
										GLRenderer.passToGAPreEx = true;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 5: // Pass to post ex mode
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.passToPostEx = true;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 6: // Stop calculation and pass to post ex
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

//							GLRenderer.stopCalc = true;
										GLRenderer.passToPostEx = true;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 7:
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.passToGAPostEx = true;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 8: // close polygon pass to ga
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 1;
										GLRenderer.passToGAPostEx = true;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 9: // discart Selected polygon
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 9;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 109: // regenerate
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 109;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 110: // clear history
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										fh.clearFile("scaff.csv");
										GLRenderer.triggerAction = 110;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 111: // set 3d
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 12;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
							break;
						case 101: // Info Dialog
							builder.setMessage(s)
								.setCancelable(true)

								.setNeutralButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										dialog.cancel();
									}
								});
//					.setNegativeButton("No", new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int id) {
//							dialog.cancel();
//						}
//					});
							break;
						case 12: // save and exit
							builder.setMessage(s)
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										GLRenderer.triggerAction = 12;
									}
								})
								.setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										MainActivity.kill();
									}
								});
							break;
					}
					builder.show();

				}
			}
		);
	}
	
}
