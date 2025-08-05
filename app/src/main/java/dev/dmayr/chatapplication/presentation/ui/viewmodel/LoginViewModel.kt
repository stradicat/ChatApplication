package dev.dmayr.chatapplication.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dmayr.chatapplication.data.domain.model.User
import dev.dmayr.chatapplication.data.domain.usecase.AuthenticateUserUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    private val _authenticationResult = MutableLiveData<Result<User>>()
    val authenticationResult: LiveData<Result<User>> = _authenticationResult

    fun authenticate(username: String, password: String) {
        viewModelScope.launch {
            _authenticationResult.value = authenticateUserUseCase(username, password)
        }
    }
}
