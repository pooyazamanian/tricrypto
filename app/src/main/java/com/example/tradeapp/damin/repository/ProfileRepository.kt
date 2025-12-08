package com.example.tradeapp.damin.repository

import com.example.tradeapp.damin.model.Profile
import io.github.jan.supabase.postgrest.result.PostgrestResult
import java.util.UUID

interface ProfileRepository {
    suspend fun upsertProfile(profile: Profile): PostgrestResult?
    suspend fun getProfile(userId: UUID): PostgrestResult?
}