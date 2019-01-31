package com.meteoro.creaturemon.mvi.data.model

data class AttributeValue(val name: String = "", val value: Int = 0) {
    override fun toString(): String = "$name: $value"
}