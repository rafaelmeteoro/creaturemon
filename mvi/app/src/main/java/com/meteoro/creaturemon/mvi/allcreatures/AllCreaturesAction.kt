package com.meteoro.creaturemon.mvi.allcreatures

import com.meteoro.creaturemon.mvi.mvibase.MviAction

sealed class AllCreaturesAction : MviAction {
    object LoadAllCreaturesAction : AllCreaturesAction()
    object ClearAllCreaturesAction : AllCreaturesAction()
}