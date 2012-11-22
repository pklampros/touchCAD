//package net.orangevertex.touchcad;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//
//public class Splash extends Activity{
//
////	MediaPlayer ourSong;
//	@Override
//	protected void onCreate(Bundle bundle) {
//		super.onCreate(bundle);
//		setContentView(R.layout.splash);
////		ourSong = MediaPlayer.create(Splash.this, R.raw.brion);
////		ourSong.start();
//		Thread timer = new Thread() {
//			public void run() {
//				try{
//					sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} finally {
//					Intent openStartingPoint = new Intent("net.orangevertex.orangevertex.MainActivity");
//					startActivity(openStartingPoint);
//				}
//			}
//		};
//		timer.start();
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
////		ourSong.release();
//		finish();
//	}
//
//	
//}
