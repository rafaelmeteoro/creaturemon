package com.meteoro.creaturemon.mvi.app

import android.app.Application
import androidx.room.Room
import com.meteoro.creaturemon.mvi.data.repository.room.CreatureDatabase

class CreaturemonApplication : Application() {

    companion object {
        lateinit var database: CreatureDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, CreatureDatabase::class.java, "creature_database")
            .build()
    }
}