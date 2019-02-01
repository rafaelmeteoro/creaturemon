package com.meteoro.creaturemon.mvi.mvibase

import io.reactivex.Observable

interface MviViewModel<I : MviIntent, S : MviViewState> {
    fun processIntent(intents: Observable<I>)
    fun states(): Observable<S>
}