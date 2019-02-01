package com.meteoro.creaturemon.mvi.addcreature

import com.meteoro.creaturemon.mvi.data.model.Creature
import com.meteoro.creaturemon.mvi.data.model.CreatureAttributes
import com.meteoro.creaturemon.mvi.data.model.CreatureGenerator
import com.meteoro.creaturemon.mvi.mvibase.MviViewState

data class AddCreatureViewState(
    val isProcessing: Boolean,
    val creature: Creature,
    val isDrawableSelected: Boolean,
    val isSaveComplete: Boolean,
    val error: Throwable?
) : MviViewState {
    companion object {
        fun default(): AddCreatureViewState = AddCreatureViewState(
            isProcessing = false,
            creature = CreatureGenerator().generateCreature(CreatureAttributes(), name = "", drawable = 0),
            isDrawableSelected = false,
            isSaveComplete = false,
            error = null
        )
    }
}