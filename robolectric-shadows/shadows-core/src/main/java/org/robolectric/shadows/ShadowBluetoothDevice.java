package org.robolectric.shadows;

import android.bluetooth.BluetoothDevice;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_CLASSIC;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_DUAL;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_UNKNOWN;

/**
 * Shadow for {@link android.bluetooth.BluetoothDevice}.
 */
@Implements(BluetoothDevice.class)
public class ShadowBluetoothDevice {

  private String name;
  private String address;

    private int deviceType=DEVICE_TYPE_UNKNOWN;

  public void setAddress(String address){this.address = address;}

  public void setName(String name) {
    this.name = name;
  }

    public void setDeviceType(int deviceType){
        if(deviceType == DEVICE_TYPE_CLASSIC ||
                deviceType == DEVICE_TYPE_DUAL ||
                deviceType == DEVICE_TYPE_LE ||
                deviceType == DEVICE_TYPE_UNKNOWN)
            this.deviceType = deviceType;
        else
            this.deviceType =DEVICE_TYPE_UNKNOWN;
    }

  @Implementation
  public String getName() {
    return name;
  }

  @Implementation
  public String getAddress(){return address;}

    @Implementation
    public  int getType(){return deviceType;}

}
