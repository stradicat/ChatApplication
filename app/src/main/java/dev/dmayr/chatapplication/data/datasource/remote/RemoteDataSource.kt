package dev.dmayr.chatapplication.data.datasource.remote

import dev.dmayr.chatapplication.data.network.ApiService
import dev.dmayr.chatapplication.data.network.AuthResponse
import dev.dmayr.chatapplication.data.network.CreateRoomRequest
import dev.dmayr.chatapplication.data.network.LoginRequest
import dev.dmayr.chatapplication.data.network.RegisterRequest
import dev.dmayr.chatapplication.data.network.RoomResponse
import dev.dmayr.chatapplication.data.network.UserResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getChatRooms(): Result<List<RoomResponse>> {
        return try {
            val response = apiService.getChatRooms()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch rooms: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRoom(name: String, description: String?): Result<RoomResponse> {
        return try {
            val request = CreateRoomRequest(name, description)
            val response = apiService.createRoom(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to create room: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRoomUsers(roomId: String): Result<List<UserResponse>> {
        return try {
            val response = apiService.getRoomUsers(roomId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch users: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, password: String? = null): Result<AuthResponse> {
        return try {
            val request = LoginRequest(username, password)
            val response = apiService.login(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String? = null): Result<AuthResponse> {
        return try {
            val request = RegisterRequest(username, email)
            val response = apiService.register(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
