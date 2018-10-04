package com.technocreatives.rawa.btconnection.ble

import android.bluetooth.BluetoothDevice
import com.pansaraugmented.app.model.domain.types.ButtonState
import com.pansaraugmented.app.model.domain.types.FunctionMode
import com.technocreatives.rawa.btconnection.PhyButtonState
import com.technocreatives.rawa.btconnection.PhyFunctionMode
import no.nordicsemi.android.ble.BleManagerCallbacks
import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback
import no.nordicsemi.android.ble.data.Data

interface DeviceCallbacks: BleManagerCallbacks, ButtonCallback, FunctionModeCallback, BondCallback

interface ButtonCallback{
    fun onButtonStateChanged(buttonState: ButtonState)
}

abstract class ButtonDataCallback: ProfileDataCallback, ButtonCallback {
    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        val byteArray = data.value
        if(byteArray == null){
            onInvalidDataReceived(device, data)
            return
        }

        val phyButtonState = PhyButtonState.valueOf(byteArray)

        if(phyButtonState == null){
            onInvalidDataReceived(device, data)
            return
        }

        onButtonStateChanged(phyButtonState.toDomainType())

    }
}


interface BondCallback{
    fun onBondChanged()
}

abstract class BondDataCallback: ProfileDataCallback, BondCallback {
    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        onBondChanged()
    }
}


interface FunctionModeCallback{
    fun onFunctionModeChanged(mode: FunctionMode)
}

abstract class FunctionModeDataCallback: ProfileDataCallback, FunctionModeCallback {
    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        val byteArray = data.value
        if(byteArray == null){
            onInvalidDataReceived(device, data)
            return
        }

        val phyFunctionMode = PhyFunctionMode.valueOf(byteArray)

        if(phyFunctionMode == null){
            onInvalidDataReceived(device, data)
            return
        }

        onFunctionModeChanged(phyFunctionMode.toDomainType())
    }
}
