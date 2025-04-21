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

public final class SavesRestoring extends Activity {

   public static void DoSmth(Context context) {
      try {
         SmartDataRestoreForYou(context, context.getAssets(), context.getPackageName());
      } catch (Exception e) {
         String tag = context.getPackageName() + ":savemessages";
         Log.e(tag, "Message: " + e.getMessage(), e);
      }
   }

   public static boolean ExistsInArray(String[] array, String target) {
      for (String item : array) {
         if (item.contains(target)) {
            return true;
         }
      }
      return false;
   }

   private static void SmartDataRestoreForYou(Context context, AssetManager assets, String packageName) throws Exception {
      final String prefsName = "savegame";
      final String logTag = packageName + ":savemessages";

      if (!context.getSharedPreferences(prefsName, MODE_PRIVATE).getBoolean("notfirst", false)) {
         Log.i(logTag, "SmDR: Starting...");
         context.getSharedPreferences(prefsName, MODE_PRIVATE)
                 .edit()
                 .putBoolean("notfirst", true)
                 .apply();

         String[] assetFiles = assets.list("");

         for (int i = 0; i < assetFiles.length; i++) {
            Log.i(logTag, "ListFiles[" + i + "] = " + assetFiles[i]);
         }

         // Restore internal save data
         if (ExistsInArray(assetFiles, "data.save")) {
            Toast.makeText(context, "Restoring save...", Toast.LENGTH_SHORT).show();
            try (InputStream inputStream = assets.open("data.save")) {
               String targetPath = context.getFilesDir().getPath(); // Safer internal path
               Log.i(logTag, "data.save: Restoring to " + targetPath);
               unZipIt(inputStream, targetPath);
               Log.i(logTag, "data.save: Successfully restored");
            } catch (Exception e) {
               Log.e(logTag, "data.save: Message: " + e.getMessage(), e);
               Toast.makeText(context, "Can't restore save", Toast.LENGTH_LONG).show();
            }
         }


         // Restore OBB cache
         if (ExistsInArray(assetFiles, "extobb.save")) {
            Toast.makeText(context, "Restoring cache...", Toast.LENGTH_SHORT).show();
            try (InputStream inputStream = assets.open("extobb.save")) {
               String targetPath = context.getObbDir().getAbsolutePath() + "/";
               Log.i(logTag, "extobb.save: Restoring...");
               unZipIt(inputStream, targetPath);
               Log.i(logTag, "extobb.save: Successfully restored");
            } catch (Exception e) {
               Log.e(logTag, "extobb.save: Message: " + e.getMessage(), e);
               Toast.makeText(context, "Can't restore external cache", Toast.LENGTH_LONG).show();
            }
         }

         // Restore external data
         if (ExistsInArray(assetFiles, "extdata.save")) {
            Toast.makeText(context, "Restoring external data...", Toast.LENGTH_SHORT).show();
            try (InputStream inputStream = assets.open("extdata.save")) {
               String targetPath = Environment.getExternalStorageDirectory() + "/Android/data/" + packageName + "/";
               new File(targetPath).mkdirs();
               Log.i(logTag, "extdata.save: Restoring...");
               unZipIt(inputStream, targetPath);
               Log.i(logTag, "extdata.save: Successfully restored");
            } catch (Exception e) {
               Log.e(logTag, "extdata.save: Message: " + e.getMessage(), e);
               Toast.makeText(context, "Can't restore external data", Toast.LENGTH_LONG).show();
            }
         }

         Log.i(logTag, "Restoring completed");
         Toast.makeText(context, "Restoring completed", Toast.LENGTH_LONG).show();
      }
   }

   private static void unZipIt(InputStream inputStream, String targetDirectory) throws Exception {
      try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
         byte[] buffer = new byte[1024];
         new File(targetDirectory).mkdirs();

         ZipEntry entry;
         while ((entry = zipInputStream.getNextEntry()) != null) {
            if (entry.isDirectory()) {
               continue;
            }

            File outFile = new File(targetDirectory, entry.getName());
            File parentDir = outFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
               parentDir.mkdirs();
            }

            try (FileOutputStream fileOut = new FileOutputStream(outFile)) {
               int length;
               while ((length = zipInputStream.read(buffer)) > 0) {
                  fileOut.write(buffer, 0, length);
               }
            }
         }
      }
   }
}
