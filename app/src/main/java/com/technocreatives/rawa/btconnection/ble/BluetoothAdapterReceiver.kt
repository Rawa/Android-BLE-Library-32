package com.technocreatives.rawa.btconnection.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.getSystemService
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


object BluetoothAdapterReceiver : BroadcastReceiver() {
    private lateinit var btStateSubject: BehaviorSubject<BluetoothAdapterState>
    lateinit var bluetoothState: Observable<BluetoothAdapterState>

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            when (state) {
                BluetoothAdapter.STATE_ON -> btStateSubject.onNext(BluetoothAdapterState.ON)
                BluetoothAdapter.STATE_OFF -> btStateSubject.onNext(BluetoothAdapterState.OFF)
                BluetoothAdapter.STATE_TURNING_ON -> btStateSubject.onNext(BluetoothAdapterState.TURNING_ON)
                BluetoothAdapter.STATE_TURNING_OFF -> btStateSubject.onNext(BluetoothAdapterState.TURNING_OFF)
            }
        }
    }

    fun register(context: Context) {
        val btManager = context.getSystemService<BluetoothManager>()
        val initialState = BluetoothAdapterState.fromAdapterState(btManager!!.adapter.state)

        btStateSubject = BehaviorSubject.createDefault(initialState)
        bluetoothState = btStateSubject.hide()!!

        // Register receiver
        val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(this, intentFilter)
    }

    fun unregister(context: Context) {
        btStateSubject.onComplete()
        context.unregisterReceiver(this)
    }
}


enum class BluetoothAdapterState {
    TURNING_ON,
    TURNING_OFF,
    ON,
    OFF;

    companion object {
        fun fromAdapterState(state: Int): BluetoothAdapterState {
            return when (state) {
                BluetoothAdapter.STATE_ON -> ON
                BluetoothAdapter.STATE_OFF -> OFF
                BluetoothAdapter.STATE_TURNING_ON -> TURNING_ON
                BluetoothAdapter.STATE_TURNING_OFF -> TURNING_OFF
                else -> {
                    throw RuntimeException("Unknown state: $state")
                }
            }

        }
    }
}
