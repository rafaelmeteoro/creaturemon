package com.meteoro.creaturemon.view.creature

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.meteoro.creaturemon.R
import com.meteoro.creaturemon.databinding.ActivityCreatureBinding
import com.meteoro.creaturemon.model.AttributeStore
import com.meteoro.creaturemon.model.AttributeType
import com.meteoro.creaturemon.model.AttributeValue
import com.meteoro.creaturemon.model.Avatar
import com.meteoro.creaturemon.view.avatars.AvatarAdapter
import com.meteoro.creaturemon.view.avatars.AvatarBottomDialogFragment
import com.meteoro.creaturemon.viewmodel.CreatureViewModel
import kotlinx.android.synthetic.main.activity_creature.*

class CreatureActivity : AppCompatActivity(), AvatarAdapter.AvatarListener {

    private lateinit var viewModel: CreatureViewModel

    lateinit var binding: ActivityCreatureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_creature)

        viewModel = ViewModelProviders.of(this).get(CreatureViewModel::class.java)
        binding.viewmodel = viewModel

        configureUI()
        configureSpinnerAdapters()
        configureSpinnerListeners()
        configureClickListener()
        configureLiveDataObservers()
    }

    private fun configureUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.add_creature)
        if (viewModel.drawable != 0) hideTapLabel()
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
                viewModel.attributeSelected(AttributeType.INTELLIGENCE, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        strength.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.attributeSelected(AttributeType.STRENGTH, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        endurance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.attributeSelected(AttributeType.ENDURANCE, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun configureClickListener() {
        avatarImageView.setOnClickListener {
            val bottomDialogFragment = AvatarBottomDialogFragment.newInstance()
            bottomDialogFragment.show(supportFragmentManager, "AvatarBottomDialogFragment")
        }
    }

    private fun configureLiveDataObservers() {
        viewModel.getCreatureLiveData().observe(this, Observer { creature ->
            creature?.let {
                hitPoints.text = creature.hitPoints.toString()
                avatarImageView.setImageResource(creature.drawable)
                nameEditText.setText(creature.name)
            }
        })
        viewModel.getSaveLiveData().observe(this, Observer { saved ->
            saved?.let {
                if (saved) {
                    Toast.makeText(this, getString(R.string.creature_saved), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, getString(R.string.error_saving_creature), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun avatarClicked(avatar: Avatar) {
        viewModel.drawableSelected(avatar.drawable)
        hideTapLabel()
    }

    private fun hideTapLabel() {
        tapLabel.visibility = View.INVISIBLE
    }
}