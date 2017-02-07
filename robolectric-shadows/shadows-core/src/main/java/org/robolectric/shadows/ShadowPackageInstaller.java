package org.robolectric.shadows;

import android.annotation.NonNull;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.io.IOException;
import java.io.OutputStream;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

@Implements(value = PackageInstaller.class, minSdk = LOLLIPOP)
public class ShadowPackageInstaller {

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
