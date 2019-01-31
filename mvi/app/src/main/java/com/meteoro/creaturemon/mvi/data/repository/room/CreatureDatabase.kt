package com.meteoro.creaturemon.mvi.data.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.meteoro.creaturemon.mvi.data.model.Creature

@Database(entities = [(Creature::class)], version = 1)
@TypeConverters(CreatureAttributeConverter::class)
abstract class CreatureDatabase : RoomDatabase() {
    abstract fun creatureDao(): CreatureDao
}