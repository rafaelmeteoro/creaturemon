package com.meteoro.creaturemon.mvi.addcreature

import androidx.lifecycle.ViewModel
import com.meteoro.creaturemon.mvi.addcreature.AddCreatureAction.*
import com.meteoro.creaturemon.mvi.addcreature.AddCreatureIntent.*
import com.meteoro.creaturemon.mvi.addcreature.AddCreatureResult.*
import com.meteoro.creaturemon.mvi.data.model.CreatureAttributes
import com.meteoro.creaturemon.mvi.data.model.CreatureGenerator
import com.meteoro.creaturemon.mvi.mvibase.MviViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class AddCreatureViewModel(
    private val actionProcessorHolder: AddCreatureProcessorHolder
) : ViewModel(), MviViewModel<AddCreatureIntent, AddCreatureViewState> {

    private val intentsSubject: PublishSubject<AddCreatureIntent> = PublishSubject.create()
    private val statesObservable: Observable<AddCreatureViewState> = compose()

    override fun processIntent(intents: Observable<AddCreatureIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<AddCreatureViewState> = statesObservable

    private fun compose(): Observable<AddCreatureViewState> {
        return intentsSubject
            .map(this::actionFromIntent)
            .compose(actionProcessorHolder.actionProcessor)
            .scan(AddCreatureViewState.default(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun actionFromIntent(intent: AddCreatureIntent): AddCreatureAction {
        return when (intent) {
            is AvatarIntent -> AvatarAction(intent.drawable)
            is NameIntent -> NameAction(intent.name)
            is IntelligenceIntent -> IntelligenceAction(intent.intelligenceIndex)
            is StrengthIntent -> StrengthAction(intent.strengthIndex)
            is EnduranceIntent -> EnduranceAction(intent.enduranceIndex)
            is SaveIntent -> SaveAction(
                intent.drawable, intent.name, intent.intelligenceIndex, intent.strengthIndex,
                intent.enduranceIndex
            )
        }
    }

    companion object {
        private val generator = CreatureGenerator()

        private val reducer = BiFunction { previousState: AddCreatureViewState, result: AddCreatureResult ->
            when (result) {
                is AvatarResult -> reduceAvatar(previousState, result)
                is NameResult -> reduceName(previousState, result)
                is IntelligenceResult -> reduceIntelligence(previousState, result)
                is StrengthResult -> reduceStrength(previousState, result)
                is EnduranceResult -> reduceEndurance(previousState, result)
                is SaveResult -> reduceSave(previousState, result)
            }
        }

        private fun reduceAvatar(
            previousState: AddCreatureViewState,
            result: AvatarResult
        ): AddCreatureViewState = when (result) {
            is AvatarResult.Success -> {
                previousState.copy(
                    isProcessing = false,
                    error = null,
                    creature = generator.generateCreature(
                        previousState.creature.attributes, previousState.creature.name, result.drawable
                    ),
                    isDrawableSelected = (result.drawable != 0)
                )
            }
            is AvatarResult.Failure -> {
                previousState.copy(isProcessing = false, error = result.error)
            }
            is AvatarResult.Processing -> {
                previousState.copy(isProcessing = true, error = null)
            }
        }

        private fun reduceName(
            previousState: AddCreatureViewState,
            result: NameResult
        ): AddCreatureViewState = when (result) {
            is NameResult.Success -> {
                previousState.copy(
                    isProcessing = false,
                    error = null,
                    creature = generator.generateCreature(
                        previousState.creature.attributes, result.name, previousState.creature.drawable
                    )
                )
            }
            is NameResult.Failure -> {
                previousState.copy(isProcessing = false, error = result.error)
            }
            is NameResult.Processing -> {
                previousState.copy(isProcessing = true, error = null)
            }
        }

        private fun reduceIntelligence(
            previousState: AddCreatureViewState,
            result: IntelligenceResult
        ): AddCreatureViewState = when (result) {
            is IntelligenceResult.Success -> {
                val attributes = CreatureAttributes(
                    result.intelligence,
                    previousState.creature.attributes.strength,
                    previousState.creature.attributes.endurance
                )
                previousState.copy(
                    isProcessing = false,
                    error = null,
                    creature = generator.generateCreature(
                        attributes, previousState.creature.name, previousState.creature.drawable
                    )
                )
            }
            is IntelligenceResult.Failure -> {
                previousState.copy(isProcessing = false, error = result.error)
            }
            is IntelligenceResult.Processing -> {
                previousState.copy(isProcessing = true, error = null)
            }
        }

        private fun reduceStrength(
            previousState: AddCreatureViewState,
            result: StrengthResult
        ): AddCreatureViewState = when (result) {
            is StrengthResult.Success -> {
                val attributes = CreatureAttributes(
                    previousState.creature.attributes.intelligence,
                    result.strength,
                    previousState.creature.attributes.endurance
                )
                previousState.copy(
                    isProcessing = false,
                    error = null,
                    creature = generator.generateCreature(
                        attributes, previousState.creature.name, previousState.creature.drawable
                    )
                )
            }
            is StrengthResult.Failure -> {
                previousState.copy(isProcessing = false, error = result.error)
            }
            is StrengthResult.Processing -> {
                previousState.copy(isProcessing = true, error = null)
            }
        }

        private fun reduceEndurance(
            previousState: AddCreatureViewState,
            result: EnduranceResult
        ): AddCreatureViewState = when (result) {
            is EnduranceResult.Success -> {
                val attributes = CreatureAttributes(
                    previousState.creature.attributes.intelligence,
                    previousState.creature.attributes.strength,
                    result.endurance
                )
                previousState.copy(
                    isProcessing = false,
                    error = null,
                    creature = generator.generateCreature(
                        attributes, previousState.creature.name, previousState.creature.drawable
                    )
                )
            }
            is EnduranceResult.Failure -> {
                previousState.copy(isProcessing = false, error = result.error)
            }
            is EnduranceResult.Processing -> {
                previousState.copy(isProcessing = true, error = null)
            }
        }

        private fun reduceSave(
            previousState: AddCreatureViewState,
            result: SaveResult
        ): AddCreatureViewState = when (result) {
            is SaveResult.Success -> {
                previousState.copy(isProcessing = false, isSaveComplete = true, error = null)
            }
            is SaveResult.Failure -> {
                previousState.copy(isProcessing = false, error = result.error)
            }
            is SaveResult.Processing -> {
                previousState.copy(isProcessing = true, error = null)
            }
        }
    }
}