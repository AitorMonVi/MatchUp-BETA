package com.example.matchup_beta

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ScrollFragment : Fragment(R.layout.fragment_scroll) {

    private lateinit var recycler: RecyclerView
    private lateinit var userAdapter: UserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // recyclerView
        recycler = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        userAdapter = UserAdapter(mutableListOf())
        recycler.adapter = userAdapter


        // configuració retrofit
        val retrofit = RetrofitService.MatchUPAPI.API()


        // crida a la api
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = retrofit.getUsers()
                Log.d("ScrollFragment", "Código de respuesta: ${response.code()}")
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    Log.d("ScrollFragment", "Usuarios recibidos: $users")
                    userAdapter.updateData(users)
                } else {
                    Log.e(
                        "ScrollFragment",
                        "Error en la respuesta: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ScrollFragment", "Excepción al obtener usuarios: ${e.message}")
            }
        }
    }
}