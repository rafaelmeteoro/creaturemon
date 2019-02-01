package com.meteoro.creaturemon.mvi.app

import android.content.Context
import com.meteoro.creaturemon.mvi.data.model.CreatureGenerator
import com.meteoro.creaturemon.mvi.data.repository.CreatureRepository
import com.meteoro.creaturemon.mvi.data.repository.room.RoomRepository
import com.meteoro.creaturemon.mvi.util.schedulers.BaseSchedulerProvider
import com.meteoro.creaturemon.mvi.util.schedulers.SchedulerProvider

object Injection {

    fun provideCreatureRepository(context: Context): CreatureRepository {
        return RoomRepository()
    }

    fun provideCreatureGenerator(): CreatureGenerator {
        return CreatureGenerator()
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider
}