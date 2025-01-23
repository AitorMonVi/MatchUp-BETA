package com.example.matchup_beta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BottomMenuFragment : Fragment(R.layout.fragment_bottom_menu) {
    private lateinit var scroll:ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        scroll = view.findViewById<ImageButton>(R.id.scrollButton)
        scroll.setOnClickListener {
            (activity as? MainActivity)?.let { mainActivity ->
                mainActivity.replaceFragment(ScrollFragment(), "")
                mainActivity.showToolbarAndDrawer()
            }
        }

    }
}