package org.robolectric.shadows;

import android.bluetooth.BluetoothDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.TestRunners;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_DUAL;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_UNKNOWN;
import static android.os.Build.VERSION_CODES.ECLAIR;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(TestRunners.MultiApiSelfTest.class)
public class ShadowBluetoothDeviceTest {

    private BluetoothDevice bluetoothDevice;
    private ShadowBluetoothDevice shadowBluetoothDevice;

    @Before
    public void setUp() throws Exception {
        bluetoothDevice = Shadow.newInstanceOf(BluetoothDevice.class);
        shadowBluetoothDevice = shadowOf(bluetoothDevice);
    }

    @Test
    @Config(minSdk = ECLAIR)
    public void testDeviceHasName() {
        final String deviceName = "Device_name";
        shadowBluetoothDevice.setName(deviceName);
        assertThat(bluetoothDevice.getName()).isEqualTo(deviceName);
    }

    @Test
    @Config(minSdk = ECLAIR)
    public void testDeviceHasAddress() {
        final String deviceAddress = "AA:BB:CC:DD:EE:FF";
        shadowBluetoothDevice.setAddress(deviceAddress);
        assertThat(bluetoothDevice.getAddress()).isEqualTo(deviceAddress);
    }

    @Test
    @Config(minSdk = JELLY_BEAN_MR2)
    public void testDeviceHasDefaultUnknowType() {
        assertThat(bluetoothDevice.getType()).isEqualTo(DEVICE_TYPE_UNKNOWN);
    }

    @Test
    @Config(minSdk = JELLY_BEAN_MR2)
    public void testDeviceHasType() {
        final int deviceType = DEVICE_TYPE_DUAL;

        assertThat(bluetoothDevice.getType()).isNotEqualTo(deviceType);
        shadowBluetoothDevice.setDeviceType(deviceType);
        assertThat(bluetoothDevice.getType()).isEqualTo(deviceType);
    }

    @Test
    @Config(minSdk = JELLY_BEAN_MR2)
    public void testDeviceWithInvalidTypeHasUnknowType() {
        final int deviceType = Integer.MAX_VALUE;

        assertThat(bluetoothDevice.getType()).isEqualTo(DEVICE_TYPE_UNKNOWN);
        shadowBluetoothDevice.setDeviceType(deviceType);
        assertThat(bluetoothDevice.getType()).isEqualTo(DEVICE_TYPE_UNKNOWN);
    }
}
