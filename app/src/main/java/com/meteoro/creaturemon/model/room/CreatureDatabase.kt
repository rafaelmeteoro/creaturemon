package com.meteoro.creaturemon.model.room

import android.arch.persistence.room.RoomDatabase

abstract class CreatureDatabase : RoomDatabase() {
    abstract fun createDao(): CreatureDao
}