package dev.dmayr.chatapplication.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "profile_image_url") val profileImageUrl: String?
)
