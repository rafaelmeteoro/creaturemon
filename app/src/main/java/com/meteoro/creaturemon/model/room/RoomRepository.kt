package com.meteoro.creaturemon.model.room

import android.os.AsyncTask
import com.meteoro.creaturemon.app.CreaturemonApplication
import com.meteoro.creaturemon.model.Creature
import com.meteoro.creaturemon.model.CreatureRepository

class RoomRepository : CreatureRepository {

    private val creatureDao: CreatureDao = CreaturemonApplication.database.createDao()

    private class InsertAsyncTask internal constructor(private val dao: CreatureDao) :
        AsyncTask<Creature, Void, Void>() {
        override fun doInBackground(vararg params: Creature): Void? {
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val dao: CreatureDao) :
        AsyncTask<Creature, Void, Void>() {
        override fun doInBackground(vararg params: Creature): Void? {
            return null
        }
    }
}