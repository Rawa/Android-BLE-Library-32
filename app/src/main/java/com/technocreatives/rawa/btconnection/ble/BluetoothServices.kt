package com.technocreatives.rawa.btconnection.ble

import java.util.*

interface BluetoothServiceIdentifier {
    val uuid: UUID
    fun characteristicUUID(partialUuid: String): UUID {
        assert(partialUuid.length == 4)
        val charUUIDString = uuid.toString().replaceRange(3..7, partialUuid)
        return UUID.fromString(charUUIDString)
    }
}

interface BluetoothCharacteristicIdentifier {
    val partialUuid: String
    val service: BluetoothServiceIdentifier
    val uuid: UUID get() = service.characteristicUUID(partialUuid)
}

object DeviceService : BluetoothServiceIdentifier {
    override val uuid = UUID.fromString("00001623-1212-efde-1223-785feabcd123")!!

}

enum class DeviceCharacteristics(override val partialUuid: String) : BluetoothCharacteristicIdentifier {
    FunctionMode("1554"),
    ButtonState("1591");
    // Not used

    override val service = DeviceService
}

object UnknownService : BluetoothServiceIdentifier {
    override val uuid = UUID.fromString("00001811-1212-efde-1223-785feabcd123")!!
}

enum class UnknownCharacteristics(override val partialUuid: String): BluetoothCharacteristicIdentifier {
    Pairing("1820"); // Not used

    override val service = UnknownService
}

object BatteryService : BluetoothServiceIdentifier {
    override val uuid = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")!!
}

enum class BatteryCharacteristics(override val partialUuid: String) : BluetoothCharacteristicIdentifier {
    BatteryLevel("2A19");

    override val service: BluetoothServiceIdentifier = BatteryService
}


object DeviceInformationService : BluetoothServiceIdentifier {
    override val uuid = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")!!
}

enum class DeviceInformationCharacteristics(override val partialUuid: String) : BluetoothCharacteristicIdentifier {
    FirmwareVersion("2A26");

    override val service: BluetoothServiceIdentifier = DeviceInformationService
}

