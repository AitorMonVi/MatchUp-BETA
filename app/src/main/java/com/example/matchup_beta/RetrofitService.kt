package com.example.matchup_beta

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): Response<User>

    @GET("messages")
    suspend fun getMessages(): Response<List<MessageUser>>

    @GET("likes/{user_giver}")
    suspend fun getLikes(@Path("user_giver") userId: Int): Response<LikesResponse>

    @POST("likes")
    suspend fun giveLike(@Body like: Like): Response<Any>

    @HTTP(method = "DELETE", path = "likes", hasBody = true)
    suspend fun deleteLike(@Body like: Like): Response<Any>
}