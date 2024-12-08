package com.example.matchup_beta

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LikesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var likeUserAdapter: LikeUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_likes)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val userList = listOf(
            User("John", "Online", "image_url_1", 25),
            User("Alice", "Offline", "image_url_2", 30),
            User("Bob", "Online", "image_url_3", 28)
        )
        likeUserAdapter = LikeUserAdapter(userList,
            onLikeClick = { user ->
              Toast.makeText(this, "Like clicked for: ${user.name}", Toast.LENGTH_SHORT).show()
            },
            onDiscardClick = { user ->
                Toast.makeText(this,"Discard clicked for: ${user.name}",Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = likeUserAdapter
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
