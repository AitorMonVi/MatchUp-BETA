package com.example.matchup_beta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton= view.findViewById<Button>(R.id.enterButton)
        loginButton.setOnClickListener{
            (activity as? MainActivity)?.let { mainActivity ->
                mainActivity.replaceFragment(ScrollFragment(), "")
                mainActivity.showToolbarAndDrawer()
            }
        }
        return view
    }
}