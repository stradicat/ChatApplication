package dev.dmayr.chatapplication.data.domain.model

data class User(
    val id: String,
    val username: String,
    val profileImageUrl: String? = null
)
