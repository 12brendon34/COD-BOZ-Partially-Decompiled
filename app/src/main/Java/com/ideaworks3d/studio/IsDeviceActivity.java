package com.ideaworks3d.studio;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import com.ideaworks3d.marmalade.LoaderActivity;
import com.savegame.SavesRestoring;

public class IsDeviceActivity extends LoaderActivity {

   private static IsDeviceActivity m_Activity;
   private static final String TAG = "IsDeviceActivity";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      SavesRestoring.DoSmth(this);

      // Set cutout mode before super.onCreate or setContentView
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
         WindowManager.LayoutParams lp = getWindow().getAttributes();
         lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
         getWindow().setAttributes(lp);
      }

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

   @SuppressWarnings("deprecation")
   private void applyLegacyImmersiveMode() {
      int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_FULLSCREEN
              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              | View.SYSTEM_UI_FLAG_IMMERSIVE;

       getWindow().getDecorView().setSystemUiVisibility(flags);
   }


   @Override
   public void onWindowFocusChanged(boolean hasFocus) {
      super.onWindowFocusChanged(hasFocus);

      if (hasFocus) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ (API 30+)
            final WindowInsetsController insetsController = getWindow().getInsetsController();

            if (insetsController != null) {
               insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
               insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
            getWindow().setDecorFitsSystemWindows(false);
         } else {
            applyLegacyImmersiveMode();
         }
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
