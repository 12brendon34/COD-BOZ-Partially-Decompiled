package defpackage;

import com.ideaworks3d.marmalade.LoaderAPI;
import com.ideaworks3d.marmalade.LoaderActivity;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
/* renamed from: IsIOSUtils  reason: default package */
/* loaded from: classes.dex */
public class IsIOSUtils {
    public static HashMap<String, KeychainObject> kobjMap = new HashMap<>();
    static boolean initKobjMap = true;
    public static String fileName = ".temp.bin";

    public static void IsInstallExceptionHandler() {
    }

    public static void IsUninstallExceptionHandler() {
    }

    public boolean Test() {
        return true;
    }

    public static void IsInstallMemoryWarningHandler(String str, String str2, boolean z) {
    }

    public static void IsUninstallMemoryWarningHandler() {
    }

    public static void IsSimulateMemoryWarning() {
    }

    public static void IsOSMalloc(int i) {
    }

    public static void IsOSFree() {
    }

    public static void IsOSRealloc(int i) {
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public static boolean IsKeychainInit(String str) throws IOException {
        boolean z;
        FileInputStream openFileInput = null;
        byte[] bArr = new byte[0];
        int read = 0;
        LoaderActivity activity = LoaderAPI.getActivity();
        if (initKobjMap) {
            try {
                openFileInput = activity.openFileInput(fileName);
                bArr = new byte[65535];
                read = openFileInput.read(bArr);
            } catch (FileNotFoundException e) {
                z = true;
            } catch (IOException e2) {
                return false;
            }
            if (read == -1) {
                return false;
            }
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            int i = 0;
            do {
                KeychainObject keychainObject = new KeychainObject();
                String str2 = "";
                while (true) {
                    int i2 = i;
                    String str3 = str2;
                    char c = wrap.getChar();
                    str2 = c != '#' ? str3 + c : str3;
                    i = i2 + 2;
                    if (c == '#') {
                        break;
                    }
                }
                keychainObject.crc = wrap.getInt();
                i += 4;
                keychainObject.valid = true;
                kobjMap.put(str2, keychainObject);
            } while (i < read);
            openFileInput.close();
            z = false;
            initKobjMap = false;
        } else {
            z = false;
        }
        if (z) {
            try {
                activity.openFileOutput(fileName, 0).close();
            } catch (IOException e3) {
                return false;
            }
        }
        return true;
    }

    public static void IsKeychainTerminate() {
        kobjMap.clear();
    }

    public static boolean IsKeychainSearchKey(String str) {
        if (kobjMap.containsKey(str)) {
            KeychainObject keychainObject = kobjMap.get(str);
            return keychainObject.valid && keychainObject.crc != -1;
        }
        return false;
    }

    public static int IsKeychainReadKey(String str) {
        if (kobjMap.containsKey(str)) {
            KeychainObject keychainObject = kobjMap.get(str);
            if (!keychainObject.valid || keychainObject.crc == -1) {
                return 0;
            }
            return keychainObject.crc;
        }
        return 0;
    }

    public static boolean IsKeychainWriteKey(String str, String str2) {
        int parseInt = 0;
        if (!kobjMap.containsKey(str)) {
            KeychainObject keychainObject = new KeychainObject();
            keychainObject.valid = true;
            keychainObject.crc = -1;
            kobjMap.put(str, keychainObject);
        }
        try {
            parseInt = Integer.parseInt(str2);
        } catch (Exception e) {
        }
        if (kobjMap.get(str).crc == parseInt) {
            return true;
        }
        kobjMap.get(str).crc = parseInt;
        try {
            FileOutputStream openFileOutput = LoaderAPI.getActivity().openFileOutput(fileName, 0);
            for (Map.Entry<String, KeychainObject> entry : kobjMap.entrySet()) {
                String key = entry.getKey();
                KeychainObject value = entry.getValue();
                ByteBuffer allocate = ByteBuffer.allocate((key.length() * 2) + 2 + 4);
                char[] charArray = key.toCharArray();
                for (char c : charArray) {
                    allocate.putChar(c);
                }
                allocate.putChar('#');
                allocate.putInt(value.crc);
                allocate.rewind();
                openFileOutput.write(allocate.array());
            }
            openFileOutput.close();
            return true;
        } catch (FileNotFoundException e2) {
            return false;
        } catch (IOException e3) {
            return false;
        }
    }
}
