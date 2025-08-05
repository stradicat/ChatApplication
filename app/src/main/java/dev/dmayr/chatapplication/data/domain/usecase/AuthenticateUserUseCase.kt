package dev.dmayr.chatapplication.data.domain.usecase

import dev.dmayr.chatapplication.data.repository.AuthRepository
import dev.dmayr.chatapplication.domain.model.User
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        return authRepository.authenticate(username, password)
    }
}
