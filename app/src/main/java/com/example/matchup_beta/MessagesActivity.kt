package com.example.matchup_beta

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessagesActivity : AppCompatActivity() {
    private lateinit var messageAdapter: MessagesAdapter
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_messages)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val messageUserList = mutableListOf(
            MessageUser("Juan", 25, "image_url_1","Hola, ¿cómo estás?", 3),
            MessageUser("Ana", 22, "image_url_2","¿Nos vemos hoy?", 0),
            MessageUser("Carlos", 30, "image_url_3","Estoy ocupado, hablamos luego.", 1)
        )
        messageAdapter = MessagesAdapter(messageUserList, onItemClick = { message ->
            val updatedMessage = message.copy(unreadMessagesCount = 0)
            val updatedList = messageUserList.toMutableList()
            val index = updatedList.indexOf(message)
            updatedList[index] = updatedMessage
            messageAdapter.updateData(updatedList)

            Toast.makeText(this, "Mensaje de ${message.name} marcado como leído", Toast.LENGTH_SHORT).show()
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter
    }
}