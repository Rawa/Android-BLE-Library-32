package com.technocreatives.rawa.btconnection.fragments.selectdevice

import android.view.View
import com.pansaraugmented.app.ui.views.base.customrecyclerview.CustomViewHolder
import kotlinx.android.synthetic.main.list_item_peripheral_scan.view.*

class PeripheralViewHolder(view: View, listener: OnPeripheralSelectedListener) : CustomViewHolder<DiscoveredPeripheral>(view) {
    override fun update(data: DiscoveredPeripheral) {
        item = data
        view.peripheral_mac.text = data.device.address
        view.peripheral_content.text = data.rssi.toString()
        view.peripheral_id.text = data.device.name
    }

    lateinit var item: DiscoveredPeripheral

    class Factory(private val onPeripheralSelected: OnPeripheralSelectedListener) : CustomViewHolder.Factory<DiscoveredPeripheral, PeripheralViewHolder>() {
        override fun createViewHolder(view: View): PeripheralViewHolder {
            return PeripheralViewHolder(view, onPeripheralSelected)
        }
    }

    init {
        view.setOnClickListener { listener.onDeviceSelected(item) }
    }
}

interface OnPeripheralSelectedListener {
    fun onDeviceSelected(scanResult: DiscoveredPeripheral)
}
