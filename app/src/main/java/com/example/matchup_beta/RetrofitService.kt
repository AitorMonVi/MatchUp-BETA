package com.example.matchup_beta

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.http.Body
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PUT

interface RetrofitService {

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): Response<User>

    @POST("users")
    suspend fun createUser(@Body usuario: UserCreate): Response<Any>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @PUT("users/update")
    suspend fun updateUser(@Header("Authorization") token: String, @Body data: UserUpdate): Response<Any>

    @GET("messages")
    suspend fun getMessages(): Response<List<MessageUser>>

    @GET("likes/{user_giver}")
    suspend fun getLikes(@Path("user_giver") userId: Int): Response<LikesResponse>

    @POST("likes")
    suspend fun giveLike(@Body like: Like): Response<Any>

    @HTTP(method = "DELETE", path = "likes", hasBody = true)
    suspend fun deleteLike(@Body like: Like): Response<Any>

    class MatchUPAPI {
        companion object {
            private var mAPI: RetrofitService? = null

            @Synchronized
            fun API(): RetrofitService {
                if (mAPI == null) {
                    val client: OkHttpClient = getUnsafeOkHttpClient()
                    val gsondateformat = GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .create();
                    mAPI = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create(gsondateformat))
                        .baseUrl("https://52.45.133.37:443")
                        .client(client)
                        .build()
                        .create(RetrofitService::class.java)
                }
                return mAPI!!
            }

            private fun getUnsafeOkHttpClient(): OkHttpClient {
                try {
                    // Create a trust manager that does not validate certificate chains
                    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                    )

                    // Install the all-trusting trust manager
                    val sslContext = SSLContext.getInstance("SSL")
                    sslContext.init(null, trustAllCerts, SecureRandom())
                    // Create an ssl socket factory with our all-trusting manager
                    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

                    val builder = OkHttpClient.Builder()
                    builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    builder.hostnameVerifier { hostname, session -> true }

                    val okHttpClient = builder.build()
                    return okHttpClient
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}