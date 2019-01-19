package com.meteoro.creaturemon.model.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.meteoro.creaturemon.model.Creature

@Database(entities = [(Creature::class)], version = 1)
@TypeConverters(CreatureAttributesConverter::class)
abstract class CreatureDatabase : RoomDatabase() {
    abstract fun createDao(): CreatureDao
}