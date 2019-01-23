package com.meteoro.creaturemon.mvp.app

import android.app.Application
import androidx.room.Room
import com.meteoro.creaturemon.mvp.model.room.CreatureDatabase

class CreaturemonApplication : Application() {

    companion object {
        lateinit var database: CreatureDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            this, CreatureDatabase::class.java,
            "creature_database"
        ).build()
    }
}