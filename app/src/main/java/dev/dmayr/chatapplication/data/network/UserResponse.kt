package dev.dmayr.chatapplication.data.network

data class UserResponse(
    val id: String,
    val name: String,
    val email: String?,
    val isOnline: Boolean,
    val lastSeen: Long?
)
