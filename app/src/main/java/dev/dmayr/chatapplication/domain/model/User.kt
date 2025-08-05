package dev.dmayr.chatapplication.domain.model

data class User(
    val id: String, // Unique user identifier
    val username: String,
    val profileImageUrl: String? = null // Optional profile image URL
)
