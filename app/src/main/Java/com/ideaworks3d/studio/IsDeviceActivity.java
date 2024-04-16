package com.ideaworks3d.studio;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.ideaworks3d.marmalade.LoaderActivity;
import com.savegame.SavesRestoring;
import java.io.File;
/* loaded from: classes.dex */
public class IsDeviceActivity extends LoaderActivity {
    private static final byte[] SALT = {-46, 65, 30, Byte.MIN_VALUE, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32, -64, 89};
    public static final int STATUS_SUCCESS = 200;
    private static IsDeviceActivity m_Activity;
    private final String TAG = new String("IsDeviceActivity");
    private final String NO_FILE = null;

    @Override // com.ideaworks3d.marmalade.LoaderActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        SavesRestoring.DoSmth(this);
        super.onCreate(bundle);
        m_Activity = this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ideaworks3d.marmalade.LoaderActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        Log.v(this.TAG, "onStart");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ideaworks3d.marmalade.LoaderActivity, android.app.Activity
    public void onPause() {
        Log.v(this.TAG, "onPause");
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ideaworks3d.marmalade.LoaderActivity, android.app.Activity
    public void onResume() {
        Log.v(this.TAG, "onResume");
        super.onResume();
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        Log.v(this.TAG, "onUserLeaveHint");
        super.onUserLeaveHint();
    }

    public boolean hasActiveInternetConnection(Context context) {
        if (((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()) {
            return true;
        }
        Log.d(this.TAG, "No network available!");
        return false;
    }

    @Override // com.ideaworks3d.marmalade.LoaderActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        if (!z && IsDevice.GetInstance().IsActivated()) {
            IsDevice.GetInstance().IsDeviceKeyCallback(3);
        }
    }

    @Override // com.ideaworks3d.marmalade.LoaderActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (26 == i || 3 == i) {
            Log.v(this.TAG, "onKeyDown Recieved: " + i);
            if (IsDevice.GetInstance().IsActivated()) {
                IsDevice.GetInstance().IsDeviceKeyCallback(i);
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ideaworks3d.marmalade.LoaderActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }
}
