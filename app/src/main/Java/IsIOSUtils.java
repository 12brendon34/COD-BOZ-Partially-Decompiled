import com.ideaworks3d.marmalade.LoaderAPI;
import com.ideaworks3d.marmalade.LoaderActivity;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IsIOSUtils {
   public static String fileName = ".temp.bin";
   static boolean initKobjMap = true;
   public static HashMap kobjMap = new HashMap();

   public static void IsInstallExceptionHandler() {
   }

   public static void IsInstallMemoryWarningHandler(String var0, String var1, boolean var2) {
   }

   public static boolean IsKeychainInit(String var0) {
      boolean var5 = false;
      LoaderActivity var6 = LoaderAPI.getActivity();
      boolean var2;
      if (initKobjMap) {
         label109: {
            label121: {
               label116: {
                  int var4;
                  FileInputStream var7;
                  boolean var10001;
                  byte[] var28;
                  try {
                     var7 = var6.openFileInput(fileName);
                     var28 = new byte['\uffff'];
                     var4 = var7.read(var28);
                  } catch (FileNotFoundException var26) {
                     var10001 = false;
                     break label116;
                  } catch (IOException var27) {
                     var10001 = false;
                     return var5;
                  }

                  if (var4 == -1) {
                     return var5;
                  }

                  ByteBuffer var9;
                  var9 = ByteBuffer.wrap(var28);

                  int var29 = 0;

                  label98:
                  while(true) {
                     KeychainObject var8;
                     var8 = new KeychainObject();

                     var0 = "";

                     char var1;
                     do {
                        var1 = var9.getChar();

                        if (var1 != '#') {
                           StringBuilder var10 = new StringBuilder();
                           var0 = var10.append(var0).append(var1).toString();
                        }

                        var29 += 2;
                     } while(var1 != '#');

                     var8.crc = var9.getInt();

                     int var3 = var29 + 4;

                     var8.valid = true;
                     kobjMap.put(var0, var8);

                     var29 = var3;
                     if (var3 >= var4) {
                        try {
                           var7.close();
                           break label121;
                        } catch (FileNotFoundException var12) {
                           var10001 = false;
                           break;
                        } catch (IOException var13) {
                           var10001 = false;
                           return var5;
                        }
                     }
                  }
               }

               var2 = true;
               break label109;
            }

            var2 = false;
         }

         initKobjMap = false;
      } else {
         var2 = false;
      }

      if (var2) {
         try {
            var6.openFileOutput(fileName, 0).close();
         } catch (IOException var11) {
            return var5;
         }
      }

      var5 = true;
      return var5;
   }

   public static int IsKeychainReadKey(String var0) {
      int var1;
      if (!kobjMap.containsKey(var0)) {
         var1 = 0;
      } else {
         KeychainObject var2 = (KeychainObject)kobjMap.get(var0);
         if (var2.valid && var2.crc != -1) {
            var1 = var2.crc;
         } else {
            var1 = 0;
         }
      }

      return var1;
   }

   public static boolean IsKeychainSearchKey(String var0) {
      boolean var1;
      if (!kobjMap.containsKey(var0)) {
         var1 = false;
      } else {
         KeychainObject var2 = (KeychainObject)kobjMap.get(var0);
         if (var2.valid && var2.crc != -1) {
            var1 = true;
         } else {
            var1 = false;
         }
      }

      return var1;
   }

   public static void IsKeychainTerminate() {
      kobjMap.clear();
   }

   public static boolean IsKeychainWriteKey(String var0, String var1) {
      KeychainObject var4;
      if (!kobjMap.containsKey(var0)) {
         var4 = new KeychainObject();
         var4.valid = true;
         var4.crc = -1;
         kobjMap.put(var0, var4);
      }

      boolean var3;
      label108: {
         boolean var10001;
         int var2;
         label88: {
            try {
               var2 = Integer.parseInt(var1);
               if (((KeychainObject)kobjMap.get(var0)).crc == var2) {
                  break label108;
               }
            } catch (Exception var18) {
               var10001 = false;
               break label88;
            }

            try {
               ((KeychainObject)kobjMap.get(var0)).crc = var2;
            } catch (Exception var17) {
               var10001 = false;
            }
         }

         label109: {
            label110: {
               label76: {
                  FileOutputStream var19;
                  Iterator var20;
                  try {
                     var19 = LoaderAPI.getActivity().openFileOutput(fileName, 0);
                     var20 = kobjMap.entrySet().iterator();
                  } catch (FileNotFoundException var11) {
                     var10001 = false;
                     break label110;
                  } catch (IOException var12) {
                     var10001 = false;
                     break label76;
                  }

                  label75:
                  while(true) {
                     ByteBuffer var6;
                     char[] var22;
                     label73: {
                        if (var20.hasNext()) {
                           Map.Entry var21 = (Map.Entry)var20.next();
                           String var5 = (String)var21.getKey();
                           var4 = (KeychainObject)var21.getValue();
                           var6 = ByteBuffer.allocate(var5.length() * 2 + 2 + 4);
                           var22 = var5.toCharArray();
                           break label73;
                        }

                        try {
                           var19.close();
                           break label109;
                        } catch (FileNotFoundException var7) {
                           var10001 = false;
                           break label110;
                        } catch (IOException var8) {
                           var10001 = false;
                           break;
                        }
                     }

                     var2 = 0;

                     while(true) {
                        if (var2 >= var22.length) {
                           break;
                        }

                        var6.putChar(var22[var2]);

                        ++var2;
                     }

                     try {
                        var6.putChar('#');
                        var6.putInt(var4.crc);
                        var6.rewind();
                        var19.write(var6.array());
                     } catch (FileNotFoundException var9) {
                        var10001 = false;
                        break label110;
                     } catch (IOException var10) {
                        var10001 = false;
                        break;
                     }
                  }
               }

               var3 = false;
               return var3;
            }

            var3 = false;
            return var3;
         }

         var3 = true;
         return var3;
      }

      var3 = true;
      return var3;
   }

   public static void IsOSFree() {
   }

   public static void IsOSMalloc(int var0) {
   }

   public static void IsOSRealloc(int var0) {
   }

   public static void IsSimulateMemoryWarning() {
   }

   public static void IsUninstallExceptionHandler() {
   }

   public static void IsUninstallMemoryWarningHandler() {
   }

   public boolean Test() {
      return true;
   }

   protected void finalize() throws Throwable {
      super.finalize();
   }
}
