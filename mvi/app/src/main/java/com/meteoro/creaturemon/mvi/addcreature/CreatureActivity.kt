package com.meteoro.creaturemon.mvi.addcreature

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.meteoro.creaturemon.mvi.R
import com.meteoro.creaturemon.mvi.addcreature.avatars.AvatarAdapter
import com.meteoro.creaturemon.mvi.addcreature.avatars.AvatarBottomDialogFragment
import com.meteoro.creaturemon.mvi.data.model.AttributeStore
import com.meteoro.creaturemon.mvi.data.model.AttributeValue
import com.meteoro.creaturemon.mvi.data.model.Avatar
import com.meteoro.creaturemon.mvi.mvibase.MviView
import com.meteoro.creaturemon.mvi.util.CreaturemonViewModelFactory
import com.meteoro.creaturemon.mvi.util.visible
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_creature.*

class CreatureActivity : AppCompatActivity(),
    AvatarAdapter.AvatarListener, MviView<AddCreatureIntent, AddCreatureViewState> {

    private val avatarIntentPublisher = PublishSubject.create<AddCreatureIntent.AvatarIntent>()
    private val nameIntentPublisher = PublishSubject.create<AddCreatureIntent.NameIntent>()
    private val intelligenceIntentPublisher = PublishSubject.create<AddCreatureIntent.IntelligenceIntent>()
    private val strengthIntentPublisher = PublishSubject.create<AddCreatureIntent.StrengthIntent>()
    private val enduranceIntentPublisher = PublishSubject.create<AddCreatureIntent.EnduranceIntent>()
    private val saveIntentPublisher = PublishSubject.create<AddCreatureIntent.SaveIntent>()

    private val disposables = CompositeDisposable()

    private var avatarResourceId = 0 // TODO: save in ViewModel

    private val viewModel: AddCreatureViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
            .of(this, CreaturemonViewModelFactory.getInstance(this))
            .get(AddCreatureViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creature)

        configureUI()
        configureSpinnerAdapters()
        configureSpinnerListeners()
        configureEditText()
        configureClickListeners()
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

    override fun intents(): Observable<AddCreatureIntent> {
        return Observable.merge(
            avatarIntent(),
            nameIntent(),
            intelligenceIntent(),
            strengthIntent()
        ).mergeWith(enduranceIntent())
            .mergeWith(saveIntent())
    }

    override fun render(state: AddCreatureViewState) {
        if (state.isSaveComplete) {
            Toast.makeText(this, getString(R.string.creature_saved), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (state.isProcessing) {
            progressBar.visible = true
            saveButton.visible = false
        } else {
            progressBar.visible = false
            saveButton.visible = true
        }

        if (state.isDrawableSelected) {
            avatarImageView.setImageResource(state.creature.drawable)
            avatarResourceId = state.creature.drawable
            hideTapLabel()
        }

        hitPoints.text = state.creature.hitPoints.toString()

        if (state.error != null) {
            Toast.makeText(this, state.error.message, Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error creating creature: ${state.error.message}")
        }
    }

    private fun avatarIntent(): Observable<AddCreatureIntent.AvatarIntent> {
        return avatarIntentPublisher
    }

    private fun nameIntent(): Observable<AddCreatureIntent.NameIntent> {
        return nameIntentPublisher
    }

    private fun intelligenceIntent(): Observable<AddCreatureIntent.IntelligenceIntent> {
        return intelligenceIntentPublisher
    }

    private fun strengthIntent(): Observable<AddCreatureIntent.StrengthIntent> {
        return strengthIntentPublisher
    }

    private fun enduranceIntent(): Observable<AddCreatureIntent.EnduranceIntent> {
        return enduranceIntentPublisher
    }

    private fun saveIntent(): Observable<AddCreatureIntent.SaveIntent> {
        return saveIntentPublisher
    }

    private fun configureUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.add_creature)
    }

    private fun configureSpinnerAdapters() {
        intelligence.adapter = ArrayAdapter<AttributeValue>(
            this, android.R.layout.simple_spinner_dropdown_item, AttributeStore.INTELLIGENCE
        )
        strength.adapter = ArrayAdapter<AttributeValue>(
            this, android.R.layout.simple_spinner_dropdown_item, AttributeStore.STRENGTH
        )
        endurance.adapter = ArrayAdapter<AttributeValue>(
            this, android.R.layout.simple_spinner_dropdown_item, AttributeStore.ENDURANCE
        )
    }

    private fun configureSpinnerListeners() {
        intelligence.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                intelligenceIntentPublisher.onNext(AddCreatureIntent.IntelligenceIntent(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        strength.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                strengthIntentPublisher.onNext(AddCreatureIntent.StrengthIntent(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        endurance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                enduranceIntentPublisher.onNext(AddCreatureIntent.EnduranceIntent(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun configureEditText() {
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameIntentPublisher.onNext(AddCreatureIntent.NameIntent(s.toString()))
            }
        })
    }

    private fun configureClickListeners() {
        avatarImageView.setOnClickListener {
            val bottomDialogFragment = AvatarBottomDialogFragment.newInstance()
            bottomDialogFragment.show(supportFragmentManager, "AvatarBottomDialogFragment")
        }

        saveButton.setOnClickListener {
            saveIntentPublisher.onNext(
                AddCreatureIntent.SaveIntent(
                    avatarResourceId,
                    nameEditText.text.toString(),
                    intelligence.selectedItemPosition,
                    strength.selectedItemPosition,
                    endurance.selectedItemPosition
                )
            )
        }
    }

    override fun avatarClicked(avatar: Avatar) {
        avatarIntentPublisher.onNext(AddCreatureIntent.AvatarIntent(avatar.drawable))
        hideTapLabel()
    }

    private fun hideTapLabel() {
        tapLabel.visibility = View.INVISIBLE
    }

    companion object {
        private const val TAG = "CreatureActivity"
    }
}