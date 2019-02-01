package com.meteoro.creaturemon.mvi.allcreatures

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.meteoro.creaturemon.mvi.R
import com.meteoro.creaturemon.mvi.addcreature.CreatureActivity
import com.meteoro.creaturemon.mvi.mvibase.MviView
import com.meteoro.creaturemon.mvi.util.CreaturemonViewModelFactory
import com.meteoro.creaturemon.mvi.util.visible
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_all_creatures.*
import kotlinx.android.synthetic.main.content_all_creatures.*

class AllCreaturesActivity : AppCompatActivity(),
    MviView<AllCreaturesIntent, AllCreaturesViewState> {

    private val adapter = CreatureAdapter(mutableListOf())

    private val clearAllCreaturesPublisher =
        PublishSubject.create<AllCreaturesIntent.ClearAllCreaturesIntent>()

    private val disposables = CompositeDisposable()

    private val viewModel: AllCreaturesViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
            .of(this, CreaturemonViewModelFactory.getInstance(this))
            .get(AllCreaturesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_creatures)
        setSupportActionBar(toolbar)

        creaturesRecyclerView.layoutManager = LinearLayoutManager(this)
        creaturesRecyclerView.adapter = adapter

        fab.setOnClickListener {
            startActivity(Intent(this, CreatureActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntent(intents())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_all -> {
                clearAllCreaturesPublisher.onNext(AllCreaturesIntent.ClearAllCreaturesIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun intents(): Observable<AllCreaturesIntent> {
        return Observable.merge(
            loadIntent(),
            clearIntent()
        )
    }

    override fun render(state: AllCreaturesViewState) {
        progressBar.visible = state.isLoading

        if (state.creatures.isEmpty()) {
            creaturesRecyclerView.visible = false
            emptyState.visible = true
        } else {
            creaturesRecyclerView.visible = false
            emptyState.visible = false
            adapter.updateCreatures(state.creatures)
        }

        if (state.error != null) {
            Toast.makeText(this, "Error loading creatures.", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error loading creatures: ${state.error.localizedMessage}")
        }
    }

    private fun loadIntent(): Observable<AllCreaturesIntent.LoadAllCreaturesIntent> {
        return Observable.just(AllCreaturesIntent.LoadAllCreaturesIntent)
    }

    private fun clearIntent(): Observable<AllCreaturesIntent.ClearAllCreaturesIntent> {
        return clearAllCreaturesPublisher
    }

    companion object {
        private const val TAG = "AllCreaturesActivity"
    }
}