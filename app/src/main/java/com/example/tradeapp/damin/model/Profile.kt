package com.example.tradeapp.damin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    // Profile.id is usually the auth.user id
    @SerialName("id") val id: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val lastModified: String? = null
)
