package com.pansaraugmented.app.model.domain.types

/**
 * Created by David Goransson on 2017-12-04.
 * Copyright 2017 The Techno Creatives. All rights reserved
 */

sealed class FunctionMode {
    object SAV : FunctionMode()

    object Time : FunctionMode() {
        override fun toString(): String {
            return "Time()"
        }
    }

    data class Live(val index: Int) : FunctionMode()
    object Init : FunctionMode() {
        override fun toString(): String {
            return "Init()"
        }
    }

    fun toMMI(): Int = when (this) {
        is FunctionMode.Time -> 0
        is FunctionMode.Live -> this.index + 1
        is FunctionMode.Init -> -1
        is FunctionMode.SAV -> -2
    }
}