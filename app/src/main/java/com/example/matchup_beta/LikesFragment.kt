package com.example.matchup_beta

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LikesFragment : Fragment(R.layout.fragment_likes) {

    private lateinit var recycler: RecyclerView
    private lateinit var likeUserAdapter: LikeUserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val retrofit = RetrofitService.MatchUPAPI.API()
        recycler= view.findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        likeUserAdapter = LikeUserAdapter(mutableListOf(),
            onLikeClick = { user ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val like = Like(id_user_giver = 1, id_user_receiver = user.id)
                        val response = retrofit.giveLike(like)
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Like registrado para ${user.name}", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("LikesFragment: ", "Error al dar like: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Excepción: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onDiscardClick = { user ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val like = Like(id_user_giver = user.id, id_user_receiver = 1)
                        val response = retrofit.deleteLike(like)
                        if(response.isSuccessful) {
                            val newList = likeUserAdapter.currentList.filter { it.id != user.id }
                            likeUserAdapter.updateData(newList)
                            Toast.makeText(context, "Eliminado like para ${user.name}", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("LikesFragment: ", "Error al eliminar like: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Log.d( "LikesFragment: ","Excepción: ${e.message}")
                    }
                }
            }
        )
        recycler.adapter = likeUserAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val likesResponse = retrofit.getLikes(1)
                Log.d("LikesFragment", "Likes response code: ${likesResponse.code()}")
                if(likesResponse.isSuccessful) {
                    val likes = likesResponse.body()?.likes_received ?: emptyList()
                    Log.d("LikesFragment", "Likes received: $likes")
                    val userDeferreds = likes.map { like ->
                        async { retrofit.getUser(like.user_giver) }
                    }
                    val userResponses = userDeferreds.awaitAll()
                    userResponses.forEachIndexed { index, response ->
                        Log.d("LikesFragment", "User response $index code: ${response.code()}")
                    }
                    val users = userResponses.mapNotNull { response ->
                        if (response.isSuccessful) response.body() else null
                    }
                    Log.d("LikesFragment", "Users obtained: $users")
                    likeUserAdapter.updateData(users)
                } else {
                    Toast.makeText(context, "Error fetching likes: ${likesResponse.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("LikesFragment", "Exception: ${e.message}", e)
            }
        }
    }
}
