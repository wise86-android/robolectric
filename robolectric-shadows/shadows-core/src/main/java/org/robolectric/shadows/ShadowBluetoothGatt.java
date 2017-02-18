package org.robolectric.shadows;


import android.annotation.NonNull;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import java.util.Collections;
import java.util.List;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;

@Implements(value = BluetoothGatt.class, minSdk = JELLY_BEAN_MR2)
public class ShadowBluetoothGatt {

    @RealObject private BluetoothGatt realGatt;

    private boolean mServiceDiscoverd;
    private BluetoothGattCallback userCallback;
    private BluetoothDevice device;

    private List<BluetoothGattService> deviceServices;

    public void setDevice(BluetoothDevice device){
        this.device = device;
    }

    public void setCallback(@NonNull BluetoothGattCallback callback) {
        userCallback=callback;
    }

    public void setDeviceServices(List<BluetoothGattService> services){
        deviceServices = services;
    }

    @Implementation
    public boolean connect(){
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                userCallback.onConnectionStateChange(realGatt,
                        BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTING);
            }
        });
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                userCallback.onConnectionStateChange(realGatt,
                        BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
            }
        });
        return true;
    }

    @Implementation
    public void disconnect(){
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                userCallback.onConnectionStateChange(realGatt,
                        BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTING);
            }
        });
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                userCallback.onConnectionStateChange(realGatt,
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
        runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mServiceDiscoverd =true;
                userCallback.onServicesDiscovered(realGatt,BluetoothGatt.GATT_SUCCESS);
            }
        });
    }

    @Implementation
    public List<BluetoothGattService> getServices(){
        if(mServiceDiscoverd)
            return Collections.unmodifiableList(deviceServices);
        else
            return Collections.emptyList();
    }

    private static void runOnBackgroundThread(Runnable task){
        ShadowApplication app = ShadowApplication.getInstance();
        if(app == null)
            throw new RuntimeException("Impossible run background event");

        app.getBackgroundThreadScheduler().post(task);
    }


}
