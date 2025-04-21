package com.ideaworks3d.studio;

import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ideaworks3d.marmalade.LoaderActivity;

import java.io.File;

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

    // Called by Marmalade's libIsDevice.so
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
        LoaderActivity.m_Activity.getWindowManager()
                .getDefaultDisplay()
                .getMetrics(this.deviceMetrics);

        float widthInches = this.deviceMetrics.widthPixels / this.deviceMetrics.xdpi;
        float heightInches = this.deviceMetrics.heightPixels / this.deviceMetrics.ydpi;
        int screenSize = (int) (widthInches * widthInches + heightInches * heightInches);

        if (screenSize >= this.tabletThreshold) {
            return 3; // Tablet
        } else if (screenSize > 0) {
            return 2; // Phone
        } else {
            return 1; // Unknown or small display
        }
    }

    public String[] IsDeviceGetExternalResources(int param1, int param2) {
        // Placeholder return; actual implementation may vary
        return new String[]{"stub", "stub"};
    }

    public int IsDeviceGetAvailableBlocks() {
        statFs.restat(externalStorageDir.getAbsolutePath());
        return statFs.getAvailableBlocks();
    }

    public int IsDeviceGetBlockCount() {
        statFs.restat(externalStorageDir.getAbsolutePath());
        return statFs.getBlockCount();
    }

    public int IsDeviceGetBlockSize() {
        statFs.restat(externalStorageDir.getAbsolutePath());
        return statFs.getBlockSize();
    }

    public int IsDeviceGetFreeBlocks() {
        statFs.restat(externalStorageDir.getAbsolutePath());
        return statFs.getFreeBlocks();
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
