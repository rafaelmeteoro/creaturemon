package com.meteoro.creaturemon.mvi.allcreatures

import androidx.lifecycle.ViewModel
import com.meteoro.creaturemon.mvi.allcreatures.AllCreaturesResult.ClearAllCreaturesResult
import com.meteoro.creaturemon.mvi.allcreatures.AllCreaturesResult.LoadAllCreaturesResult
import com.meteoro.creaturemon.mvi.mvibase.MviViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class AllCreaturesViewModel(
    private val actionProcessorHolder: AllCreaturesProcessorHolder
) : ViewModel(), MviViewModel<AllCreaturesIntent, AllCreaturesViewState> {

    override fun processIntent(intents: Observable<AllCreaturesIntent>) {

    }

    override fun states(): Observable<AllCreaturesViewState> {

    }

    companion object {
        private val reducer = BiFunction { previousState: AllCreaturesViewState, result: AllCreaturesResult ->
            when (result) {
                is LoadAllCreaturesResult -> when (result) {
                    is LoadAllCreaturesResult.Success -> {
                        previousState.copy(isLoading = false, creatures = result.creatures)
                    }
                    is LoadAllCreaturesResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is LoadAllCreaturesResult.Loading -> previousState.copy(isLoading = true)
                }
                is ClearAllCreaturesResult -> when (result) {
                    is ClearAllCreaturesResult.Success -> {
                        previousState.copy(isLoading = false, creatures = emptyList())
                    }
                    is ClearAllCreaturesResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is ClearAllCreaturesResult.Clearing -> previousState.copy(isLoading = true)
                }
            }
        }
    }
}