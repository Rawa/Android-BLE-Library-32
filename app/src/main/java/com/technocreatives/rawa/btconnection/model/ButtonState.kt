package com.pansaraugmented.app.model.domain.types

import com.technocreatives.rawa.btconnection.Button
import com.pansaraugmented.common.ButtonPressType

/**
 * Created by David Goransson on 2018-03-01.
 * Copyright 2018 The Techno Creatives. All rights reserved
 */

data class ButtonState(val button: Button, val buttonPressType: ButtonPressType)