package com.meteoro.creaturemon.viewmodel

import android.arch.lifecycle.ViewModel
import com.meteoro.creaturemon.model.CreatureRepository
import com.meteoro.creaturemon.model.room.RoomRepository

class AllCreaturesViewModel(private val repository: CreatureRepository = RoomRepository()) : ViewModel() {

    private val allCreaturesLiveData = repository.getAllCreatures()

    fun getAllCreaturesLiveData() = allCreaturesLiveData

    fun clearAllCreatures() = repository.clearAllCreatures()
}