package dev.dmayr.chatapplication.data.repository

import dev.dmayr.chatapplication.data.domain.model.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
) : AuthRepository {
    override suspend fun authenticate(username: String, password: String): Result<User> {

        return if (username == "testuser" && password == "password") {
            Result.success(User(id = "user123", username = "testuser"))
        } else {
            Result.failure(Exception("Invalid username or password"))
        }
    }
}
