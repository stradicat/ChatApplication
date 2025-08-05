package dev.dmayr.chatapplication.data.domain.usecase

import dev.dmayr.chatapplication.data.repository.AuthRepository
import dev.dmayr.chatapplication.domain.model.User
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        // This would typically involve calling the AuthRepository to perform authentication
        // For an MVP, this might interact with a mock service or a simple remote authentication endpoint.
        return authRepository.authenticate(username, password)
    }
}
