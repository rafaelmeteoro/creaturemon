package com.meteoro.creaturemon.mvi.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meteoro.creaturemon.mvi.addcreature.AddCreatureProcessorHolder
import com.meteoro.creaturemon.mvi.addcreature.AddCreatureViewModel
import com.meteoro.creaturemon.mvi.allcreatures.AllCreaturesProcessorHolder
import com.meteoro.creaturemon.mvi.allcreatures.AllCreaturesViewModel
import com.meteoro.creaturemon.mvi.app.Injection

class CreaturemonViewModelFactory private constructor(
    private val applicationContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == AllCreaturesViewModel::class.java) {
            return AllCreaturesViewModel(
                AllCreaturesProcessorHolder(
                    Injection.provideCreatureRepository(applicationContext),
                    Injection.provideSchedulerProvider()
                )
            ) as T
        }
        if (modelClass == AddCreatureViewModel::class.java) {
            return AddCreatureViewModel(
                AddCreatureProcessorHolder(
                    Injection.provideCreatureRepository(applicationContext),
                    Injection.provideCreatureGenerator(),
                    Injection.provideSchedulerProvider()
                )
            ) as T
        }
        throw IllegalArgumentException("unknown model class $modelClass")
    }

    companion object : SingletonHolderSingleArg<CreaturemonViewModelFactory, Context>
        (::CreaturemonViewModelFactory)
}