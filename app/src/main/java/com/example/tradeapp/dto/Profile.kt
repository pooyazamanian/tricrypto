package com.example.tradeapp.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
//@Parcelize
@Serializable
data class Profile(
    @SerialName("id") val id: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("national_id") val nationalId: String? = null
)
//    : Parcelable
