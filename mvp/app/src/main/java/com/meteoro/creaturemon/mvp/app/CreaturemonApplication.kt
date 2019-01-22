package com.meteoro.creaturemon.mvp.app

import android.app.Application
import com.meteoro.creaturemon.mvp.model.room.CreatureDatabase

class CreaturemonApplication : Application() {

    companion object {
        lateinit var database: CreatureDatabase
    }

    override fun onCreate() {
        super.onCreate()
        // TODO: init database
    }
}