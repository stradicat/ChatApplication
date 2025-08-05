package dev.dmayr.chatapplication.data.repository

import dev.dmayr.chatapplication.domain.model.User

interface AuthRepository {
    suspend fun authenticate(username: String, password: String): Result<User>
    // suspend fun register(username: String, password: String): Result<User> // ej. para registro de usuario
}
