package com.meteoro.creaturemon.mvi.allcreatures

import com.meteoro.creaturemon.mvi.data.model.Creature
import com.meteoro.creaturemon.mvi.mvibase.MviViewState

data class AllCreaturesViewState(
    val isLoading: Boolean,
    val creatures: List<Creature>,
    val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): AllCreaturesViewState = AllCreaturesViewState(
            isLoading = false,
            creatures = emptyList(),
            error = null
        )
    }
}