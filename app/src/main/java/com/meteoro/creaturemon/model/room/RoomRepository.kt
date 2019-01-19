package com.meteoro.creaturemon.model.room

import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.meteoro.creaturemon.app.CreaturemonApplication
import com.meteoro.creaturemon.model.Creature
import com.meteoro.creaturemon.model.CreatureRepository

class RoomRepository : CreatureRepository {

    private val creatureDao: CreatureDao = CreaturemonApplication.database.createDao()

    private val allCreatures: LiveData<List<Creature>>

    init {
        allCreatures = creatureDao.getAllCreatures()
    }

    override fun saveCreature(creature: Creature) {
        InsertAsyncTask(creatureDao).execute(creature)
    }

    override fun getAllCreaatures(): LiveData<List<Creature>> = allCreatures

    override fun clearAllCreatures() {
        val creatureArray = allCreatures.value?.toTypedArray()
        if (creatureArray != null) {
            DeleteAsyncTask(creatureDao).execute(*creatureArray)
        }
    }

    private class InsertAsyncTask internal constructor(private val dao: CreatureDao) :
        AsyncTask<Creature, Void, Void>() {
        override fun doInBackground(vararg params: Creature): Void? {
            dao.insert(params[0])
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val dao: CreatureDao) :
        AsyncTask<Creature, Void, Void>() {
        override fun doInBackground(vararg params: Creature): Void? {
            dao.clearCreatures(*params)
            return null
        }
    }
}