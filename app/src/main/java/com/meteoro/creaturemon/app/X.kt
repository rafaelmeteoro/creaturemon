package com.meteoro.creaturemon.app

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layouteRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layouteRes, this, attachToRoot)
}