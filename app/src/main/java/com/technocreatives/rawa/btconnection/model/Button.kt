package com.technocreatives.rawa.btconnection

import java.io.Serializable

/**
 * Created by David Goransson on 2017-12-04.
 * Copyright 2017 The Techno Creatives. All rights reserved
 */
enum class Button(val value: String) {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    override fun toString() = value
}
