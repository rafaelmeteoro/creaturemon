package com.meteoro.creaturemon.mvp.presenter

import androidx.lifecycle.LiveData
import com.meteoro.creaturemon.mvp.model.Creature

interface AllCreaturesContract {
    interface Presenter {
        fun getAllCreatures(): LiveData<List<Creature>>
        fun clearAllCreatures()
    }

    interface View {
        fun showCreaturesCleared()
    }
}