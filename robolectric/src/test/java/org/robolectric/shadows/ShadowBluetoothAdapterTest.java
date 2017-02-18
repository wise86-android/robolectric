package org.robolectric.shadows;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.TestRunners;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(TestRunners.MultiApiSelfTest.class)
public class ShadowBluetoothAdapterTest {
  private BluetoothAdapter bluetoothAdapter;
  private ShadowBluetoothAdapter shadowBluetoothAdapter;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    bluetoothAdapter = Shadow.newInstanceOf(BluetoothAdapter.class);
    shadowBluetoothAdapter = shadowOf(bluetoothAdapter);
  }

  @Test
  public void testAdapterDefaultsDisabled() {
    assertThat(bluetoothAdapter.isEnabled()).isFalse();
  }

  @Test
  public void testAdapterCanBeEnabled_forTesting() {
    shadowBluetoothAdapter.setEnabled(true);
    assertThat(bluetoothAdapter.isEnabled()).isTrue();
  }

  @Test
  public void canGetAndSetAddress() throws Exception {
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    shadowOf(adapter).setAddress("expected");
    assertThat(adapter.getAddress()).isEqualTo("expected");
  }

  @Test
  public void canEnable_withAndroidApi() throws Exception {
    bluetoothAdapter.enable();
    assertThat(bluetoothAdapter.isEnabled()).isTrue();
  }

  @Test
  public void canDisable_withAndroidApi() throws Exception {
    shadowBluetoothAdapter.setEnabled(true);
    bluetoothAdapter.disable();
    assertThat(bluetoothAdapter.isEnabled()).isFalse();
  }

  @Test
  @Config(minSdk = JELLY_BEAN_MR2)
  public void testLeScan() {
    BluetoothAdapter.LeScanCallback callback1 = newLeScanCallback();
    BluetoothAdapter.LeScanCallback callback2 = newLeScanCallback();

    bluetoothAdapter.startLeScan(callback1);
    assertThat(shadowBluetoothAdapter.getLeScanCallbacks()).containsOnly(callback1);
    bluetoothAdapter.startLeScan(callback2);
    assertThat(shadowBluetoothAdapter.getLeScanCallbacks()).containsOnly(callback1, callback2);

    bluetoothAdapter.stopLeScan(callback1);
    assertThat(shadowBluetoothAdapter.getLeScanCallbacks()).containsOnly(callback2);
    bluetoothAdapter.stopLeScan(callback2);
    assertThat(shadowBluetoothAdapter.getLeScanCallbacks()).isEmpty();
  }

  @Test
  @Config(minSdk = JELLY_BEAN_MR2)
  public void testGetSingleLeScanCallback() {
    BluetoothAdapter.LeScanCallback callback1 = newLeScanCallback();
    BluetoothAdapter.LeScanCallback callback2 = newLeScanCallback();

    bluetoothAdapter.startLeScan(callback1);
    assertThat(shadowBluetoothAdapter.getSingleLeScanCallback()).isEqualTo(callback1);

    bluetoothAdapter.startLeScan(callback2);
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("There are 2 callbacks");
    shadowBluetoothAdapter.getSingleLeScanCallback();
  }

  @Test
  @Config(minSdk = JELLY_BEAN_MR2)
  public void testLeCallbackIsCalledWhenDeviceIsDiscovered() {

    BluetoothAdapter.LeScanCallback callback = mock(BluetoothAdapter.LeScanCallback.class);

    bluetoothAdapter.startLeScan(callback);

    final BluetoothDevice fakeDevice = Shadow.newInstanceOf(BluetoothDevice.class);
    final int deviceRssi = 123;
    final byte advertise[] = {0x00,0x01,0x02,0x03,0x04};
    shadowBluetoothAdapter.discoverDevice(fakeDevice,deviceRssi,advertise);

    verify(callback).onLeScan(fakeDevice,deviceRssi,advertise);

  }


    private BluetoothAdapter.LeScanCallback newLeScanCallback() {
    return new BluetoothAdapter.LeScanCallback() {
      @Override
      public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {}
    };
  }
}
