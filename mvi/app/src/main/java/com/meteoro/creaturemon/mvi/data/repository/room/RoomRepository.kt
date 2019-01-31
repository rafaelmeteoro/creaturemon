package com.meteoro.creaturemon.mvi.data.repository.room

import android.os.AsyncTask
import com.meteoro.creaturemon.mvi.app.CreaturemonApplication
import com.meteoro.creaturemon.mvi.data.model.Creature
import com.meteoro.creaturemon.mvi.data.repository.CreatureRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RoomRepository : CreatureRepository {

    private val creatureDao: CreatureDao = CreaturemonApplication.database.creatureDao()

    private val allCreatures: Observable<List<Creature>>

    private var saveSubject: PublishSubject<Boolean> = PublishSubject.create()
    private var clearSubject: PublishSubject<Boolean> = PublishSubject.create()

    init {
        allCreatures = creatureDao.getAllCreatures()
    }

    override fun saveCreature(creature: Creature): Observable<Boolean> {
        saveSubject = PublishSubject.create()
        if (canSaveCreature(creature)) {
            InsertAsyncTask(creatureDao) {
                saveSubject.onNext(true)
            }.execute(creature)
        } else {
            saveSubject.onError(Error("Please fill in all fields."))
        }
        return saveSubject
    }

    private fun canSaveCreature(creature: Creature): Boolean {
        return creature.drawable != 0 &&
                creature.name.isNotEmpty() &&
                creature.attributes.intelligence != 0 &&
                creature.attributes.strength != 0 &&
                creature.attributes.endurance != 0
    }

    override fun getAllCreatures(): Observable<List<Creature>> = allCreatures

    override fun clearAllCreatures(): Observable<Boolean> {
        DeleteAsyncTask(creatureDao) {
            clearSubject.onNext(true)
        }.execute()
        return clearSubject
    }

    private class InsertAsyncTask internal constructor(
        private val dao: CreatureDao,
        private val completed: () -> Unit
    ) : AsyncTask<Creature, Void, Void>() {
        override fun doInBackground(vararg params: Creature): Void? {
            dao.insert(params[0])
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            completed()
        }
    }

    private class DeleteAsyncTask internal constructor(
        private val dao: CreatureDao,
        private val completed: () -> Unit
    ) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            dao.clearAllCreatures()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            completed()
        }
    }
}