package com.example.tradeapp.damin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
//@Parcelize
@Serializable
data class Profile(
    // Profile.id is usually the auth.user id
    @SerialName("id") val id: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("full_name")val full_name: String? = null,
    @SerialName("national_id")val national_id: String? = null,
//    @SerialName("created_at") val createdAt: String? = null,
//    @SerialName("updated_at") val lastModified: String? = null
)
//    : Parcelable
