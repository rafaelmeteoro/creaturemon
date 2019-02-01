package com.meteoro.creaturemon.mvi.addcreature

import com.meteoro.creaturemon.mvi.addcreature.AddCreatureAction.*
import com.meteoro.creaturemon.mvi.addcreature.AddCreatureResult.*
import com.meteoro.creaturemon.mvi.data.model.AttributeStore
import com.meteoro.creaturemon.mvi.data.model.CreatureAttributes
import com.meteoro.creaturemon.mvi.data.model.CreatureGenerator
import com.meteoro.creaturemon.mvi.data.repository.CreatureRepository
import com.meteoro.creaturemon.mvi.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class AddCreatureProcessorHolder(
    private val creatureRepository: CreatureRepository,
    private val creatureGenerator: CreatureGenerator,
    private val schedulerProvider: BaseSchedulerProvider
) {
    private val avatarProcessor =
        ObservableTransformer<AvatarAction, AvatarResult> { actions ->
            actions
                .map { action -> AvatarResult.Success(action.drawable) }
                .cast(AvatarResult::class.java)
                .onErrorReturn(AvatarResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(AvatarResult.Processing)
        }

    private val nameProcessor =
        ObservableTransformer<NameAction, NameResult> { actions ->
            actions
                .map { action -> NameResult.Success(action.name) }
                .cast(NameResult::class.java)
                .onErrorReturn(NameResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(NameResult.Processing)
        }

    private val intelligenceProcessor =
        ObservableTransformer<IntelligenceAction, IntelligenceResult> { actions ->
            actions
                .map { action ->
                    IntelligenceResult.Success(
                        AttributeStore.INTELLIGENCE[action.intelligenceIndex].value
                    )
                }
                .cast(IntelligenceResult::class.java)
                .onErrorReturn(IntelligenceResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(IntelligenceResult.Processing)
        }

    private val strengthProcessor =
        ObservableTransformer<StrengthAction, StrengthResult> { actions ->
            actions
                .map { action ->
                    StrengthResult.Success(
                        AttributeStore.STRENGTH[action.strengthIndex].value
                    )
                }
                .cast(StrengthResult::class.java)
                .onErrorReturn(StrengthResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(StrengthResult.Processing)
        }

    private val enduranceProcessor =
        ObservableTransformer<EnduranceAction, EnduranceResult> { actions ->
            actions
                .map { action ->
                    EnduranceResult.Success(
                        AttributeStore.ENDURANCE[action.enduranceIndex].value
                    )
                }
                .cast(EnduranceResult::class.java)
                .onErrorReturn(EnduranceResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(EnduranceResult.Processing)
        }

    private val saveProcessor =
        ObservableTransformer<SaveAction, SaveResult> { actions ->
            actions.flatMap { action ->
                val attributes = CreatureAttributes(
                    AttributeStore.INTELLIGENCE[action.intelligenceIndex].value,
                    AttributeStore.STRENGTH[action.strengthIndex].value,
                    AttributeStore.ENDURANCE[action.enduranceIndex].value
                )
                val creature = creatureGenerator.generateCreature(attributes, action.name, action.drawable)
                creatureRepository.saveCreature(creature)
                    .map { SaveResult.Success }
                    .cast(SaveResult::class.java)
                    .onErrorReturn(SaveResult::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(SaveResult.Processing)
            }
        }

    internal var actionProcessor =
        ObservableTransformer<AddCreatureAction, AddCreatureResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(AvatarAction::class.java).compose(avatarProcessor),
                    shared.ofType(NameAction::class.java).compose(nameProcessor),
                    shared.ofType(IntelligenceAction::class.java).compose(intelligenceProcessor),
                    shared.ofType(StrengthAction::class.java).compose(strengthProcessor)
                ).mergeWith(shared.ofType(EnduranceAction::class.java).compose(enduranceProcessor))
                    .mergeWith(shared.ofType(SaveAction::class.java).compose(saveProcessor))
                    .mergeWith(
                        // Error for not implemented actions
                        shared.filter { v ->
                            v !is AvatarAction
                                    && v !is NameAction
                                    && v !is IntelligenceAction
                                    && v !is StrengthAction
                                    && v !is EnduranceAction
                                    && v !is SaveAction
                        }.flatMap { w ->
                            Observable.error<AddCreatureResult>(
                                IllegalArgumentException("Unknown Action type: $w")
                            )
                        }
                    )
            }
        }
}