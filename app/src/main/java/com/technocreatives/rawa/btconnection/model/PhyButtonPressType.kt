package com.pansaraugmented.app.model.physical

import com.pansaraugmented.common.ButtonPressType

/**
 * Created by David Goransson on 2017-12-04.
 * Copyright 2017 The Techno Creatives. All rights reserved
 */
enum class PhyButtonPressType(val value: Int) {
    SINGLE_CLICK(0x01),
    DOUBLE_CLICK(0x02),
    TRIPLE_CLICK(0x03),
    LONG_PRESS(0x04),
    VERY_LONG_PRESS(0x05);

    fun toDomainType(): ButtonPressType = when (this) {
        SINGLE_CLICK -> ButtonPressType.SINGLE_CLICK
        DOUBLE_CLICK -> ButtonPressType.DOUBLE_CLICK
        TRIPLE_CLICK -> ButtonPressType.TRIPLE_CLICK
        LONG_PRESS -> ButtonPressType.LONG_PRESS
        VERY_LONG_PRESS -> ButtonPressType.VERY_LONG_PRESS
    }

    companion object {
        fun valueOf(value: Byte): PhyButtonPressType? {
            val asInt = value.toInt()
            values().forEach {
                if (it.value == asInt) {
                    return it
                }
            }
            return null
        }

        fun valueOf(buttonPressType: ButtonPressType): PhyButtonPressType = when (buttonPressType) {
            ButtonPressType.SINGLE_CLICK -> SINGLE_CLICK
            ButtonPressType.DOUBLE_CLICK -> DOUBLE_CLICK
            ButtonPressType.TRIPLE_CLICK -> TRIPLE_CLICK
            ButtonPressType.LONG_PRESS -> LONG_PRESS
            ButtonPressType.VERY_LONG_PRESS -> VERY_LONG_PRESS
        }
    }
}