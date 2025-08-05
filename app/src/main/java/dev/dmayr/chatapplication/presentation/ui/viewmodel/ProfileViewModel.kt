package dev.dmayr.chatapplication.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dmayr.chatapplication.data.domain.model.User
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : ViewModel() {

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    private val _profileUpdateStatus = MutableLiveData<Boolean>()
    val profileUpdateStatus: LiveData<Boolean> = _profileUpdateStatus

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            // For MVP, provide dummy data
            _userProfile.value =
                User(id = userId, username = "John Doe", profileImageUrl = "john.doe@example.com")
        }
    }

    fun updateUserProfile(newEmail: String) {
        viewModelScope.launch {
            val currentUser = _userProfile.value
            if (currentUser != null) {
                val updatedUser =
                    currentUser.copy(profileImageUrl = newEmail)
                _userProfile.value = updatedUser
                _profileUpdateStatus.value = true
            } else {
                _profileUpdateStatus.value = false
            }
        }
    }
}
