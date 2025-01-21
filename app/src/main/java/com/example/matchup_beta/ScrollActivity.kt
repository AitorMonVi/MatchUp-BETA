package com.example.matchup_beta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollFragment : Fragment(R.layout.fragment_scroll) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView
        val recycler: RecyclerView = view.findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // llista d'usuaris
        val userList = mutableListOf(
            User("John Doe", "Online", "https://es.wikipedia.org/wiki/John_Doe_%28m%C3%BAsico%29", 50),
            User("Churumbel", "Offline", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.youtube.com%2Fchannel%2FUCx0W3FyVpJrM7N_O07sEIiA&psig=AOvVaw1i_Hy-Hj4TxL6ZgViLckrf&ust=1733772203946000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCNiom_TymIoDFQAAAAAdAAAAABAE", 43)
        )

        // adapter RecyclerView
        val userAdapter = UserAdapter(userList)
        recycler.adapter = userAdapter

        // actualitza llista usuaris
        val updatedUserList = listOf(
            User("Carlos", "Online", "url_to_image", 28),
            User("Marta", "Offline", "url_to_image", 22)
        )
        userAdapter.updateData(updatedUserList)
    }
}
