package com.example.matchup_beta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LikesFragment : Fragment(R.layout.fragment_likes) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var likeUserAdapter: LikeUserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val userList = listOf(
            User("John", "Online", "image_url_1", 25),
            User("Alice", "Offline", "image_url_2", 30),
            User("Bob", "Online", "image_url_3", 28)
        )

        likeUserAdapter = LikeUserAdapter(userList,
            onLikeClick = { user ->
                Toast.makeText(context, "Like clicked for: ${user.name}", Toast.LENGTH_SHORT).show()
            },
            onDiscardClick = { user ->
                Toast.makeText(context, "Discard clicked for: ${user.name}", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = likeUserAdapter
    }
}
