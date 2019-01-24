package com.meteoro.creaturemon.mvp.presenter

import com.meteoro.creaturemon.mvp.model.AttributeType
import com.meteoro.creaturemon.mvp.model.Creature
import com.meteoro.creaturemon.mvp.model.CreatureAttributes
import com.meteoro.creaturemon.mvp.model.CreatureGenerator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class CreaturePresenterTest {

    private lateinit var presenter: CreaturePresenter

    @Mock
    lateinit var view: CreatureContract.View

    @Mock
    lateinit var mockGenerator: CreatureGenerator

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = CreaturePresenter(mockGenerator)
        presenter.setView(view)
    }

    @Test
    fun testIntelligenceSelected() {
        val attriutes = CreatureAttributes(10, 0, 0)
        val stubCreature = Creature(attriutes, 50)
        `when`(mockGenerator.generateCreature(attriutes)).thenReturn(stubCreature)

        presenter.attributeSelected(AttributeType.INTELLIGENCE, 3)

        verify(view, times(1)).showHitPoints("50")
    }
}