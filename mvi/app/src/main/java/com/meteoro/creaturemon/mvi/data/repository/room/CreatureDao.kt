package com.meteoro.creaturemon.mvi.data.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meteoro.creaturemon.mvi.data.model.Creature
import io.reactivex.Observable

@Dao
interface CreatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(creature: Creature)

    @Query("DELETE FROM creature_tab")
    fun clearAllCreatures()

    @Query("SELECT * FROM creature_tab ORDER BY name ASC")
    fun getAllCreatures(): Observable<List<Creature>>
}