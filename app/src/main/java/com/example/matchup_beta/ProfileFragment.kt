package com.example.matchup_beta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.transition.Visibility

class ProfileFragment : Fragment() {

    private lateinit var nameText : EditText
    private lateinit var ageText : EditText
    private lateinit var descriptionText : EditText
    private lateinit var editProfile : ImageButton
    private lateinit var makeChanges : Button

    private var click : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // declaramos botones, text, etc
        nameText = view.findViewById(R.id.name)
        ageText = view.findViewById(R.id.age)
        descriptionText = view.findViewById(R.id.description)
        editProfile = view.findViewById(R.id.edit_profile)
        makeChanges = view.findViewById(R.id.make_changes)

        // hacemos invisible el boton para hacer cambios
        makeChanges.visibility = View.INVISIBLE
        nameText.isEnabled = false
        ageText.isEnabled = false
        descriptionText.isEnabled = false

        editProfile.setOnClickListener{
            if (!click) {
                makeChanges.visibility = View.VISIBLE
                nameText.isEnabled = true
                ageText.isEnabled = true
                descriptionText.isEnabled = true
                click = true
            } else {
                makeChanges.visibility = View.INVISIBLE
                nameText.isEnabled = false
                ageText.isEnabled = false
                descriptionText.isEnabled = false
                click = false
            }
        }

        makeChanges.setOnClickListener{
            makeChanges.visibility = View.INVISIBLE
            nameText.isEnabled = false
            ageText.isEnabled = false
            descriptionText.isEnabled = false
            click = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        return view
    }
}
