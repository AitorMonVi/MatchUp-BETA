package com.example.matchup_beta

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var isChangingTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // declaramos todos los button
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnScroll = findViewById<Button>(R.id.btn_scroll)
        val btnLikes = findViewById<Button>(R.id.btn_likes)
        val btnProfile = findViewById<Button>(R.id.btn_profile)
        val btnMessages = findViewById<Button>(R.id.btn_messages)
        val btnChat = findViewById<Button>(R.id.btn_chat)

        val switchMode = findViewById<SwitchCompat>(R.id.switchMode)

        val sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("nightMode", false)

        switchMode.isChecked = nightMode
        applyNightMode(nightMode)

        switchMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChangingTheme) return@setOnCheckedChangeListener
            isChangingTheme = true

            val editor = sharedPreferences.edit()
            editor.putBoolean("nightMode", isChecked)
            editor.apply()

            applyNightMode(isChecked)
            recreate()
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnScroll.setOnClickListener {
            val intent = Intent(this, ScrollActivity::class.java)
            startActivity(intent)
        }

        btnLikes.setOnClickListener {
            val intent = Intent(this, LikesActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnMessages.setOnClickListener {
            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun applyNightMode(enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun onResume() {
        super.onResume()
        isChangingTheme = false
    }
}
