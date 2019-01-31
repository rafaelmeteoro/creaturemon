package com.meteoro.creaturemon.mvi.data.repository

import com.meteoro.creaturemon.mvi.data.model.Creature
import io.reactivex.Observable

interface CreatureRepository {
    fun saveCreature(creature: Creature): Observable<Boolean>
    fun getAllCreatures(): Observable<List<Creature>>
    fun clearAllCreatures(): Observable<Boolean>
}