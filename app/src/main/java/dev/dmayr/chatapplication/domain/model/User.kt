package dev.dmayr.chatapplication.domain.model

data class User(
    val id: String,
    val name: String,
    val isOnline: Boolean = false
)
