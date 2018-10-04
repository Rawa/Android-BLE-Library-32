package com.technocreatives.rawa.btconnection

import android.util.Log
import com.pansaraugmented.app.model.domain.types.ButtonState
import com.pansaraugmented.app.model.physical.PhyButtonPressType

/**
 * Created by David Goransson on 2017-12-04.
 * Copyright 2017 The Techno Creatives. All rights reserved
 */
data class PhyButtonState(private val phyButton: PhyButton, private val phyButtonPressType: PhyButtonPressType) {

    fun toDomainType(): ButtonState = ButtonState(phyButton.toDomainType(), phyButtonPressType.toDomainType())

    companion object {
        fun valueOf(value: ByteArray): PhyButtonState? {
            require(value.size == 2) { "Invalid size of ByteArray" }

            val (phyButtonByte, phyButtonPressTypeByte) = value

            Log.d("Test", "PhyButtonByte $phyButtonByte, PhyButtonPressTypeByte $phyButtonPressTypeByte")
            val phyButton = PhyButton.valueOf(phyButtonByte)

            val phyButtonPressType = PhyButtonPressType.valueOf(phyButtonPressTypeByte)
                    ?: PhyButtonPressType.SINGLE_CLICK

            Log.d("Test", "PhyButton $phyButton, PhyButtonPressType $phyButtonPressType")

            return PhyButtonState(phyButton, phyButtonPressType)
        }
    }
}
