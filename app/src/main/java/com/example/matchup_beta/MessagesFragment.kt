package com.example.matchup_beta

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MessagesFragment: Fragment(R.layout.fragment_messages) {

    private lateinit var messageAdapter: MessagesAdapter
    private lateinit var recycler: RecyclerView

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recycler = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        messageAdapter = MessagesAdapter(mutableListOf<MessageUser>()) { message ->
            Toast.makeText(requireContext(), "Click en ${message.name}", Toast.LENGTH_SHORT).show()
        }
        recycler.adapter = messageAdapter

        val okHttpClient = UnsafeClient.getUnsafeOkHttpClient()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://52.45.133.37:443/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = retrofitService.getMessages()
                Log.d("MessagesFragment", "Código de respuesta: ${response.code()}")
                if (response.isSuccessful) {
                    val messages = response.body() ?: emptyList()
                    Log.d("MessagesFragment", "Mensajes recibidos: $messages")
                    messageAdapter.updateData(messages)
                } else {
                    Log.e(
                        "MessagesFragment",
                        "Error en la respuesta: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("MessagesFragment", "Excepción al obtener mensajes: ${e.message}")
            }
        }

    }
}