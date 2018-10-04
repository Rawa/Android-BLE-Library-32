package com.technocreatives.rawa.btconnection.fragments.selectdevice

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pansaraugmented.app.ui.views.base.customrecyclerview.SortedRecyclerViewAdapter
import com.technocreatives.rawa.btconnection.R
import com.technocreatives.rawa.btconnection.ble.DeviceService
import kotlinx.android.synthetic.main.fragment_selectdevice.view.*
import no.nordicsemi.android.support.v18.scanner.*
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class SelectDeviceFragment : Fragment(), OnPeripheralSelectedListener, EasyPermissions.PermissionCallbacks {

    private val rationale = "Location permissions has not been enabled. Please enable inorder to be able to scan."
    private val requestCode: Int = 1
    private val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    private val factory = PeripheralViewHolder.Factory(this)
    private val comparator: Comparator<DiscoveredPeripheral> = Comparator { o1, o2 -> o2.rssi - o1.rssi }
    private val adapter = SortedRecyclerViewAdapter(factory, R.layout.list_item_peripheral_scan, comparator)

    private val ENABLE_BT_RC = 1

    val settings = ScanSettings.Builder()
            .setLegacy(false)
            .setUseHardwareFilteringIfSupported(false)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setUseHardwareBatchingIfSupported(false)
            .build()

    val requiredServices = listOf(DeviceService.uuid)
    val filters = requiredServices.let { services ->
        services.map {
            ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid(it))
                    .build()
        }
    }
    private val scanner = BluetoothLeScannerCompat.getScanner()!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_selectdevice, container, false)

        // Set the adapter
        val listView = view.list
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = adapter

        val refreshButton = view!!.b_scanning_refresh
        refreshButton.setOnClickListener {
            startScan()
        }

        return view
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (!prerequisitesFulfilled()) {
            if (!bluetoothEnabled()) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                activity!!.startActivityForResult(enableBtIntent, ENABLE_BT_RC)
            }
            if (!hasPermission()) {
                EasyPermissions.requestPermissions(activity!!, rationale,
                        requestCode, *permissions)

            }
        }

        val bluetoothManager = activity!!.getSystemService<BluetoothManager>()!!
        if (bluetoothManager.getConnectedDevices(BluetoothProfile.GATT).isNotEmpty()) {
            context!!.toast("Hanging BT connections!")
        }

    }

    override fun onDeviceSelected(scanResult: DiscoveredPeripheral) {
        val action = SelectDeviceFragmentDirections.actionFragmentSelectDeviceToFragmentDevice(scanResult.device.address)
        findNavController().navigate(action)
    }

    val peripheralComparator = Comparator<DiscoveredPeripheral> { o1, o2 -> o1.device.address.compareTo(o2.device.address) }
    val scanCallback = object: ScanCallback(){
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                val discoveredPeripheral = DiscoveredPeripheral(result.device, result.rssi)

                adapter.replaceAddItem(discoveredPeripheral, peripheralComparator)
            }
        }

    private fun startScan() {
        adapter.clear()
        scanner.stopScan(scanCallback)
        scanner.startScan(filters, settings, scanCallback)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, activity!!)
    }

    private fun prerequisitesFulfilled(): Boolean {
        return bluetoothEnabled() && hasPermission()
    }

    private fun bluetoothEnabled(): Boolean {
        val btManager = context!!.getSystemService<BluetoothManager>()!!
        return btManager.adapter.isEnabled
    }

    private fun hasPermission(): Boolean {
        return EasyPermissions.hasPermissions(context!!, *permissions)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(tag, "onPermissionsGranted")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(tag, "onPermissionsDenied")
        if (EasyPermissions.somePermissionPermanentlyDenied(activity!!, perms)) {
            Log.d(tag, "onPermissionsGranted")
            AppSettingsDialog.Builder(activity!!).setRequestCode(AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE).build().show()
        } else {
            activity!!.finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(tag, "onActivityResult $requestCode, $resultCode")

        if (requestCode == ENABLE_BT_RC && !bluetoothEnabled()) {
            activity!!.finish()
        }

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE && !hasPermission()) {
            // Do something after user returned from app settings screen, like showing a Toast.
            activity!!.finish()
        }
    }

    override fun onDetach() {
        super.onDetach()
        scanner.stopScan(scanCallback)
    }
}
data class DiscoveredPeripheral(val device: BluetoothDevice, val rssi: Int)
