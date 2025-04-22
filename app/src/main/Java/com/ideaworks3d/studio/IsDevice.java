package com.ideaworks3d.studio;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

import android.content.res.Resources;
import java.io.File;

import com.ideaworks3d.marmalade.LoaderActivity;

class IsDevice implements Cloneable {
    public static final String TAG = "IsDevice";
    private static IsDevice instance = null;

    private boolean isActivated = false;
    public int tabletThreshold = 461;

    public DisplayMetrics deviceMetrics = new DisplayMetrics();

    private final File externalStorageDir = Environment.getExternalStorageDirectory();
    private final StatFs statFs = new StatFs(externalStorageDir.getAbsolutePath());

    public static final String EXP_PATH = File.separator + "Android" + File.separator + "obb" + File.separator;

    private IsDevice() {
        instance = this;
    }

    public static synchronized IsDevice GetInstance() {
        if (instance == null) {
            instance = new IsDevice();
        }
        return instance;
    }

    public native void IsDeviceKeyCallback(int keyCode);

    public boolean IsActivated() {
        return this.isActivated;
    }

    public String GetExpansionPath() {
        String path = Environment.getExternalStorageDirectory() +
                EXP_PATH +
                LoaderActivity.m_Activity.getPackageName() +
                File.separator;
        Log.i(TAG, "Expansion Path: " + path);
        return path;
    }

    public void Activate() {
        this.isActivated = true;
    }

    public int IsDeviceSetTabletThreshold(int threshold) {
        this.tabletThreshold = threshold;
        return this.tabletThreshold;
    }
    public int IsDeviceGetDisplayType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getDisplayMetricsApi30Plus();
        } else {
            getDisplayMetricsLegacy();
        }

        float widthInches = deviceMetrics.widthPixels / deviceMetrics.xdpi;
        float heightInches = deviceMetrics.heightPixels / deviceMetrics.ydpi;
        int screenSize = (int) (widthInches * widthInches + heightInches * heightInches);

        if (screenSize >= this.tabletThreshold) {
            return 3; // Tablet
        } else if (screenSize > 0) {
            return 2; // Phone
        } else {
            return 1; // Unknown or small display
        }
    }
    @TargetApi(Build.VERSION_CODES.R)
    private void getDisplayMetricsApi30Plus() {
        WindowManager wm = (WindowManager) LoaderActivity.m_Activity.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            WindowMetrics metrics = wm.getCurrentWindowMetrics();
            Rect bounds = metrics.getBounds();
            deviceMetrics.widthPixels = bounds.width();
            deviceMetrics.heightPixels = bounds.height();
            deviceMetrics.xdpi = Resources.getSystem().getDisplayMetrics().xdpi;
            deviceMetrics.ydpi = Resources.getSystem().getDisplayMetrics().ydpi;
        }
    }

    @SuppressWarnings("deprecation")
    private void getDisplayMetricsLegacy() {
        Display display = LoaderActivity.m_Activity
                .getWindowManager()
                .getDefaultDisplay();

        display.getMetrics(this.deviceMetrics);
    }

    public String[] IsDeviceGetExternalResources(int param1, int param2) {
        return new String[]{"stub", "stub"};
    }
    public int IsDeviceGetAvailableBlocks() {
        statFs.restat(externalStorageDir.getAbsolutePath());
        long blocks = statFs.getAvailableBlocksLong();

        //no way it's going over the limit but whatever
        return (blocks > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) blocks;
    }

    public int IsDeviceGetBlockCount() {
        statFs.restat(externalStorageDir.getAbsolutePath());
        long blocks = statFs.getBlockCountLong();
        return (blocks > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) blocks;
    }

    public int IsDeviceGetBlockSize() {
        statFs.restat(externalStorageDir.getAbsolutePath());
        long size = statFs.getBlockSizeLong();
        return (size > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) size;
    }

    public int IsDeviceGetFreeBlocks() {
        statFs.restat(externalStorageDir.getAbsolutePath());
        long blocks = statFs.getFreeBlocksLong();
        return (blocks > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) blocks;
    }

    public String IsDeviceGetAbsolutePath() {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
        Log.v(TAG, "AbsolutePath returning: " + absolutePath);
        return absolutePath;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
