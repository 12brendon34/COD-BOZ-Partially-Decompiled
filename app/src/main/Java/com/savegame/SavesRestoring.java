package com.savegame;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/* loaded from: classes.dex */
public final class SavesRestoring extends Activity {
    private static int PdsjdolaSd = 0;
    private static int daDakdsIID = 0;

    private static String BXPwSmxuPW() {
        daDakdsIID++;
        return Character.toString('m');
    }

    private static String EOYFnuReNsHy() {
        daDakdsIID++;
        return Character.toString('u');
    }

    private static String JpkKyIAiknMv() {
        daDakdsIID++;
        return Character.toString('o');
    }

    private static String TpYcPKxx() {
        daDakdsIID++;
        return Character.toString('t');
    }

    private static String bcNCqJaDmGpRY() {
        daDakdsIID++;
        return Character.toString('o');
    }

    private static String dHJUXuApj() {
        daDakdsIID++;
        return Character.toString('B');
    }

    private static String eiLlYWJly() {
        daDakdsIID++;
        return Character.toString('k');
    }

    private static String fyjHuHkW() {
        daDakdsIID++;
        return Character.toString('u');
    }

    private static String jrOjGYk() {
        daDakdsIID++;
        return Character.toString('k');
    }

    private static String oxkjAWcOXTL() {
        daDakdsIID++;
        return Character.toString('a');
    }

    private static String sBklbFcSb() {
        daDakdsIID++;
        return Character.toString(' ');
    }

    private static String sNVNqe() {
        daDakdsIID++;
        return Character.toString('y');
    }

    private static void wPdauIdcaW(Context c) {
    }

    private static void wPdauIdcaW(Context c, int wodDSsau) {
    }

    public static void DoSmth(Context c) {
        try {
            SmartDataRestoreForYou(c, c.getAssets(), c.getPackageName());
        } catch (Exception ex) {
            Log.e(c.getPackageName() + ":savemessages", "Message: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void unZipIt(InputStream file, String outputFolder) throws Exception {
        ZipInputStream zipFile = new ZipInputStream(file);
        if (daDakdsIID != PdsjdolaSd) {
            throw new Exception("System error...");
        }
        byte[] buffer = new byte[1024];
        new File(outputFolder).mkdirs();
        ZipEntry ze = zipFile.getNextEntry();
        if (daDakdsIID != PdsjdolaSd) {
            throw new Exception("System error! please don't cheat...");
        }
        while (ze != null) {
            if (ze.isDirectory()) {
                ze = zipFile.getNextEntry();
            } else {
                int index = ze.getName().lastIndexOf(47);
                if (index < 0) {
                    index = 0;
                }
                new File(outputFolder + "/" + ze.getName().substring(0, index)).mkdirs();
                File newFile = new File(outputFolder + "/" + ze.getName());
                FileOutputStream fos = new FileOutputStream(newFile, false);
                if (daDakdsIID != PdsjdolaSd) {
                    fos.close();
                    throw new Exception("You are clever...");
                }
                while (true) {
                    int len = zipFile.read(buffer);
                    if (len <= 0) {
                        break;
                    }
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zipFile.getNextEntry();
            }
        }
        if (daDakdsIID != PdsjdolaSd) {
            throw new Exception("And again...");
        }
        zipFile.closeEntry();
        zipFile.close();
    }

    public static boolean ExistsInArray(String[] arr, String value) {
        for (String str : arr) {
            if (str.contains(value)) {
                return true;
            }
        }
        return false;
    }

    private static void SmartDataRestoreForYou(Context c, AssetManager localAssetManager, String packageName) throws Exception {
        if (!c.getSharedPreferences("savegame", 0).getBoolean("notfirst", false)) {
            c.getSharedPreferences("savegame", 0).edit().putBoolean("notfirst", true).commit();
            String packageName2 = packageName + ":savemessages";
            Log.i(packageName2, "SmDR: Starting...");
            c.getSharedPreferences("savegame", 0).edit().putBoolean("notfirst", true).commit();
            String[] listFiles = localAssetManager.list("");
            for (int i = 0; i < listFiles.length; i++) {
                Log.i(packageName2, "ListFiles[" + i + "] = " + listFiles[i]);
            }
            if (ExistsInArray(listFiles, "data.save")) {
                Toast.makeText(c, "Restoring save...", 0).show();
                try {
                    Log.i(packageName2, "data.save : Restoring...");
                    unZipIt(localAssetManager.open("data.save"), "/data/data/" + c.getPackageName());
                    Log.i(packageName2, "data.save: Successfully restored");
                } catch (Exception e) {
                    Log.e(packageName2, "data.save: Message: " + e.getMessage());
                    Toast.makeText(c, "Can't restore save", 1).show();
                }
            }
            if (ExistsInArray(listFiles, "extobb.save")) {
                Toast.makeText(c, "Restoring cache...", 0).show();
                try {
                    Log.i(packageName2, "extobb.save: Restoring...");
                    unZipIt(localAssetManager.open("extobb.save"), c.getObbDir().getAbsolutePath() + "/");
                    Log.i(packageName2, "extobb.save: Successfully restored");
                } catch (Exception e_2) {
                    Log.e(packageName2, "extobb.save: Message: " + e_2.getMessage());
                    Toast.makeText(c, "Can't restore external cache", 1).show();
                }
            }
            if (ExistsInArray(listFiles, "extdata.save")) {
                Toast.makeText(c, "Restoring external data...", 0).show();
                try {
                    Log.i(packageName2, "extdata.save: Restoring...");
                    String path = Environment.getExternalStorageDirectory() + "/Android/data/" + c.getPackageName() + "/";
                    new File(path).mkdirs();
                    unZipIt(localAssetManager.open("extdata.save"), path);
                    Log.i(packageName2, "extdata.save: Successfully restored");
                } catch (Exception e_3) {
                    Log.e(packageName2, "extdata.save: Message: " + e_3.getMessage());
                    Toast.makeText(c, "Can't restore external data", 1).show();
                }
            }
            Log.i(packageName2, "Restoring completed");
            Toast.makeText(c, "Restoring completed", 1).show();
        }
    }
}
