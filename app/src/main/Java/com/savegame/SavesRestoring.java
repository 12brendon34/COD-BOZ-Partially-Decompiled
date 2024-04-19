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
   private static int PdsjdolaSd = 0;
   private static int daDakdsIID = 0;

   private static String BXPwSmxuPW() {
      ++daDakdsIID;
      return Character.toString('m');
   }

   public static void DoSmth(Context var0) {
      try {
         SmartDataRestoreForYou(var0, var0.getAssets(), var0.getPackageName());
      } catch (Exception var2) {
         Log.e(var0.getPackageName() + ":savemessages", "Message: " + var2.getMessage());
         var2.printStackTrace();
      }

   }

   private static String EOYFnuReNsHy() {
      ++daDakdsIID;
      return Character.toString('u');
   }

   public static boolean ExistsInArray(String[] var0, String var1) {
      int var2 = 0;

      boolean var3;
      while(true) {
         if (var2 >= var0.length) {
            var3 = false;
            break;
         }

         if (var0[var2].contains(var1)) {
            var3 = true;
            break;
         }

         ++var2;
      }

      return var3;
   }

   private static String JpkKyIAiknMv() {
      ++daDakdsIID;
      return Character.toString('o');
   }

   private static void SmartDataRestoreForYou(Context var0, AssetManager var1, String var2) throws Exception {
      if (!var0.getSharedPreferences("savegame", 0).getBoolean("notfirst", false)) {
         var0.getSharedPreferences("savegame", 0).edit().putBoolean("notfirst", true).commit();
         var2 = var2 + ":savemessages";
         Log.i(var2, "SmDR: Starting...");
         var0.getSharedPreferences("savegame", 0).edit().putBoolean("notfirst", true).commit();
         String[] var4 = var1.list("");

         for(int var3 = 0; var3 < var4.length; ++var3) {
            Log.i(var2, "ListFiles[" + var3 + "] = " + var4[var3]);
         }

         StringBuilder var5;
         if (ExistsInArray(var4, "data.save")) {
            Toast.makeText(var0, "Restoring save...", 0).show();

            try {
               Log.i(var2, "data.save : Restoring...");
               InputStream var6 = var1.open("data.save");
               var5 = new StringBuilder();
               unZipIt(var6, var5.append("/data/data/").append(var0.getPackageName()).toString());
               Log.i(var2, "data.save: Successfully restored");
            } catch (Exception var9) {
               Log.e(var2, "data.save: Message: " + var9.getMessage());
               Toast.makeText(var0, "Can't restore save", 1).show();
            }
         }

         String var12;
         if (ExistsInArray(var4, "extobb.save")) {
            Toast.makeText(var0, "Restoring cache...", 0).show();

            try {
               Log.i(var2, "extobb.save: Restoring...");
               var5 = new StringBuilder();
               var12 = var5.append(var0.getObbDir().getAbsolutePath()).append("/").toString();
               unZipIt(var1.open("extobb.save"), var12);
               Log.i(var2, "extobb.save: Successfully restored");
            } catch (Exception var8) {
               Log.e(var2, "extobb.save: Message: " + var8.getMessage());
               Toast.makeText(var0, "Can't restore external cache", 1).show();
            }
         }

         if (ExistsInArray(var4, "extdata.save")) {
            Toast.makeText(var0, "Restoring external data...", 0).show();

            try {
               Log.i(var2, "extdata.save: Restoring...");
               StringBuilder var10 = new StringBuilder();
               var12 = var10.append(Environment.getExternalStorageDirectory()).append("/Android/data/").append(var0.getPackageName()).append("/").toString();
               File var11 = new File(var12);
               var11.mkdirs();
               unZipIt(var1.open("extdata.save"), var12);
               Log.i(var2, "extdata.save: Successfully restored");
            } catch (Exception var7) {
               Log.e(var2, "extdata.save: Message: " + var7.getMessage());
               Toast.makeText(var0, "Can't restore external data", 1).show();
            }
         }

         Log.i(var2, "Restoring completed");
         Toast.makeText(var0, "Restoring completed", 1).show();
      }

   }

   private static String TpYcPKxx() {
      ++daDakdsIID;
      return Character.toString('t');
   }

   private static String bcNCqJaDmGpRY() {
      ++daDakdsIID;
      return Character.toString('o');
   }

   private static String dHJUXuApj() {
      ++daDakdsIID;
      return Character.toString('B');
   }

   private static String eiLlYWJly() {
      ++daDakdsIID;
      return Character.toString('k');
   }

   private static String fyjHuHkW() {
      ++daDakdsIID;
      return Character.toString('u');
   }

   private static String jrOjGYk() {
      ++daDakdsIID;
      return Character.toString('k');
   }

   private static String oxkjAWcOXTL() {
      ++daDakdsIID;
      return Character.toString('a');
   }

   private static String sBklbFcSb() {
      ++daDakdsIID;
      return Character.toString(' ');
   }

   private static String sNVNqe() {
      ++daDakdsIID;
      return Character.toString('y');
   }

   private static void unZipIt(InputStream var0, String var1) throws Exception {
      ZipInputStream var4 = new ZipInputStream(var0);
      if (daDakdsIID != PdsjdolaSd) {
         throw new Exception("System error...");
      } else {
         byte[] var5 = new byte[1024];
         (new File(var1)).mkdirs();
         ZipEntry var6 = var4.getNextEntry();
         if (daDakdsIID != PdsjdolaSd) {
            throw new Exception("System error! please don't cheat...");
         } else {
            while(true) {
               while(var6 != null) {
                  if (var6.isDirectory()) {
                     var6 = var4.getNextEntry();
                  } else {
                     int var3 = var6.getName().lastIndexOf(47);
                     int var2 = var3;
                     if (var3 < 0) {
                        var2 = 0;
                     }

                     (new File(var1 + "/" + var6.getName().substring(0, var2))).mkdirs();
                     FileOutputStream var7 = new FileOutputStream(new File(var1 + "/" + var6.getName()), false);
                     if (daDakdsIID != PdsjdolaSd) {
                        var7.close();
                        throw new Exception("You are clever...");
                     }

                     while(true) {
                        var2 = var4.read(var5);
                        if (var2 <= 0) {
                           var7.close();
                           var6 = var4.getNextEntry();
                           break;
                        }

                        var7.write(var5, 0, var2);
                     }
                  }
               }

               if (daDakdsIID != PdsjdolaSd) {
                  throw new Exception("And again...");
               }

               var4.closeEntry();
               var4.close();
               return;
            }
         }
      }
   }

   private static void wPdauIdcaW(Context var0) {
   }

   private static void wPdauIdcaW(Context var0, int var1) {
   }
}
