package dev.dmayr.chatapplication.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("rooms")
    suspend fun getChatRooms(): Response<List<RoomResponse>>

    @POST("rooms")
    suspend fun createRoom(@Body request: CreateRoomRequest): Response<RoomResponse>

    @GET("rooms/{roomId}/users")
    suspend fun getRoomUsers(@Path("roomId") roomId: String): Response<List<UserResponse>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}

data class CreateRoomRequest(
    val name: String,
    val description: String? = null
)

data class LoginRequest(
    val username: String,
    val password: String? = null
)

data class RegisterRequest(
    val username: String,
    val email: String? = null
)

data class AuthResponse(
    val token: String,
    val userId: String,
    val username: String
)
