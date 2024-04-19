package com.ideaworks3d.studio;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.ideaworks3d.marmalade.LoaderActivity;
import com.savegame.SavesRestoring;

public class IsDeviceActivity extends LoaderActivity {
   private static final byte[] SALT = new byte[]{-46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32, -64, 89};
   public static final int STATUS_SUCCESS = 200;
   private static IsDeviceActivity m_Activity;
   private final String NO_FILE = null;
   private final String TAG = new String("IsDeviceActivity");
   //private ZipResourceFile mAPKExtensionFile = null;

   /*
   public boolean handleFileUpdated(DownloadsDB var1, int var2, String var3, long var4) {
      boolean var6 = true;
      boolean var7 = false;
      DownloadInfo var8 = var1.getDownloadInfoByFileName(var3);
      if (var8 != null) {
         String var9 = var8.mFileName;
         if (var9 != null) {
            if (var3.equals(var9)) {
               var6 = var7;
               return var6;
            }

            File var10 = new File(Helpers.generateSaveFileName(this, var9));
            if (var10.exists()) {
               var10.delete();
            }

            Log.i(this.TAG, "Deleting Partial Download file");
         }
      }

      if (Helpers.doesFileExist(this, var3, var4, true)) {
         var6 = false;
      }

      return var6;
   }
   */

   public boolean hasActiveInternetConnection(Context var1) {
      boolean var2;
      if (((WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()) {
         var2 = true;
      } else {
         Log.d(this.TAG, "No network available!");
         var2 = false;
      }

      return var2;
   }

   public void onCreate(Bundle var1) {
      SavesRestoring.DoSmth(this);
      super.onCreate(var1);
      m_Activity = this;
   }

   protected void onDestroy() {
      super.onDestroy();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      if (26 == var1 || 3 == var1) {
         Log.v(this.TAG, "onKeyDown Recieved: " + var1);
         if (IsDevice.GetInstance().IsActivated()) {
            IsDevice.GetInstance().IsDeviceKeyCallback(var1);
         }
      }

      return super.onKeyDown(var1, var2);
   }

   protected void onPause() {
      Log.v(this.TAG, "onPause");
      super.onPause();
   }

   protected void onResume() {
      Log.v(this.TAG, "onResume");
      super.onResume();
   }

   protected void onStart() {
      super.onStart();
      Log.v(this.TAG, "onStart");
   }

   protected void onUserLeaveHint() {
      Log.v(this.TAG, "onUserLeaveHint");
      super.onUserLeaveHint();
   }

   public void onWindowFocusChanged(boolean var1) {
      if (!var1 && IsDevice.GetInstance().IsActivated()) {
         IsDevice.GetInstance().IsDeviceKeyCallback(3);
      }

   }
}
