import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.util.Log;
import com.ideaworks3d.marmalade.LoaderAPI;
import com.ideaworks3d.marmalade.event.RequestPermissionsResultEvent;
import com.ideaworks3d.marmalade.event.RequestPermissionsResultListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class s3ePermissions implements RequestPermissionsResultListener {
      private static final String TAG = "s3ePermissions";
      private static final int S3E_PERMISSION_GRANTED = 0;
      private static final int S3E_PERMISSION_DENIED = 1;
      private static final int S3E_PERMISSIONS_ERR_NO_PERMISSIONS_TO_REQUEST = 1;
      private s3ePermissions.PendingRequestsQueue pendingRequests = new s3ePermissions.PendingRequestsQueue();

      private static native void native_RequestPermissionsResultCallback(int var0, String[] var1, int[] var2);

      private static void notifyRequestPermissionsResult(int var0, String[] var1, int[] var2) {
            int[] var3 = new int[var2.length];

            for(int var4 = 0; var4 < var2.length; ++var4) {
                  var3[var4] = var2[var4] == 0 ? 0 : 1;
            }

            native_RequestPermissionsResultCallback(var0, var1, var3);
      }

      public void s3ePermissionsInit() {
            Log.i("s3ePermissions", "s3ePermissionsInit");
            LoaderAPI.addRequestPermissionsResultListener(this);
      }

      public void s3ePermissionsTerminate() {
            Log.i("s3ePermissions", "s3ePermissionsTerminate");
            LoaderAPI.removeRequestPermissionsResultListener(this);
      }

      public boolean s3ePermissionsIsGranted(String var1) {
            Log.i("s3ePermissions", "s3ePermissionsIsGranted");
            return getPermissionGrantedValue(var1) == 0;
      }

      private static int getPermissionGrantedValue(String var0) {
            if (VERSION.SDK_INT >= 23) {
                  return LoaderAPI.getActivity().checkSelfPermission(var0);
            } else {
                  Log.i("s3ePermissions", "Warning: getPermissionGrantedValue called in compatibility mode (app is not targeting api level 23)");
                  PackageManager var1 = LoaderAPI.getActivity().getPackageManager();
                  String var2 = LoaderAPI.getActivity().getPackageName();
                  return var1.checkPermission(var0, var2);
            }
      }

      public synchronized int s3ePermissionsRequest(String[] var1, int var2, boolean var3) {
            Log.i("s3ePermissions", "s3ePermissionsRequest requestId = " + var2);
            if (var1.length == 0) {
                  Log.i("s3ePermissions", "s3ePermissionsRequest - empty permissions request list");
                  return 1;
            } else if (VERSION.SDK_INT >= 23) {
                  Iterator var6 = this.pendingRequests.iterator();

                  while(var6.hasNext()) {
                        s3ePermissions.PendingRequest var7 = (s3ePermissions.PendingRequest)var6.next();
                        if (var7.requestId == var2) {
                              Log.i("s3ePermissions", "s3ePermissionsRequest - there is ongoing request with same id: " + var2);
                        }
                  }

                  this.pendingRequests.add(new s3ePermissions.PendingRequest(var2, var3, var1));
                  if (this.pendingRequests.size() == 1 && !this.pendingRequests.get(0).request()) {
                        this.pendingRequests.remove(0);
                  }

                  return 0;
            } else {
                  Log.i("s3ePermissions", "Warning: s3ePermissionsRequest called in compatibility mode (app is not targeting api level 23)");
                  int[] var4 = new int[var1.length];

                  for(int var5 = 0; var5 < var1.length; ++var5) {
                        var4[var5] = getPermissionGrantedValue(var1[var5]);
                  }

                  notifyRequestPermissionsResult(var2, var1, var4);
                  return 0;
            }
      }

      private s3ePermissions.PendingRequest removePendingRequestWithId(int var1) {
            Iterator var2 = this.pendingRequests.iterator();

            s3ePermissions.PendingRequest var3;
            do {
                  if (!var2.hasNext()) {
                        return null;
                  }

                  var3 = (s3ePermissions.PendingRequest)var2.next();
            } while(var3.requestId != var1);

            this.pendingRequests.remove(var3);
            return var3;
      }

      private void updatePendingRequests(RequestPermissionsResultEvent var1) {
            for(int var2 = 0; var2 < var1.m_permissions.length; ++var2) {
                  this.pendingRequests.updatePermission(var1.m_permissions[var2], var1.m_grantResults[var2]);
            }

      }

      public synchronized void onRequestPermissionsResultEvent(RequestPermissionsResultEvent var1) {
            Log.i("s3ePermissions", "Handling permissions request result, request code: " + var1.m_requestCode);

            for(int var2 = 0; var2 < var1.m_permissions.length; ++var2) {
                  Log.i("s3ePermissions", "Permission " + var1.m_permissions[var2] + " grantResult " + var1.m_grantResults[var2]);
            }

            s3ePermissions.PendingRequest var3 = this.removePendingRequestWithId(var1.m_requestCode);
            this.updatePendingRequests(var1);
            if (var3 == null) {
                  Log.i("s3ePermissions", "onRequestPermissionsResultEvent - not found pending request data!");
                  notifyRequestPermissionsResult(var1.m_requestCode, var1.m_permissions, var1.m_grantResults);
            }

            notifyRequestPermissionsResult(var1.m_requestCode, var3.getResPermissions(var1.m_permissions), var3.getResGrantResults(var1.m_grantResults));

            while(this.pendingRequests.size() > 0 && !this.pendingRequests.get(0).request()) {
                  this.pendingRequests.remove(0);
            }

      }

      public boolean s3ePermissionsShouldShowRequestPermissionRationale(String var1) {
            return VERSION.SDK_INT >= 23 ? LoaderAPI.getActivity().shouldShowRequestPermissionRationale(var1) : false;
      }

      public synchronized void s3ePermissionsNotifyPermissionsResultCleanup() {
            this.pendingRequests.cleanup();
      }

      private class PendingRequestsQueue implements Iterable {
            private List pendingRequests = new ArrayList();
            private List sessionRequestResults = new ArrayList();

            PendingRequestsQueue() {
            }

            public Iterator iterator() {
                  return this.pendingRequests.iterator();
            }

            boolean add(s3ePermissions.PendingRequest var1) {
                  Iterator var2 = this.sessionRequestResults.iterator();

                  while(var2.hasNext()) {
                        s3ePermissions.PermissionRequestResult var3 = (s3ePermissions.PermissionRequestResult)var2.next();
                        var1.updatePermission(var3.permission, var3.result);
                  }

                  return this.pendingRequests.add(var1);
            }

            s3ePermissions.PendingRequest get(int var1) {
                  return (s3ePermissions.PendingRequest)this.pendingRequests.get(var1);
            }

            int size() {
                  return this.pendingRequests.size();
            }

            s3ePermissions.PendingRequest remove(int var1) {
                  return (s3ePermissions.PendingRequest)this.pendingRequests.remove(var1);
            }

            boolean remove(Object var1) {
                  return this.pendingRequests.remove(var1);
            }

            void updatePermission(String var1, int var2) {
                  Iterator var3 = this.pendingRequests.iterator();

                  while(var3.hasNext()) {
                        s3ePermissions.PendingRequest var4 = (s3ePermissions.PendingRequest)var3.next();
                        var4.updatePermission(var1, var2);
                  }

                  this.sessionRequestResults.add(s3ePermissions.this.new PermissionRequestResult(var1, var2));
            }

            public void cleanup() {
                  if (this.pendingRequests.size() == 0) {
                        this.sessionRequestResults.clear();
                  }

            }
      }

      private class PendingRequest {
            public int requestId;
            private boolean skipGranted;
            public List permissions;
            public List skippedPermissions;

            public PendingRequest(int var2, boolean var3, String[] var4) {
                  this.requestId = var2;
                  this.skipGranted = var3;
                  this.permissions = new ArrayList(Arrays.asList(var4));
                  this.skippedPermissions = new ArrayList();
            }

            public void updatePermission(String var1, int var2) {
                  for(int var3 = this.permissions.size() - 1; var3 >= 0; --var3) {
                        if (((String)this.permissions.get(var3)).equals(var1)) {
                              this.permissions.remove(var3);
                              this.skippedPermissions.add(s3ePermissions.this.new PermissionRequestResult(var1, var2));
                        }
                  }

            }

            public String[] getResPermissions(String[] var1) {
                  if (var1 == null) {
                        var1 = new String[0];
                  } else if (var1.length == 0) {
                        var1 = (String[])this.permissions.toArray(new String[this.permissions.size()]);
                  }

                  int var2 = var1.length + this.skippedPermissions.size();
                  String[] var3 = new String[var2];

                  int var4;
                  for(var4 = 0; var4 < var1.length; ++var4) {
                        var3[var4] = var1[var4];
                  }

                  for(var4 = 0; var4 < this.skippedPermissions.size(); ++var4) {
                        var3[var1.length + var4] = ((s3ePermissions.PermissionRequestResult)this.skippedPermissions.get(var4)).permission;
                  }

                  return var3;
            }

            public int[] getResGrantResults(int[] var1) {
                  int var2;
                  if (var1 == null) {
                        var1 = new int[0];
                  } else if (var1.length == 0) {
                        var1 = new int[this.permissions.size()];

                        for(var2 = 0; var2 < this.permissions.size(); ++var2) {
                              var1[var2] = s3ePermissions.getPermissionGrantedValue((String)this.permissions.get(var2));
                        }
                  }

                  var2 = var1.length + this.skippedPermissions.size();
                  int[] var3 = new int[var2];

                  int var4;
                  for(var4 = 0; var4 < var1.length; ++var4) {
                        var3[var4] = var1[var4];
                  }

                  for(var4 = 0; var4 < this.skippedPermissions.size(); ++var4) {
                        if (!((s3ePermissions.PermissionRequestResult)this.skippedPermissions.get(var4)).isSet()) {
                              Log.i("s3ePermissions", "Warning: permission " + ((s3ePermissions.PermissionRequestResult)this.skippedPermissions.get(var4)).permission + " is expected to be set as part of another request.");
                        }

                        var3[var1.length + var4] = ((s3ePermissions.PermissionRequestResult)this.skippedPermissions.get(var4)).isSet() ? ((s3ePermissions.PermissionRequestResult)this.skippedPermissions.get(var4)).result : s3ePermissions.getPermissionGrantedValue(((s3ePermissions.PermissionRequestResult)this.skippedPermissions.get(var4)).permission);
                  }

                  return var3;
            }

            public boolean request() {
                  Log.i("s3ePermissions", "Executing permissions request with id: " + this.requestId);

                  for(int var1 = this.permissions.size() - 1; var1 >= 0; --var1) {
                        if (this.skipGranted && s3ePermissions.getPermissionGrantedValue((String)this.permissions.get(var1)) == 0) {
                              Log.i("s3ePermissions", "Skipping permission check (already granted): " + (String)this.permissions.get(var1));
                              this.skippedPermissions.add(s3ePermissions.this.new PermissionRequestResult((String)this.permissions.get(var1), 0));
                              this.permissions.remove(var1);
                        }
                  }

                  if (this.permissions.size() == 0) {
                        s3ePermissions.notifyRequestPermissionsResult(this.requestId, this.getResPermissions((String[])null), this.getResGrantResults((int[])null));
                        return false;
                  } else {
                        if (VERSION.SDK_INT >= 23) {
                              LoaderAPI.getActivity().requestPermissions((String[])this.permissions.toArray(new String[this.permissions.size()]), this.requestId);
                        } else {
                              Log.i("s3ePermissions", "Warning: Attempt to request permissions on API level < 23");
                        }

                        return true;
                  }
            }
      }

      private class PermissionRequestResult {
            public String permission;
            public int result;
            private boolean isResultSet = false;

            public PermissionRequestResult(String var2) {
                  this.permission = var2;
            }

            public PermissionRequestResult(String var2, int var3) {
                  this.permission = var2;
                  this.setResult(var3);
            }

            public void setResult(int var1) {
                  this.result = var1;
                  this.isResultSet = true;
            }

            public boolean isSet() {
                  return this.isResultSet;
            }
      }
}
