package com.technocreatives.rawa.btconnection

import android.util.Log
import com.pansaraugmented.app.model.domain.types.FunctionMode
import com.technocreatives.rawa.btconnection.ble.DeviceCharacteristics
import java.math.BigInteger

/**
 * Created by David Goransson on 2017-12-04.
 * Copyright 2017 The Techno Creatives. All rights reserved
 */


sealed class PhyFunctionMode {
    abstract fun asByteArray(): ByteArray
    abstract val baseValue: Int

    object Init : PhyFunctionMode() {
        override val baseValue = 0x0600
        override fun asByteArray(): ByteArray = this.baseValue.toBigInteger().toByteArray()
    }

    object Time : PhyFunctionMode() {
        override val baseValue = 0x0100
        override fun asByteArray(): ByteArray = this.baseValue.toBigInteger().toByteArray()
    }

    data class Live(val index: Int) : PhyFunctionMode() {
        override val baseValue = 0x0200

        override fun asByteArray(): ByteArray {
            val indexByte = index + 1 and 0xff
            val result = baseValue or indexByte

            return result.toBigInteger().toByteArray()
        }
    }

    object SAV : PhyFunctionMode() {
        override val baseValue = 0x1600
        override fun asByteArray(): ByteArray = this.baseValue.toBigInteger().toByteArray()
    }

    companion object {
        val uuid = DeviceCharacteristics.FunctionMode.uuid
        fun valueOf(value: ByteArray): PhyFunctionMode {
            require(value.size == 2, { "Invalid size of ByteArray" })

            val asInt = BigInteger(value).toInt()

            // TODO find a prettier way to do this
            val baseValue = asInt and 0xff00
            return when (baseValue) {
                PhyFunctionMode.Init.baseValue -> {
                    PhyFunctionMode.Init
                }
                PhyFunctionMode.Live(0).baseValue -> {
                    Log.d("PhyFunctionmode", "Live mode instance ${value[1] - 1}")
                    PhyFunctionMode.Live(value[1].toInt() - 1)
                }
                PhyFunctionMode.Time.baseValue -> {
                    PhyFunctionMode.Time
                }
                PhyFunctionMode.SAV.baseValue -> {
                    PhyFunctionMode.SAV
                }
                else -> throw IllegalArgumentException("ByteArray does not match protocol")
            }
        }

        fun valueOf(functionMode: FunctionMode): PhyFunctionMode =
                when (functionMode) {
                    is FunctionMode.Init -> PhyFunctionMode.Init
                    is FunctionMode.SAV -> PhyFunctionMode.SAV
                    is FunctionMode.Time -> PhyFunctionMode.Time
                    is FunctionMode.Live -> PhyFunctionMode.Live(functionMode.index)
                }
    }

    fun toDomainType(): FunctionMode = when (this) {
        is PhyFunctionMode.Init -> FunctionMode.Init
        is PhyFunctionMode.Time -> FunctionMode.Time
        is PhyFunctionMode.Live -> FunctionMode.Live(index)
        is PhyFunctionMode.SAV -> FunctionMode.SAV
    }
}

//enum class PhyFunctionMode1(val value: Int) {
//
//
//
//    fun toDomainType(): FunctionMode = when (this) {
//        LIVE -> FunctionMode.LIVE
//        TIME -> FunctionMode.TIME
//        INIT -> FunctionMode.INIT
//    }
//
//    fun asByteArray(): ByteArray = this.value.toBigInteger().toByteArray()
//
//    companion object {
//        val uuid = Utils.fromPartialUUID(1554)
//
//        fun valueOf(value: ByteArray): PhyFunctionMode? {
//            if (value.size != 2) {
//                return null
//            }
//
//            val asInt = (value[0].toInt() shl 8) + value[1]
//
//            PhyFunctionMode.values().forEach {
//                if (it.value == asInt) {
//                    return it
//                }
//            }
//
//            return null
//        }
//
//        fun valueOf(functionMode: FunctionMode): PhyFunctionMode = when (functionMode) {
//            FunctionMode.TIME -> TIME
//            FunctionMode.LIVE -> LIVE
//            FunctionMode.INIT -> INIT
//        }
//    }
//}