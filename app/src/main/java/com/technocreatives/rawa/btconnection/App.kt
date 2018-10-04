package com.technocreatives.rawa.btconnection

import android.app.Application
import com.technocreatives.rawa.btconnection.ble.BluetoothAdapterReceiver

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Listen for adapter changes
        BluetoothAdapterReceiver.register(this)
    }
}
