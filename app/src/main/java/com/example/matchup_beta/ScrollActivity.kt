package com.example.matchup_beta

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_scroll)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val recycler: RecyclerView = findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(this)

        val userList = mutableListOf(
            User("John Doe", "Online","https://es.wikipedia.org/wiki/John_Doe_%28m%C3%BAsico%29",50 ),
            User("Churumbel", "Offline", "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.youtube.com%2Fchannel%2FUCx0W3FyVpJrM7N_O07sEIiA&psig=AOvVaw1i_Hy-Hj4TxL6ZgViLckrf&ust=1733772203946000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCNiom_TymIoDFQAAAAAdAAAAABAE", 43),
        )
        val userAdapter = UserAdapter(userList)
        recycler.adapter = userAdapter

        val updatedUserList = listOf(
            User("Carlos", "Online", "url_to_image", 28),
            User("Marta", "Offline", "url_to_image", 22)
        )
        userAdapter.updateData(updatedUserList)
    }
}