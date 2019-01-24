package com.meteoro.creaturemon.mvp.view.allcreatures

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.meteoro.creaturemon.mvp.R
import com.meteoro.creaturemon.mvp.presenter.AllCreaturesContract
import com.meteoro.creaturemon.mvp.presenter.AllCreaturesPresenter
import com.meteoro.creaturemon.mvp.view.creature.CreatureActivity
import kotlinx.android.synthetic.main.activity_all_creatures.*
import kotlinx.android.synthetic.main.content_all_creatures.*

class AllCreaturesActivity : AppCompatActivity(), AllCreaturesContract.View {

    private val adapter = CreatureAdapter(mutableListOf())

    private val presenter = AllCreaturesPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_creatures)
        setSupportActionBar(toolbar)

        presenter.setView(this)

        presenter.getAllCreatures().observe(this, Observer { creatures ->
            creatures?.let {
                adapter.updateCreatures(creatures)
            }
        })

        creaturesRecyclerView.layoutManager = LinearLayoutManager(this)
        creaturesRecyclerView.adapter = adapter

        fab.setOnClickListener {
            startActivity(Intent(this, CreatureActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_all -> {
                presenter.clearAllCreatures()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showCreaturesCleared() {
        Toast.makeText(this, getString(R.string.creatures_cleared), Toast.LENGTH_SHORT).show()
    }
}