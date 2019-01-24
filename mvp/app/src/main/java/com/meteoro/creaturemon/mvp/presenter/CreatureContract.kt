package com.meteoro.creaturemon.mvp.presenter

import androidx.annotation.DrawableRes
import com.meteoro.creaturemon.mvp.model.AttributeType

interface CreatureContract {
    interface Presenter {
        fun updateName(name: String)
        fun attributeSelected(attributeType: AttributeType, position: Int)
        fun drawableSelected(drawable: Int)
        fun isDrawableSelected(): Boolean
        fun saveCreature()
    }

    interface View {
        fun showHitPoints(hitPoints: String)
        fun showAvatarDrawable(@DrawableRes resourceId: Int)
        fun showCreatureSaved()
        fun showCreatureSaveError()
    }
}