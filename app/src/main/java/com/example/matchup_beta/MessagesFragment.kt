package com.example.matchup_beta

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessagesFragment: Fragment(R.layout.fragment_messages) {
    private lateinit var messageAdapter: MessagesAdapter
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
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

            Toast.makeText(requireContext(), "Mensaje de ${message.name} marcado como leído", Toast.LENGTH_SHORT).show()
        })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = messageAdapter
    }
}