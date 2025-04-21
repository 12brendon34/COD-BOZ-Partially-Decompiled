package com.ideaworks3d.studio;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import com.ideaworks3d.marmalade.LoaderActivity;
import java.io.File;
class IsDevice implements Cloneable {
    public static final String TAG = "IsDevice";
    public static IsDevice s_Instance = null;
    final int eGeneric = 1;
    final int eSmartphone = 2;
    final int eTablet = 3;
    private boolean m_IsActivated = false;
    public int TabletThreshold = 461;
    public DisplayMetrics m_DeviceMetrics = new DisplayMetrics();
    private File m_CurrentDirectory = Environment.getExternalStorageDirectory();
    private StatFs m_StatFs = new StatFs(this.m_CurrentDirectory.getAbsolutePath());
    public native void IsDeviceKeyCallback(int i);

    public String GetMainExpansionFilename() {
        LoaderActivity loaderActivity = LoaderActivity.m_Activity;
        try {
            return "main." + loaderActivity.getPackageManager().getPackageInfo(loaderActivity.getPackageName(), 0).versionCode + "." + loaderActivity.getPackageName() + ".obb";
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public int IsDeviceGetMainExpansionFileSize() {
        String str = new String(GetExpansionPath() + GetMainExpansionFilename());
        Log.i(TAG, "IsDeviceGetMainExpansionFileSize value " + str);
        File file = new File(str);
        if (!file.exists()) {
            return 0;
        }
        Log.i(TAG, "IsDeviceGetMainExpansionFileSize size " + (file.length() / 1024));
        return ((int) file.length()) / 1024;
    }

    public static final String EXP_PATH = File.separator + "Android" + File.separator + "obb" + File.separator;

    public String GetExpansionPath() {
        String str = Environment.getExternalStorageDirectory().toString() + EXP_PATH + LoaderActivity.m_Activity.getPackageName() + File.separator;
        Log.i(TAG, "Expansion Path: " + str);
        return str;
    }

    private IsDevice() {
        s_Instance = this;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static synchronized IsDevice GetInstance() {
        IsDevice isDevice;
        synchronized (IsDevice.class) {
            if (s_Instance == null) {
                s_Instance = new IsDevice();
            }
            isDevice = s_Instance;
        }
        return isDevice;
    }

    public boolean IsActivated() {
        return this.m_IsActivated;
    }

    public void Activate() {
        this.m_IsActivated = true;
    }

    public int IsDeviceSetTabletThreshold(int i) {
        this.TabletThreshold = i;
        return this.TabletThreshold;
    }

    public int IsDeviceGetDisplayType() {
        LoaderActivity.m_Activity.getWindowManager().getDefaultDisplay().getMetrics(this.m_DeviceMetrics);
        float f = this.m_DeviceMetrics.widthPixels / this.m_DeviceMetrics.xdpi;
        float f2 = this.m_DeviceMetrics.heightPixels / this.m_DeviceMetrics.ydpi;
        int i = (int) (f * f * f2 * f2);
        if (i >= this.TabletThreshold) {
            return 3;
        }
        if (i > 0) {
            return 2;
        }
        return 1;
    }

    public int IsDeviceGetFreeStorage() {
        return -1;
    }

    public String IsDeviceGetAbsolutePath() {
        String str = new String(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/");
        Log.v(TAG, "AbsolutePath returning: " + str);
        return str;
    }

    public int IsDeviceGetAvailableBlocks() {
        this.m_StatFs.restat(this.m_CurrentDirectory.getAbsolutePath());
        return this.m_StatFs.getAvailableBlocks();
    }

    public int IsDeviceGetBlockCount() {
        this.m_StatFs.restat(this.m_CurrentDirectory.getAbsolutePath());
        return this.m_StatFs.getBlockCount();
    }

    public int IsDeviceGetBlockSize() {
        this.m_StatFs.restat(this.m_CurrentDirectory.getAbsolutePath());
        return this.m_StatFs.getBlockSize();
    }

    public int IsDeviceGetFreeBlocks() {
        this.m_StatFs.restat(this.m_CurrentDirectory.getAbsolutePath());
        return this.m_StatFs.getFreeBlocks();
    }

    public void IsDeviceSetPublicKey(String str) {
    }

    public String[] IsDeviceGetExternalResources(int i, int i2) {
        return new String[]{"stub", "stub"};
    }
}
