package com.meteoro.creaturemon.mvp.presenter

import androidx.lifecycle.LiveData
import com.meteoro.creaturemon.mvp.model.Creature
import com.meteoro.creaturemon.mvp.model.CreatureRepository
import com.meteoro.creaturemon.mvp.model.room.RoomRepository

class AllCreaturesPresenter(private val repository: CreatureRepository = RoomRepository()) :
    BasePresenter<AllCreaturesContract.View>(), AllCreaturesContract.Presenter {

    override fun getAllCreatures(): LiveData<List<Creature>> {
        return repository.getAllCreatures()
    }

    override fun clearAllCreatures() {
        repository.clearAllCreatures()
        getView()?.showCreaturesCleared()
    }
}