package com.meteoro.creaturemon.mvi.allcreatures

import com.meteoro.creaturemon.mvi.data.model.Creature
import com.meteoro.creaturemon.mvi.data.model.CreatureAttributes
import com.meteoro.creaturemon.mvi.data.model.CreatureGenerator
import com.meteoro.creaturemon.mvi.data.repository.CreatureRepository
import com.meteoro.creaturemon.mvi.util.schedulers.BaseSchedulerProvider
import com.meteoro.creaturemon.mvi.util.schedulers.ImmediateSchedulerProvider
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AllCreaturesViewModelTest {

    @Mock
    private lateinit var creatureRepository: CreatureRepository
    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var generator: CreatureGenerator
    private lateinit var viewModel: AllCreaturesViewModel
    private lateinit var testObserver: TestObserver<AllCreaturesViewState>
    private lateinit var creatures: List<Creature>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = ImmediateSchedulerProvider()

        generator = CreatureGenerator()

        viewModel = AllCreaturesViewModel(
            AllCreaturesProcessorHolder(
                creatureRepository,
                schedulerProvider
            )
        )

        creatures = listOf(
            generator.generateCreature(CreatureAttributes(3, 7, 10), "Creature 1", 1),
            generator.generateCreature(CreatureAttributes(7, 10, 3), "Creature 2", 1),
            generator.generateCreature(CreatureAttributes(10, 3, 7), "Creature 3", 1)
        )

        testObserver = viewModel.states().test()
    }

    @Test
    fun loadAllCreaturesFromRepositoryAndLoadIntoView() {
        `when`(creatureRepository.getAllCreatures()).thenReturn(Observable.just(creatures))

        viewModel.processIntent(Observable.just(AllCreaturesIntent.LoadAllCreaturesIntent))

        testObserver.assertValueAt(1, AllCreaturesViewState::isLoading)
        testObserver.assertValueAt(2) { allCreaturesViewState: AllCreaturesViewState ->
            !allCreaturesViewState.isLoading
        }
    }

    @Test
    fun errorLoadingCreaturesShowsError() {
        `when`(creatureRepository.getAllCreatures()).thenReturn(Observable.error(Exception()))

        viewModel.processIntent(Observable.just(AllCreaturesIntent.LoadAllCreaturesIntent))

        testObserver.assertValueAt(2) { state -> state.error != null }
    }
}