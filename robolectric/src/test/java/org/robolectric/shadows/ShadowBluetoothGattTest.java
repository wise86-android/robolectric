package org.robolectric.shadows;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.TestRunners;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(TestRunners.MultiApiSelfTest.class)
@Config(minSdk = JELLY_BEAN_MR2)
public class ShadowBluetoothGattTest {

    private BluetoothGatt bluetoothGatt;
    private ShadowBluetoothGatt shadowBluetoothGatt;
    private BluetoothGattCallback mCallback;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        bluetoothGatt = Shadow.newInstanceOf(BluetoothGatt.class);
        shadowBluetoothGatt = shadowOf(bluetoothGatt);
        
        mCallback = mock(BluetoothGattCallback.class);
        shadowBluetoothGatt.setCallback(mCallback);

    }

    private static void runBacgroundTask(){
        ShadowApplication.getInstance().getBackgroundThreadScheduler().advanceToLastPostedRunnable();
    }

    @Test
    public void getDeviceReturnTheSetDevices(){
        BluetoothDevice device = mock(BluetoothDevice.class);
        shadowBluetoothGatt.setDevice(device);
        assertThat(bluetoothGatt.getDevice()).isEqualTo(device);
    }

    @Test
    public void connectCallTheConnectionWithConnectingState(){

        bluetoothGatt.connect();

        runBacgroundTask();

        verify(mCallback).onConnectionStateChange(bluetoothGatt,BluetoothGatt.GATT_SUCCESS,
                BluetoothGatt.STATE_CONNECTING);
        verify(mCallback).onConnectionStateChange(bluetoothGatt,BluetoothGatt.GATT_SUCCESS,
                BluetoothGatt.STATE_CONNECTED);
    }

    @Test
    public void disconnectCallTheConnectionWithDisconnectingState(){

        connectNode(bluetoothGatt);
        bluetoothGatt.disconnect();
        runBacgroundTask();
        verify(mCallback).onConnectionStateChange(bluetoothGatt,BluetoothGatt.GATT_SUCCESS,
                BluetoothGatt.STATE_DISCONNECTING);
        verify(mCallback).onConnectionStateChange(bluetoothGatt,BluetoothGatt.GATT_SUCCESS,
                BluetoothGatt.STATE_DISCONNECTED);
    }

    @Test
    public void discoverServiceIsEmptyIfNotScanned(){
        assertThat(bluetoothGatt.getServices()).isEmpty();
    }

    @Test
    public void discoverServiceGiveCallback(){

        List<BluetoothGattService> services = new ArrayList<>(2);
        services.add(mock(BluetoothGattService.class));
        services.add(mock(BluetoothGattService.class));

        shadowBluetoothGatt.setDeviceServices(services);

        connectNode(bluetoothGatt);

        bluetoothGatt.discoverServices();

        runBacgroundTask();

        verify(mCallback).onServicesDiscovered(bluetoothGatt,BluetoothGatt.GATT_SUCCESS);

        List<BluetoothGattService> discoverService= bluetoothGatt.getServices();

        Assert.assertEquals(services.size(),discoverService.size());
        Assert.assertEquals(services.get(0),discoverService.get(0));
        Assert.assertEquals(services.get(1),discoverService.get(1));
    }

    @Test
    public void discoverASpecificServiceReturnNullIfNotDiscover(){

        List<BluetoothGattService> services = new ArrayList<>(2);

        UUID targetService = UUID.fromString("c24e3528-ef45-4513-a4b9-8cb9719a473c");
        UUID otherService = UUID.fromString("cdc54dea-de7c-43f7-94f3-0d6131fd016a");

        services.add(new BluetoothGattService(targetService,BluetoothGattService.SERVICE_TYPE_PRIMARY));
        services.add(new BluetoothGattService(otherService,BluetoothGattService.SERVICE_TYPE_PRIMARY));

        shadowBluetoothGatt.setDeviceServices(services);

        Assert.assertNull(bluetoothGatt.getService(targetService));

        connectNode(bluetoothGatt);
        bluetoothGatt.discoverServices();

        runBacgroundTask();

        Assert.assertEquals(services.get(0),bluetoothGatt.getService(targetService));

    }


    @Test
    public void readCharOnlyIfConnected(){
        Assert.assertFalse(bluetoothGatt.readCharacteristic(mock(BluetoothGattCharacteristic.class)));
    }


    private static void connectNode(BluetoothGatt gatt){
        gatt.connect();
        runBacgroundTask();
    }

    @Test
    public void readCharIsSuccessfullIfTheGeneratorHasData(){
        UUID targetChar = UUID.fromString("c24e3528-ef45-4513-a4b9-8cb9719a473c");
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(targetChar,BluetoothGattCharacteristic.PROPERTY_READ,BluetoothGattCharacteristic.PERMISSION_READ);

        byte[] data = new byte[]{0x0,0x01,0x03};

        Iterator<byte[]> generator = mock(Iterator.class);

        when(generator.hasNext()).thenReturn(true);
        when(generator.next()).thenReturn(data);

        shadowBluetoothGatt.setDataGeneratorForCharacteristics(targetChar,generator);

        connectNode(bluetoothGatt);

        Assert.assertTrue(bluetoothGatt.readCharacteristic(characteristic));

        runBacgroundTask();

        verify(mCallback).onCharacteristicRead(bluetoothGatt,characteristic,BluetoothGatt.GATT_SUCCESS);
        Assert.assertArrayEquals(data,characteristic.getValue());

    }

    @Test
    public void readCharFailIfTheGeneratorHasNoData(){
        UUID targetChar = UUID.fromString("c24e3528-ef45-4513-a4b9-8cb9719a473c");
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(targetChar,BluetoothGattCharacteristic.PROPERTY_READ,BluetoothGattCharacteristic.PERMISSION_READ);

        Iterator<byte[]> generator = mock(Iterator.class);

        when(generator.hasNext()).thenReturn(false);

        shadowBluetoothGatt.setDataGeneratorForCharacteristics(targetChar,generator);

        connectNode(bluetoothGatt);

        Assert.assertTrue(bluetoothGatt.readCharacteristic(characteristic));

        runBacgroundTask();

        verify(mCallback).onCharacteristicRead(bluetoothGatt,characteristic,BluetoothGatt.GATT_FAILURE);

    }


    @Test
    public void readCharReturnFalseWithWrongProperties(){

        BluetoothGattCharacteristic characteristic =  mock(BluetoothGattCharacteristic.class);

        connectNode(bluetoothGatt);

        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_NOTIFY);
        Assert.assertFalse(bluetoothGatt.readCharacteristic(characteristic));

        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_WRITE);

        Assert.assertFalse(bluetoothGatt.readCharacteristic(characteristic));
    }

    @Test
    public void writeCharGiveCallback(){
        UUID targetChar = UUID.fromString("c24e3528-ef45-4513-a4b9-8cb9719a473c");
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(targetChar,BluetoothGattCharacteristic.PROPERTY_WRITE,BluetoothGattCharacteristic.PERMISSION_WRITE);

        connectNode(bluetoothGatt);

        Assert.assertTrue(bluetoothGatt.writeCharacteristic(characteristic));

        runBacgroundTask();

        verify(mCallback).onCharacteristicWrite(bluetoothGatt,characteristic,BluetoothGatt.GATT_SUCCESS);

    }

    @Test
    public void writeCharReturnFalseIfNotConnected(){
        UUID targetChar = UUID.fromString("c24e3528-ef45-4513-a4b9-8cb9719a473c");
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(targetChar,BluetoothGattCharacteristic.PROPERTY_WRITE,BluetoothGattCharacteristic.PERMISSION_WRITE);

        Assert.assertFalse(bluetoothGatt.writeCharacteristic(characteristic));

    }

    @Test
    public void writeCharReturnFalseWithWrongProperties(){
        BluetoothGattCharacteristic characteristic =  mock(BluetoothGattCharacteristic.class);

        connectNode(bluetoothGatt);

        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_NOTIFY);
        Assert.assertFalse(bluetoothGatt.writeCharacteristic(characteristic));

        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_READ);

        Assert.assertFalse(bluetoothGatt.writeCharacteristic(characteristic));;
    }

}
