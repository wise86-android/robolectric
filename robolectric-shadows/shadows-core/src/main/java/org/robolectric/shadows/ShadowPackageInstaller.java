package org.robolectric.shadows;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.IPackageInstaller;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.os.Handler;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

@Implements(value = PackageInstaller.class, minSdk = LOLLIPOP)
public class ShadowPackageInstaller {

  private int nextSessionId;
  private Map<Integer, PackageInstaller.SessionInfo> sessionInfos = new HashMap<>();
  private Set<CallbackInfo> callbackInfos = new HashSet<>();

  private class CallbackInfo {
    PackageInstaller.SessionCallback callback;
    Handler handler;
  }

  @Implementation
  public List<PackageInstaller.SessionInfo> getAllSessions() {
    return ImmutableList.copyOf(sessionInfos.values());
  }

  @Implementation
  public void registerSessionCallback(@NonNull PackageInstaller.SessionCallback callback, @NonNull Handler handler) {
    CallbackInfo callbackInfo = new CallbackInfo();
    callbackInfo.callback = callback;
    callbackInfo.handler = handler;
    this.callbackInfos.add(callbackInfo);
  }

  @Implementation
  @Nullable
  public PackageInstaller.SessionInfo getSessionInfo(int sessionId) {
    return sessionInfos.get(sessionId);
  }

  @Implementation
  public int createSession(@NonNull PackageInstaller.SessionParams params) throws IOException {
    final PackageInstaller.SessionInfo sessionInfo = new PackageInstaller.SessionInfo();
    sessionInfo.sessionId = nextSessionId++;
    sessionInfo.appPackageName = params.appPackageName;
    sessionInfos.put(sessionInfo.getSessionId(), sessionInfo);

    for (final CallbackInfo callbackInfo : callbackInfos) {
      callbackInfo.handler.post(new Runnable() {
        @Override
        public void run() {
          callbackInfo.callback.onCreated(sessionInfo.sessionId);
        }
      });
    }

    return sessionInfo.sessionId;
  }

  @Implementation
  public void abandonSession(int sessionId) {
    sessionInfos.remove(sessionId);
  }

  @Implementation
  @NonNull
  public PackageInstaller.Session openSession(int sessionId) throws IOException {
    if (!sessionInfos.containsKey(sessionId)) {
      throw new SecurityException("Invalid session Id: " + sessionId);
    }

    return new PackageInstaller.Session(null);
  }

  private Function<String, PackageInstaller.SessionInfo> packageNameToSessionInfo() {
    return new Function<String, PackageInstaller.SessionInfo>() {
      @Override
      public PackageInstaller.SessionInfo apply(String packageName) {
        PackageInstaller.SessionInfo sessionInfo = new PackageInstaller.SessionInfo();
        sessionInfo.appPackageName = packageName;
        return sessionInfo;
      }
    };
  }


  @Implements(value = PackageInstaller.Session.class, minSdk = LOLLIPOP)
  public static class ShadowSession {

    private OutputStream outputStream;
    private boolean outputStreamOpen;

    @Implementation
    public @NonNull OutputStream openWrite(@NonNull String name, long offsetBytes, long lengthBytes) throws IOException {
      outputStream = new OutputStream() {
        @Override
        public void write(int aByte) throws IOException {

        }

        @Override
        public void close() throws IOException {
          outputStreamOpen = false;
        }
      };
      outputStreamOpen = true;
      return outputStream;
    }

    @Implementation
    public void commit(@NonNull IntentSender statusReceiver) {
      if (outputStreamOpen) {
        throw new SecurityException("OutputStream still open");
      }
    }
  }
}
