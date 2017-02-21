package org.robolectric.shadows;


import android.annotation.NonNull;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;

@Implements(value = BluetoothGatt.class, minSdk = JELLY_BEAN_MR2)
public class ShadowBluetoothGatt {

    private final static long CALLBACK_DELAY =100;

    @RealObject private BluetoothGatt realGatt;

    private boolean mServiceDiscoverd=false;
    private boolean mIsConnected=false;
    private BluetoothGattCallback mUserCallback;
    private BluetoothDevice device;

    private List<BluetoothGattService> mDeviceServices;
    private Map<UUID,Iterator<byte[]>> mCharDataGenerator = new HashMap<>();


    public void setDevice(BluetoothDevice device){
        this.device = device;
    }

    public void setCallback(@NonNull BluetoothGattCallback callback) {
        mUserCallback =callback;
    }

    public void setDeviceServices(List<BluetoothGattService> services){
        mDeviceServices = services;
    }

    public void setDataGeneratorForCharacteristics(UUID charUuid, Iterator<byte[]> dataGenerator){
        mCharDataGenerator.put(charUuid,dataGenerator);
    }

    @Implementation
    public boolean connect(){
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mUserCallback.onConnectionStateChange(realGatt,
                        BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTING);
            }
        });
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mIsConnected=true;
                mUserCallback.onConnectionStateChange(realGatt,
                        BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
            }
        });
        return true;
    }

    @Implementation
    public void disconnect(){
        if(!mIsConnected)
            return;
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mUserCallback.onConnectionStateChange(realGatt,
                        BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTING);
            }
        });
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mUserCallback.onConnectionStateChange(realGatt,
                        BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
            }
        });
    }

    @Implementation
    public BluetoothDevice getDevice(){
        return device;
    }

    @Implementation
    public void discoverServices(){
        if (!mIsConnected)
            return;
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mServiceDiscoverd =true;
                mUserCallback.onServicesDiscovered(realGatt,BluetoothGatt.GATT_SUCCESS);
            }
        });
    }

    private BluetoothGattService findService(UUID uuid){
        for(BluetoothGattService s : mDeviceServices) {
            if (s.getUuid().equals(uuid))
                return s;
        }
        return null;
    }

    @Implementation
    public BluetoothGattService	getService(UUID uuid){
        if(mServiceDiscoverd)
            return findService(uuid);
        else
            return null;
    }

    @Implementation
    public List<BluetoothGattService> getServices(){
        if(mServiceDiscoverd)
            return Collections.unmodifiableList(mDeviceServices);
        else
            return Collections.emptyList();
    }


    @Implementation
    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic){
        if(!mIsConnected)
            return false;

        if((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) == 0 )
            return false;

        final Iterator<byte[]> generator = mCharDataGenerator.get(characteristic.getUuid());

        final BluetoothGattCharacteristic internalCharacteristics=characteristic;

        final boolean hasNext = generator.hasNext();
        final byte[] data = hasNext ? generator.next() : null ;

        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if(hasNext) {
                    internalCharacteristics.setValue(data);
                    mUserCallback.onCharacteristicRead(realGatt, internalCharacteristics,
                            BluetoothGatt.GATT_SUCCESS);
                }else
                    mUserCallback.onCharacteristicRead(realGatt, internalCharacteristics,
                            BluetoothGatt.GATT_FAILURE);
            }
        });


        return true;
    }

    @Implementation
    public boolean writeCharacteristic (final BluetoothGattCharacteristic characteristic){
        if(!mIsConnected)
            return false;

        if((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) == 0 &&
                (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) == 0)
            return false;

        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mUserCallback.onCharacteristicWrite(realGatt,characteristic,BluetoothGatt.GATT_SUCCESS);
            }
        });

        return true;
    }


    private static void runOnBackgroundThread(Runnable task){
        ShadowApplication app = ShadowApplication.getInstance();
        if(app == null)
            throw new RuntimeException("Impossible run background event");

        app.getBackgroundThreadScheduler().postDelayed(task, CALLBACK_DELAY);
    }


}
