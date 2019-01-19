package com.meteoro.creaturemon.model

import android.arch.lifecycle.LiveData

interface CreatureRepository {
    fun saveCreature(creature: Creature)
    fun getAllCreaatures(): LiveData<List<Creature>>
    fun clearAllCreatures()
}