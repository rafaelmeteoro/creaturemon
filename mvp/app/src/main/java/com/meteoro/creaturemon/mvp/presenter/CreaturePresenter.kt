package com.meteoro.creaturemon.mvp.presenter

import com.meteoro.creaturemon.mvp.model.*

class CreaturePresenter(private val generator: CreatureGenerator = CreatureGenerator()) :
    BasePresenter<CreatureContract.View>(), CreatureContract.Presenter {

    private lateinit var creature: Creature

    private var name = ""
    private var intelligence = 0
    private var strength = 0
    private var endurance = 0
    private var drawable = 0

    private fun updateCreature() {
        val attributes = CreatureAttributes(intelligence, strength, endurance)
        creature = generator.generateCreature(attributes, name, drawable)
        getView()?.showHitPoints(creature.hitPoints.toString())
    }

    override fun updateName(name: String) {
        this.name = name
        updateCreature()
    }

    override fun attributeSelected(attributeType: AttributeType, position: Int) {
        when (attributeType) {
            AttributeType.INTELLIGENCE ->
                intelligence = AttributeStore.INTELLIGENCE[position].value
            AttributeType.STRENGTH ->
                strength = AttributeStore.STRENGTH[position].value
            AttributeType.ENDURANCE ->
                endurance = AttributeStore.ENDURANCE[position].value
        }
        updateCreature()
    }

    override fun drawableSelected(drawable: Int) {
        this.drawable = drawable
        getView()?.showAvatarDrawable(drawable)
        updateCreature()
    }

    override fun isDrawableSelected(): Boolean {
        return drawable != 0
    }
}