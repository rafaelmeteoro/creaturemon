package com.meteoro.creaturemon.mvi.allcreatures

import com.meteoro.creaturemon.mvi.mvibase.MviIntent

sealed class AllCreaturesIntent : MviIntent {
    object LoadAllCreaturesIntent : AllCreaturesIntent()
    object ClearAllCreaturesIntent : AllCreaturesIntent()
}