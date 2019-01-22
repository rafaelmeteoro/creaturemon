package com.meteoro.creaturemon.mvp.view.allcreatures

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meteoro.creaturemon.mvp.R
import com.meteoro.creaturemon.mvp.app.inflate
import com.meteoro.creaturemon.mvp.model.Creature

class CreatureAdapter(private val creatures: MutableList<Creature>) :
    RecyclerView.Adapter<CreatureAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creature))
    }

    override fun onBindViewHolder(holder: CreatureAdapter.ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    override fun getItemCount(): Int = creatures.size

    fun updateCreatures(creatures: List<Creature>) {
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var creature: Creature

        fun bind(creature: Creature) {
            this.creature = creature
            // TODO: populate views
        }
    }
}