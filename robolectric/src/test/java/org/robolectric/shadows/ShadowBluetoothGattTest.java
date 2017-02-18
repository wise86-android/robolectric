package org.robolectric.shadows;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

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
@Config(minSdk = JELLY_BEAN_MR2)
public class ShadowBluetoothGattTest {

    private BluetoothGatt bluetoothGatt;
    private ShadowBluetoothGatt shadowBluetoothGatt;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        bluetoothGatt = Shadow.newInstanceOf(BluetoothGatt.class);
        shadowBluetoothGatt = shadowOf(bluetoothGatt);
    }


    @Test
    public void getDeviceReturnTheSetDevices(){
        BluetoothDevice device = mock(BluetoothDevice.class);
        shadowBluetoothGatt.setDevice(device);
        assertThat(bluetoothGatt.getDevice()).isEqualTo(device);
    }

    @Test
    public void connectCallTheConnectionWithConnectingState(){
        BluetoothGattCallback callback = mock(BluetoothGattCallback.class);

        shadowBluetoothGatt.setCallback(callback);

        bluetoothGatt.connect();

        verify(callback).onConnectionStateChange(bluetoothGatt,BluetoothGatt.GATT_SUCCESS,
                BluetoothGatt.STATE_CONNECTING);
        verify(callback).onConnectionStateChange(bluetoothGatt,BluetoothGatt.GATT_SUCCESS,
                BluetoothGatt.STATE_CONNECTED);
    }

    @Test
    public void disconnectCallTheConnectionWithDisconnectingState(){
        BluetoothGattCallback callback = mock(BluetoothGattCallback.class);

        shadowBluetoothGatt.setCallback(callback);

        bluetoothGatt.disconnect();

        verify(callback).onConnectionStateChange(bluetoothGatt,BluetoothGatt.GATT_SUCCESS,
                BluetoothGatt.STATE_DISCONNECTING);
        verify(callback).onConnectionStateChange(bluetoothGatt,BluetoothGatt.GATT_SUCCESS,
                BluetoothGatt.STATE_DISCONNECTED);
    }


    @Test
    public void discoverServiceIsEmptyIfNotScanned(){
        assertThat(bluetoothGatt.getServices()).isEmpty();
    }

    @Test
    public void discoverServiceGiveCallback(){
        BluetoothGattCallback callback = mock(BluetoothGattCallback.class);

        shadowBluetoothGatt.setCallback(callback);

        bluetoothGatt.discoverServices();

        verify(callback).onServicesDiscovered(bluetoothGatt,BluetoothGatt.GATT_SUCCESS);
    }
}
