package com.ideaworks3d.studio;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.ideaworks3d.marmalade.LoaderActivity;
import com.savegame.SavesRestoring;

public class IsDeviceActivity extends LoaderActivity {

   private static IsDeviceActivity m_Activity;
   private static final String TAG = "IsDeviceActivity";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      SavesRestoring.DoSmth(this);
      super.onCreate(savedInstanceState);
      m_Activity = this;
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_POWER || keyCode == KeyEvent.KEYCODE_HOME) {
         Log.v(TAG, "onKeyDown Received: " + keyCode);
         if (IsDevice.GetInstance().IsActivated()) {
            IsDevice.GetInstance().IsDeviceKeyCallback(keyCode);
         }
      }
      return super.onKeyDown(keyCode, event);
   }

   @Override
   protected void onPause() {
      Log.v(TAG, "onPause");
      super.onPause();
   }

   @Override
   protected void onResume() {
      Log.v(TAG, "onResume");
      super.onResume();
   }

   @Override
   protected void onStart() {
      super.onStart();
      Log.v(TAG, "onStart");
   }

   @Override
   protected void onUserLeaveHint() {
      Log.v(TAG, "onUserLeaveHint");
      super.onUserLeaveHint();
   }
   @Override
   public void onWindowFocusChanged(boolean hasFocus) {
      super.onWindowFocusChanged(hasFocus);

      if (hasFocus) {
         getWindow().getDecorView().setSystemUiVisibility(
                 View.SYSTEM_UI_FLAG_IMMERSIVE
                         | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                         | View.SYSTEM_UI_FLAG_FULLSCREEN
                         | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                         | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                         | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
         );
      }

      if (!hasFocus && IsDevice.GetInstance().IsActivated()) {
         IsDevice.GetInstance().IsDeviceKeyCallback(KeyEvent.KEYCODE_HOME);
      }
   }

   // Called by Marmalade's s3eLibraryLoad libIsDevice.so
   public static IsDeviceActivity getInstance() {
      return m_Activity;
   }
}
