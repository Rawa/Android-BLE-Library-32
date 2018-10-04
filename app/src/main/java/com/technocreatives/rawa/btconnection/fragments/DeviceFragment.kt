package com.technocreatives.rawa.btconnection.fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pansaraugmented.app.model.domain.types.ButtonState
import com.pansaraugmented.app.model.domain.types.FunctionMode
import com.technocreatives.rawa.btconnection.ble.DeviceCallbacks
import com.technocreatives.rawa.btconnection.ble.MyBleManager
import com.technocreatives.rawa.btconnection.R
import kotlinx.android.synthetic.main.fragment_device.view.*
import no.nordicsemi.android.log.Logger


class DeviceFragment : Fragment() {

    lateinit var manager: MyBleManager
    lateinit var macAddress: String
    lateinit var device: BluetoothDevice

    val TAG = "DeviceManager"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_device, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Automatically connect on attach
        val session = Logger.newSession(activity, "MyDevice", "time")
        val args = DeviceFragmentArgs.fromBundle(arguments)
        macAddress = args.macAddress
        Log.d("DeviceFragment", "Address: $macAddress")

        val adapter = BluetoothAdapter.getDefaultAdapter()
        device = adapter.getRemoteDevice(macAddress)
        manager = MyBleManager(context!!)
        manager.setLogger(session)
        manager.setGattCallbacks(object: DeviceCallbacks {
            override fun onBondChanged() {
                Log.d(TAG, "onBondChanged")
            }

            override fun onDeviceDisconnecting(device: BluetoothDevice?) {
                Log.d(TAG, "onDeviceDisconnecting")
            }

            override fun onDeviceDisconnected(device: BluetoothDevice?) {
                Log.d(TAG, "onDeviceDisconnected")
            }

            override fun onDeviceConnected(device: BluetoothDevice?) {
                Log.d(TAG,"onDeviceConnected")
            }

            override fun onDeviceNotSupported(device: BluetoothDevice?) {
                Log.d(TAG, "onDeviceNotSupported")
            }

            override fun onBondingFailed(device: BluetoothDevice?) {
                Log.d(TAG, "onBondingFailed")
            }

            override fun onServicesDiscovered(device: BluetoothDevice?, optionalServicesFound: Boolean) {
                Log.d(TAG, "onServicesDiscovered")
            }

            override fun onBondingRequired(device: BluetoothDevice?) {
                Log.d(TAG, "onBondingRequired")
            }

            override fun onLinkLossOccurred(device: BluetoothDevice?) {
                Log.d(TAG, "onLinkLoss")
            }

            override fun onBonded(device: BluetoothDevice?) {
                Log.d(TAG, "onBonded")
            }

            override fun onDeviceReady(device: BluetoothDevice?) {
                Log.d(TAG, "onDeviceReady")
            }

            override fun onError(device: BluetoothDevice?, message: String?, errorCode: Int) {
                Log.d(TAG, "onError: $message")
            }

            override fun onDeviceConnecting(device: BluetoothDevice?) {
                Log.d(TAG, "onDeviceConnecting")
            }

            override fun onButtonStateChanged(buttonState: ButtonState) {
                Log.d(TAG, "onButtonStateChanged: $buttonState")
            }

            override fun onFunctionModeChanged(mode: FunctionMode) {
                Log.d(TAG, "onFunctionModeChanged: $mode")
            }

        })
        Log.d(TAG, "BeforeConnect: ${manager.isConnected}")
        manager.connect(device).done {
            Log.d(TAG, "connect Success")
            manager.readBondCharacteristics()
            view!!.connect.text = "Connect success"
        }.fail{ _, status ->
            Log.d(TAG, "connect fail!")
            view!!.connect.text = "Connect fail, status: $status"
        }.enqueue()
    }



    override fun onDetach() {
        super.onDetach()

        Log.d(TAG, "isConnected: ${manager.isConnected}")
        if (manager.isConnected){
            manager.disconnect().done {
                Log.d(TAG, "disonnectSuccess")
            }.fail{ _, status ->
                Log.d(TAG, "disonnectFail: $status")
            }.enqueue()
        }


    }
}
