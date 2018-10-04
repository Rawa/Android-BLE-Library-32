package com.technocreatives.rawa.btconnection.ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import com.pansaraugmented.app.model.domain.types.ButtonState
import com.pansaraugmented.app.model.domain.types.FunctionMode
import no.nordicsemi.android.ble.BleManager

class MyBleManager(context: Context): BleManager<DeviceCallbacks>(context) {
    private var modeCharacteristics: BluetoothGattCharacteristic? = null
    private val modeCallback = object: FunctionModeDataCallback() {
        override fun onFunctionModeChanged(mode: FunctionMode) {
            mCallbacks.onFunctionModeChanged(mode)
        }
    }

    private var buttonCharacteristics: BluetoothGattCharacteristic? = null
    private val buttonDataCallback = object: ButtonDataCallback() {
        override fun onButtonStateChanged(buttonState: ButtonState) {
            mCallbacks.onButtonStateChanged(buttonState)
        }
    }

    private var bondCharacteristics: BluetoothGattCharacteristic? = null
    private val bondDataCallback = object: BondDataCallback() {
        override fun onBondChanged() {
            mCallbacks.onBondChanged()
        }
    }

    private val gattCallback = object: BleManagerGattCallback(){
        override fun initialize() {
            super.initialize()
            readCharacteristic(bondCharacteristics).with(bondDataCallback).enqueue()

            readCharacteristic(modeCharacteristics).with(modeCallback).enqueue()
            setNotificationCallback(modeCharacteristics).with(modeCallback)
            enableNotifications(modeCharacteristics).enqueue()
            setNotificationCallback(buttonCharacteristics).with(buttonDataCallback)
            enableNotifications(buttonCharacteristics).enqueue()
        }

        override fun onDeviceDisconnected() {
            modeCharacteristics = null
            buttonCharacteristics = null
            bondCharacteristics = null
        }

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val clockService = gatt.getService(DeviceService.uuid) ?: return false
            val unknownService = gatt.getService(UnknownService.uuid) ?: return false

            modeCharacteristics = clockService.getCharacteristic(DeviceCharacteristics.FunctionMode.uuid) ?: return false
            buttonCharacteristics = clockService.getCharacteristic(DeviceCharacteristics.ButtonState.uuid) ?: return false
            bondCharacteristics = unknownService.getCharacteristic(UnknownCharacteristics.Pairing.uuid) ?: return false

            return true
        }

    }

    override fun getGattCallback(): BleManagerGattCallback {
        return gattCallback
    }

    override fun shouldAutoConnect(): Boolean {
        return true
    }

    fun readBondCharacteristics(){
        bondCharacteristics?.let {
            readCharacteristic(bondCharacteristics).with(bondDataCallback).enqueue()
        }
    }
}
