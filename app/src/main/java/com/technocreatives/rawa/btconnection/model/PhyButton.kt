package com.technocreatives.rawa.btconnection


/**
 * Created by David Goransson on 2017-12-04.
 * Copyright 2017 The Techno Creatives. All rights reserved
 */
enum class PhyButton(val value: Int) {
    BUTTON_1(0x01),
    BUTTON_2(0x02),
    BUTTON_3(0x04);

    fun toDomainType(): Button = when (this) {
        BUTTON_1 -> Button.TOP
        BUTTON_2 -> Button.MIDDLE
        BUTTON_3 -> Button.BOTTOM
    }

    companion object {
        fun valueOf(value: Byte): PhyButton {
            val asInt = value.toInt()

            PhyButton.values().forEach {
                if (it.value == asInt) {
                    return it
                }
            }

            throw IllegalArgumentException("Byte does not match protocol")

        }

        fun valueOf(button: Button): PhyButton = when (button) {
            Button.TOP -> BUTTON_1
            Button.MIDDLE -> BUTTON_2
            Button.BOTTOM -> BUTTON_3
        }
    }
}
