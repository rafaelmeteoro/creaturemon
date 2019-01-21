package com.meteoro.creaturemon.model.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.meteoro.creaturemon.model.Creature

@Dao
interface CreatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(creature: Creature)

    @Delete
    fun clearCreatures(vararg creature: Creature)

    @Query("SELECT * FROM creature_table ORDER BY name ASC")
    fun getAllCreatures(): LiveData<List<Creature>>
}